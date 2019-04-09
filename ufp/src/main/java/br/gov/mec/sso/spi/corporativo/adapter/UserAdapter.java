package br.gov.mec.sso.spi.corporativo.adapter;

import br.gov.mec.sso.spi.corporativo.model.User;
import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// /--------------------------------------------------------------------------------\
// | UserAdapter.java                                                               |
// |--------------------------------------------------------------------------------|
// | Adapter pattern para montar a conversação entre a entidade externa e o modelo  |
// | do Red Hat Single Sign-on.                                                     |
// \--------------------------------------------------------------------------------/

public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger logger = Logger.getLogger(UserAdapter.class);

    protected User entity;

    protected String keycloakId;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, User entity) {
        super(session, realm, model);
        this.entity = entity;
        keycloakId = StorageId.keycloakId(model, entity.getCoPessoa().toString());
    }

    public String getPassword() {
        return entity.getDsSenha();
    }

    public void setPassword(String password) {
        entity.setDsSenha(password);
    }

    @Override
    public String getUsername() {
        return entity.getNoLogin();
    }

    @Override
    public void setUsername(String username) {
        entity.setNoLogin(username);
    }

    @Override
    public void setEmail(String email) {
        entity.setDsEmail(email);
    }

    @Override
    public String getEmail() {
        return entity.getDsEmail();
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    @Override
    public String getFirstName() {
        return entity.getFirstName();
    }

    @Override
    public String getLastName() {
        return entity.getLastName();
    }

    @Override
    public void setFirstName(String firstName) {
        entity.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        entity.setLastName(lastName);
    }

    // /--------------------------------------------------------------------------------\
    // | Ações para injeção de propriedades extras do Usuário                           |
    // |--------------------------------------------------------------------------------|
    // \--------------------------------------------------------------------------------/

    @Override
    public  void setSingleAttribute(String name, String value) {
        if (name.equals("dsEmailRegra")) {
            entity.setDsEmailRegra(value);
        } if (name.equals("stUsuarioBloqueado")) {
            entity.setStUsuarioBloqueado(value);
        } if (name.equals("stEmailVerificado")) {
            entity.setStEmailVerificado(value);
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public  void removeAttribute(String name) {
        if (name.equals("dsEmailRegra")) {
            entity.setDsEmailRegra(null);
        } if (name.equals("stUsuarioBloqueado")) {
            entity.setStUsuarioBloqueado(null);
        } if (name.equals("stEmailVerificado")) {
            entity.setStEmailVerificado(null);
        } else {
            super.removeAttribute(name);
        }
    }

    @Override
    public  void setAttribute(String name, List<String> values) {
        if (name.equals("dsEmailRegra")) {
            entity.setDsEmailRegra(values.get(0));
        } if (name.equals("stUsuarioBloqueado")) {
            entity.setStUsuarioBloqueado(values.get(0));
        } if (name.equals("stEmailVerificado")) {
            entity.setStEmailVerificado(values.get(0));
        } else {
            super.setAttribute(name, values);
        }
    }

    @Override
    public  String getFirstAttribute(String name) {
        if (name.equals("dsEmailRegra")) {
            return entity.getDsEmailRegra();
        } if (name.equals("stUsuarioBloqueado")) {
            return entity.getStUsuarioBloqueado();
        } if (name.equals("stEmailVerificado")) {
            return entity.getStEmailVerificado();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();

        attrs.remove("ENABLED");
        attrs.remove("FIRST_NAME");
        attrs.remove("LAST_NAME");
        attrs.remove("EMAIL_VERIFIED");

        all.putAll(attrs);
        all.putSingle("dsEmailRegra", entity.getDsEmailRegra());
        all.putSingle("stUsuarioBloqueado", entity.getStUsuarioBloqueado());
        all.putSingle("stEmailVerificado", entity.getStEmailVerificado());

        return all;
    }

    @Override
    public  List<String> getAttribute(String name) {
        if (name.equals("dsEmailRegra")) {
            List<String> emailRegra = new LinkedList<>();
            emailRegra.add(entity.getDsEmailRegra());
            return emailRegra;
        } if (name.equals("stUsuarioBloqueado")) {
            List<String> stUsuarioBloqueado = new LinkedList<>();
            stUsuarioBloqueado.add(entity.getStUsuarioBloqueado());
            return stUsuarioBloqueado;
        } if (name.equals("stEmailVerificado")) {
            List<String> stEmailVerificado = new LinkedList<>();
            stEmailVerificado.add(entity.getStEmailVerificado());
            return stEmailVerificado;
        } else {
            return super.getAttribute(name);
        }
    }

}