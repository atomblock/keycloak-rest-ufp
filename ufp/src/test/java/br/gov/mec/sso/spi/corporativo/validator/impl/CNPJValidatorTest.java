package br.gov.mec.sso.spi.corporativo.validator.impl;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CNPJValidatorTest {

    @Test
    public void testInvalidCNPJ1() {
        String cnpj = "35.698.101/0001-11";

        assertFalse(new CNPJValidator(cnpj).isValid());
    }

    @Test
    public void testInvalidCNPJ2() {
        String cnpj = "35.698.101/0001-99";

        assertFalse(new CNPJValidator(cnpj).isValid());
    }

    @Test
    public void testValidCNPJ1() {
        String cnpj = "35.698.101/0001-62";

        assertTrue(new CNPJValidator(cnpj).isValid());
    }

    @Test
    public void testValidCNPJ2() {
        String cnpj = "00.581.434/0001-22";

        assertTrue(new CNPJValidator(cnpj).isValid());
    }

}