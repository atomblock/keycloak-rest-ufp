package br.gov.mec.sso.spi.corporativo.validator.impl;

import br.gov.mec.sso.spi.corporativo.validator.AbstractPasswordValidator;
import br.gov.mec.sso.spi.corporativo.validator.enumerator.ValidatorSignal;

public class PasswordValidator extends AbstractPasswordValidator<String> {

    public PasswordValidator(String o) {
        super(o);
    }

    @Override
    public boolean isRespectingRegex() {
        return getEntity().matches(getRegex());
    }

    /**
     * Define o tamanho mínimo aceito.
     * @return int
     */
    @Override
    public int getValidLength() {
        return 6;
    }

    /**
     * This regular expression match can be used for validating strong passwords.
     * It expects at least:
     * 1 small-case letter.
     * 1 upper-case letter.
     * 1 digit.
     * 1 special character.
     * <br/>
     * The length should be between 6-15 characters.
     * The sequence of the characters is not important.
     * It must not contains whitespace characters.
     * It must not contains more than 3 repeated characters.
     * It must not contains: &amp; &gt; &lt; &quot;.
     * @return String
     */
    @Override
    public String getRegex() {
        return "(?=^.{6,15}$)(?!.*(.)\\1{3})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$$%^*()_+}{:;'?/.,])(?!.*\\s).*$";
    }

    /**
     * Não aplicável para validação de senha.
     * @return String
     */
    @Override
    public String trim() {
        return null;
    }

    @Override
    public boolean isValid() {
        if (getEntity().isEmpty()) {
            assign(ValidatorSignal.INVALID);
            return false;
        }

        if (!isRespectingRegex()) {
            assign(ValidatorSignal.INVALID);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "UserPassword";
    }
}
