package br.gov.mec.sso.spi.corporativo;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

// /--------------------------------------------------------------------------------\
// | CorporativoUserStorageProviderFactory.java                                     |
// |--------------------------------------------------------------------------------|
// | SPI Factory                                                                    |
// \--------------------------------------------------------------------------------/

/* https://github.com/keycloak/keycloak/blob/master/federation/ldap/src/main/java/org/keycloak/storage/ldap/LDAPStorageProviderFactory.java */

public class CorporativoUserStorageProviderFactory implements UserStorageProviderFactory<CorporativoUserStorageProvider> {

    private static final Logger logger = Logger.getLogger(CorporativoUserStorageProvider.class);

    private static final String PROVIDER_NAME = "corporativo-spi";

    @Override
    public CorporativoUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        try {
            InitialContext ctx = initialContext();
            CorporativoUserStorageProvider provider = (CorporativoUserStorageProvider) ctx.lookup("java:global/corporativo-storage-provider/" + CorporativoUserStorageProvider.class.getSimpleName());
            provider.setModel(model);
            provider.setSession(session);
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InitialContext initialContext() throws NamingException {
        return new InitialContext();
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }

    @Override
    public void close() {
        logger.info("closing corporativo spi factory");
    }

    @Override
    public String getHelpText() {
        return "corporativo-user-storage-provider:DBCORPORATIVO";
    }
}
