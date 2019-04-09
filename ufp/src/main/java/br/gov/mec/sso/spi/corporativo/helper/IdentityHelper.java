package br.gov.mec.sso.spi.corporativo.helper;

// |--------------------------------------------------------------------------------\
// | IdentityHelper.java                                                            |
// |--------------------------------------------------------------------------------|
// | Classe utilitária (Helper) para validações negociais para persistência de novos|
// | usuários ao Red Hat Single Sign-On.                                            |
// |                                                                                |
// \--------------------------------------------------------------------------------/

import br.gov.mec.sso.spi.corporativo.validator.impl.CNPJValidator;
import br.gov.mec.sso.spi.corporativo.validator.impl.CPFValidator;
import br.gov.mec.sso.spi.corporativo.validator.impl.PasswordValidator;

public class IdentityHelper {

    /**
     * Verifica se o nome de usuário é válido.
     * Formato esperado <code>CPF</code> || <code>CNPJ</code>
     * @param username
     * @return boolean indicando se o nome de usuário é válido.
     */
    public static boolean isValidUsername(String username) {
        return new CPFValidator(username).isValid() || new CNPJValidator(username).isValid();
    }

    /**
     * Verifica se um conjunto de caracteres corresponde ao mínimo exigido para definição de senha.
     * O RH-SSO provê uma fábrica para definição de compliance de senhas.
     * A classe utilitária é necessária apenas para gerar a credencial inicial temporária.
     * @param pw
     * @return boolean indicando se a senha possui força suficiente.
     */
    public static boolean isValidPassword(String pw) {
        return new PasswordValidator(pw).isValid();
    }

}
