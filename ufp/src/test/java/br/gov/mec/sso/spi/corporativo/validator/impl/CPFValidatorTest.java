package br.gov.mec.sso.spi.corporativo.validator.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CPFValidatorTest {

    @Test
    public void testValidCPF() {
        String cpf = "826.841.551-78";

        assertTrue(new CPFValidator(cpf).isValid());
    }

    @Test
    public void testValidCPF2() {
        String cpf = "010.901.321-23";

        assertTrue(new CPFValidator(cpf).isValid());
    }

    @Test
    public void testValidCPF3() {
        String cpf = "01090132123";

        assertTrue(new CPFValidator(cpf).isValid());
    }

    @Test
    public void testInvalidCPF() {
        String cpf = "111.111.111-11";

        assertFalse(new CPFValidator(cpf).isValid());
    }

    @Test
    public void testInvalidCPF2() {
        String cpf = "121.131.141-90";

        assertFalse(new CPFValidator(cpf).isValid());
    }

    @Test
    public void testInvalidCPF3() {
        String cpf = "83116656304";

        assertFalse(new CPFValidator(cpf).isValid());
    }

}
