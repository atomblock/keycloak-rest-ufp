package br.gov.mec.sso.spi.corporativo;

import br.gov.mec.sso.spi.corporativo.adapter.UserAdapter;
import br.gov.mec.sso.spi.corporativo.helper.CredentialHelper;
import br.gov.mec.sso.spi.corporativo.helper.IdentityHelper;
import br.gov.mec.sso.spi.corporativo.model.Person;
import br.gov.mec.sso.spi.corporativo.model.User;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

// /--------------------------------------------------------------------------------\
// | CorporativoUserStorageProvider.java                                            |
// |--------------------------------------------------------------------------------|
// | SPI para conexão com fonte de dados externa e repositórios de credenciais.     |
// | Responsável por fornecer um mecanismo de ponte entre a fonte de credenciais da |
// | base corporativa do MEC com o metamodelo interno do Red Hat Single Sign-On.    |
// \--------------------------------------------------------------------------------/

@Stateful
@Local(CorporativoUserStorageProvider.class)
public class CorporativoUserStorageProvider
        implements UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        CredentialInputUpdater,
        UserQueryProvider,
        UserRegistrationProvider,
        OnUserCache {

    private static final Logger logger = Logger.getLogger(CorporativoUserStorageProvider.class);

    public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";

    @PersistenceContext(unitName = "corporativo-user-storage-jpa")
    protected EntityManager em;

    private ComponentModel model;

    private KeycloakSession session;

    public void setModel(ComponentModel model) {
        this.model = model;
    }

    public void setSession(KeycloakSession session) {
        this.session = session;
    }

    @Remove
    @Override
    public void close() { }

    public UserAdapter getUserAdapter(UserModel user) {
        UserAdapter adapter = null;
        if (user instanceof CachedUserModel) {
            adapter = (UserAdapter)((CachedUserModel)user).getDelegateForUpdate();
        } else {
            adapter = (UserAdapter)user;
        }
        return adapter;
    }

    // /--------------------------------------------------------------------------------\
    // | Ações de controle de acesso de usuário e segurança                             |
    // |--------------------------------------------------------------------------------|
    // \--------------------------------------------------------------------------------/

    /**
     * O método <code>updateCredential</code> foi customizado para utilizar um mecanismo de enforce na credencial
     * original do usuário.
     * <br>
     * Este provider servirá de middleware entre a senha padrão informada e o que será registrado no banco de dados.
     * Caso outras aplicações necessitem realizar a equivalência das credenciais, elas terão que consumir uma cópia da
     * implementação da classe <code>br.gov.mec.sso.spi.corporativo.helper.CredentialHelperTest</code>
     * @param realmModel
     * @param user
     * @param input
     * @return
     */
    @Override
    public boolean updateCredential(RealmModel realmModel, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;

        UserCredentialModel cred = (UserCredentialModel)input;
        UserAdapter adapter = getUserAdapter(user);

        /**
         * Será gerenciado pelo RH-SSO.
         * Observar a definição da configuração prévia que deverá ser realizada na instância.
         * Item: Authentication, Aba: Password Policy?
         * <code>
         *     Hashing Iterations=20000
         *     Special Characters=1
         *     Uppercase Characters=1
         *     Lowercase Characters=1
         *     Digits=1
         *     Not Username=true
         * </code>
         */
         if (!IdentityHelper.isValidPassword(cred.getValue())) {
             return false;
         }

         adapter.setPassword(CredentialHelper.enforce(cred.getValue(),adapter.getUsername()));

         return true;
    }

    @Override
    public void disableCredentialType(RealmModel realmModel, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) return;
        getUserAdapter(user).setPassword(null);
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realmModel, UserModel user) {
        if (getUserAdapter(user).getPassword() != null) {
            Set<String> set = new HashSet<>();
            set.add(CredentialModel.PASSWORD);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return CredentialModel.PASSWORD.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType) && getPassword(user) != null;
    }

    public String getPassword(UserModel user) {
        String password = null;

        if (user instanceof CachedUserModel) {
            password = (String)((CachedUserModel)user).getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (user instanceof UserAdapter) {
            password = ((UserAdapter)user).getPassword();
        }
        return password;
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        UserCredentialModel cred = (UserCredentialModel)input;
        String password = getPassword(user);

        UserAdapter adapter = getUserAdapter(user);
        String enforced = CredentialHelper.enforce(cred.getValue(),adapter.getUsername());

        return password != null && password.equals(enforced);
    }

    // /--------------------------------------------------------------------------------\
    // | Ações de recuperação de usuário                                                |
    // |--------------------------------------------------------------------------------|
    // \--------------------------------------------------------------------------------/

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        logger.info("getUserById: " + id);

        Long persistenceId = Long.valueOf(StorageId.externalId(id));

        User entity = em.find(User.class, persistenceId);

        if (entity == null || entity.getCoPessoa() == null) {
            logger.info("could not find user by id: " + id);
            return null;
        }

        logger.info("User id found (CO_PESSOA): " + entity.getCoPessoa());

        return new UserAdapter(session, realm, model, entity);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) { // NO_LOGIN
        logger.info("getUserByUsername: " + username);

        TypedQuery<User> query = em.createNamedQuery("getUserByUsername", User.class);
        query.setParameter("username", username);

        List<User> result = query.getResultList();

        if (result.isEmpty()) {
            logger.info("could not find username: " + username);
            return null;
        }

        return new UserAdapter(session, realm, model, result.get(0));
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        TypedQuery<User> query = em.createNamedQuery("getUserByEmail", User.class);
        query.setParameter("email", email);

        List<User> result = query.getResultList();

        if (result.isEmpty()) {
            logger.info("could not find user by email: " + email);
            return null;
        }

        return new UserAdapter(session, realm, model, result.get(0));
    }

    // /--------------------------------------------------------------------------------\
    // | Ações de estatística e filtro de usuários                                      |
    // |--------------------------------------------------------------------------------|
    // \--------------------------------------------------------------------------------/

    @Override
    public int getUsersCount(RealmModel realmModel) {
        Query query = em.createNamedQuery("getUserCount", User.class);
        Object count = query.getSingleResult();
        return ((Number)count).intValue();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return getUsers(realm, -1, -1);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        TypedQuery<User> query = em.createNamedQuery("getAllUsers", User.class);

        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }

        List<User> results = query.getResultList();
        List<UserModel> users = new LinkedList<>();
        for (User entity : results) users.add(new UserAdapter(session, realm, model, entity));

        return users;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return searchForUser(search, realm, -1, -1);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        TypedQuery<User> query = em.createNamedQuery("searchForUser", User.class);
        query.setParameter("search", "%" + search.toLowerCase() + "%");
        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }
        List<User> results = query.getResultList();
        List<UserModel> users = new LinkedList<>();
        for (User entity : results) users.add(new UserAdapter(session, realm, model, entity));
        return users;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel) { // TODO - verificar necessidade
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel, int i, int i1) { // TODO - verificar necessidade
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel, int i, int i1) { // TODO - verificar necessidade
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel) { // TODO - verificar necessidade
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String s, String s1, RealmModel realmModel) { // TODO - verificar necessidade
        return Collections.EMPTY_LIST;
    }

    // /--------------------------------------------------------------------------------\
    // | Ações de manipulação de usuário                                                |
    // |--------------------------------------------------------------------------------|
    // \--------------------------------------------------------------------------------/

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        if (!IdentityHelper.isValidUsername(username)) {
            return null;
        }

        Person p = new Person();
        p.setNoPessoa("Sistema");
        p.setNuCpfcnpj(username);
        p.setTpPessoa(new Long(1));
        em.persist(p);

        User entity = new User();
        CredentialHelper temporaryCredential = new CredentialHelper.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .usePunctuation(true)
                .build();

        entity.setCoPessoa(p.getCoPessoa());
        entity.setNoLogin(username); // NO_LOGIN <= NU_CPFCNPJ
        entity.setStUsuarioBloqueado("0");
        entity.setStEmailVerificado("0");
        entity.setStAlteraSenha("1");
        entity.setDsEmailRegra("1");
        entity.setDsEmail("-");

        /**
         * Esta senha randômica não garante acesso direto ao usuário.
         * @see br.gov.mec.sso.spi.corporativo.CorporativoUserStorageProvider.isValid()
         * O usuário deverá ser forçado ao reset da senha.
         */
        entity.setDsSenha(temporaryCredential.generate(10));
        em.persist(entity);

        UserAdapter userAdapter = new UserAdapter(session, realm, model, entity);
        logger.info("added user: " + username);

        return userAdapter;
    }

    /**
     * Conforme política de administração de dados do MEC, as operações de exclusão
     * de dados das tabelas deverá ser lógica. Entretanto, os usuários do RH-SSO
     * deverão ser removidos fisicamente para não possibilitar falhas de segurança.
     * <code>
     *     em.remove(entity);
     * </code>
     * @param realmModel
     * @param user
     * @return
     */
    @Override
    public boolean removeUser(RealmModel realmModel, UserModel user) {

        String id = StorageId.externalId(user.getId());
        User entity = em.find(User.class, Long.parseLong(id));

        if (entity == null) return false;

        em.remove(entity);

        logger.info("removed user: " + entity.getNoLogin());

        return true;
    }

    /**
     * [FIXME]
     * ESTE MÉTODO NÃO ESTÁ SENDO INTERPRETADO PELA VERSÃO ATUAL DO RH-SSO
     * O método getCachedWith() não está implementado, ocasionando erro ao adicionar o provider com CACHE.
     * Configurar o provider na instância do RH-SSO sem o CACHE (CachePolicy=NO_CACHE)
     * @param realmModel
     * @param user
     * @param delegate
     */
    @Override
    public void onCache(RealmModel realmModel, CachedUserModel user, UserModel delegate) {
        String password = ((UserAdapter)delegate).getPassword();

        if (password != null) {
            user.getCachedWith().put(PASSWORD_CACHE_KEY, password);
        }
    }


}
