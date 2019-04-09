package br.gov.mec.sso.spi.corporativo.helper;

import br.gov.mec.sso.spi.corporativo.validator.impl.CPFValidator;
import org.junit.Test;

import static org.junit.Assert.*;

public class CredentialHelperTest {

    @Test
    public void testEnforcedHashAlgorithm() {
        String cpf = "826.841.551-78";
        String cleaned = new CPFValidator(cpf).trim();
        CredentialHelper ch = new CredentialHelper.PasswordGeneratorBuilder().useDigits(true).useLower(true).usePunctuation(true).useUpper(true).build();
        String pass = ch.generate(10);

        assertTrue(new CPFValidator(cleaned).isValid());
        assertNotNull(CredentialHelper.enforce(pass,cleaned));
    }

    @Test
    public void testEnforcedLeftIsEqualsEnforcedRight() {
        String cpf = "826.841.551-78";
        String cleaned = new CPFValidator(cpf).trim();
        CredentialHelper ch = new CredentialHelper.PasswordGeneratorBuilder().useDigits(true).useLower(true).usePunctuation(true).useUpper(true).build();
        String pass = ch.generate(10);

        String enforcedLeft = CredentialHelper.enforce(pass,cleaned);
        String enforcedRight = CredentialHelper.enforce(pass,cleaned);

        System.out.println("password:\t"+pass);
        System.out.println("secret:\t\t"+cleaned);
        System.out.println("encoded:\t"+enforcedLeft);

        assertEquals(enforcedLeft,enforcedRight);
    }

    @Test
    public void testEnforcedNotEqualsRandomEnforced() {
        String cpf = "826.841.551-78";
        String cleaned = new CPFValidator(cpf).trim();
        CredentialHelper ch = new CredentialHelper.PasswordGeneratorBuilder().useDigits(true).useLower(true).usePunctuation(true).useUpper(true).build();

        String leftPass = ch.generate(20);
        String rightPass = ch.generate(20);

        String enforcedLeft = CredentialHelper.enforce(leftPass,cleaned);
        String enforcedRight = CredentialHelper.enforce(rightPass,cleaned);

        assertNotEquals(enforcedLeft,enforcedRight);
    }

}
