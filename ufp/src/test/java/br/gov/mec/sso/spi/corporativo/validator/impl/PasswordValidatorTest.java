package br.gov.mec.sso.spi.corporativo.validator.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class PasswordValidatorTest {

    @Test
    public void testInvalidPassword() {
        String pw = "aaa111";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword2() {
        String pw = "aaaa123";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword3() {
        String pw = "abcd123";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword4() {
        String pw = "abc123#";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword5() {
        String pw = "aAcgRc#";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword6() {
        String pw = "aAcg c#";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword7() {
        String pw = "aAcgHc#>";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testInvalidPassword8() {
        String pw = "aAc\"Hc#>";

        assertFalse(new PasswordValidator(pw).isValid());
    }

    @Test
    public void testValidPassword() {
        String pw = "aAV35$j+#@T]";

        assertTrue(new PasswordValidator(pw).isValid());
    }

}
