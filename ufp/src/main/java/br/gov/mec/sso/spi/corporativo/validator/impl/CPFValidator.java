package br.gov.mec.sso.spi.corporativo.validator.impl;

import br.gov.mec.sso.spi.corporativo.validator.AbstractNumberVerificationDigitsValidator;
import br.gov.mec.sso.spi.corporativo.validator.behavior.ModulusNCheckSummable;
import br.gov.mec.sso.spi.corporativo.validator.behavior.RFBCIEFCSARNumberVerificable;
import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.ICheck;
import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.IWeight;
import br.gov.mec.sso.spi.corporativo.validator.enumerator.ValidatorSignal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// /--------------------------------------------------------------------------------\
// | CPFValidator.java                                                              |
// |--------------------------------------------------------------------------------|
// | Implementação para validação de sequéncia númerica acerca do número de CPF     |
// | conforme a especificação                                                       |
// |                                                                                |
// \--------------------------------------------------------------------------------/

public class CPFValidator extends AbstractNumberVerificationDigitsValidator<String> implements RFBCIEFCSARNumberVerificable<String>, ModulusNCheckSummable<String> {

    public CPFValidator(String s) {
        super(s);
    }

    @Override
    public String getName() {
        return "CPF";
    }

    @Override
    public int getValidLength() {
        return 11;
    }

    @Override
    // (^\d{3}\x2E\d{3}\x2E\d{3}\x2D\d{2}$)
    public String getRegex() {
        return "(^\\d{3}\\d{3}\\d{3}\\d{2}$)";
    }

    @Override
    public String trim() {
        String trimmer = getEntity().replaceAll("^\\s+", ""); // ltrim
        trimmer = trimmer.replaceAll("\\s+$", ""); // rtrim
        trimmer = trimmer.replaceAll("[.,-]", ""); // remove all dots,commas and dashes
        return trimmer;
    }

    @Override
    public boolean isValid() {
        String cpf = trim();

        if (cpf.isEmpty()) {
            assign(ValidatorSignal.INVALID);
            return false;
        }

        if (!isRespectingRegex()) {
            assign(ValidatorSignal.INVALID);
            return false;
        }

        if (!validateNumberWithTwoVerificationDigits()) {
            assign(ValidatorSignal.INVALID);
            return false;
        }

        return true;
    }

    @Override
    public boolean isRespectingRegex() {
        return trim().matches(getRegex());
    }

    /**
     * CPF have weight equals to 2
     *
     * @return boolean
     */
    private boolean validateNumberWithTwoVerificationDigits() {
        String cpf = trim();

        IWeight weight = (long x) -> {
            // (pesos) 10 9 8 7 6 5 4 3 2 2
            return (x % 10) + 2;
        };

        if (getValidLength() != cpf.length()) {
            return false;
        }

        if (cpf.matches("\\b(\\d)\\1*\\b")) {
            return false;
        }

        String pos1 = cpf.substring(0, getValidLength() - 2);
        String pos2 = cpf.substring(0, getValidLength() - 1);
        List<Integer> vds = Stream.of(cpf.substring(getValidLength() - 2).split("")).mapToInt((str) ->
                Integer.valueOf(str)).boxed().collect(Collectors.toList());

        return (calculateRFBCIEFCSAR3(cpf, pos1, weight) == vds.get(0)) && (calculateRFBCIEFCSAR3(cpf, pos2, weight) == vds.get(1));
    }

    @Override
    public long calculateRFBCIEFCSAR3(String cpf, String partial, IWeight weight) {
        ICheck check = (long remainder) -> {
            long result = 0;

            if (remainder > 1) {
                result = 11 - remainder;
            }

            return result;
        };

        return calculateModulusNCheckSum(partial, 11, check, weight);
    }

    public long calculateModulusNCheckSum(String partial, int modulus, ICheck check, IWeight weight) {
        return ModulusNCheckSummable.calculateModulusNCheckSum(partial, 11, check, weight);
    }

}