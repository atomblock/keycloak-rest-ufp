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
// | CNPJValidator.java                                                             |
// |--------------------------------------------------------------------------------|
// | Implementação para validação de sequéncia númerica acerca do número de CNPJ    |
// | conforme a especificação                                                       |
// |                                                                                |
// \--------------------------------------------------------------------------------/

public class CNPJValidator extends AbstractNumberVerificationDigitsValidator<String> implements RFBCIEFCSARNumberVerificable<String>, ModulusNCheckSummable<String> {

    public CNPJValidator(String s) {
        super(s);
    }

    @Override
    public String getName() {
        return "CNPJ";
    }

    @Override
    public int getValidLength() {
        return 14;
    }

    @Override
    // ([0-9]{2}[\.]?[0-9]{3}[\.]?[0-9]{3}[\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\.]?[0-9]{3}[\.]?[0-9]{3}[-]?[0-9]{2})
    public String getRegex() { return "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})"; }

    @Override
    public String trim() {
        String trimmer = getEntity().replaceAll("^\\s+", ""); // ltrim
        trimmer = trimmer.replaceAll("\\s+$", ""); // rtrim
        trimmer = trimmer.replaceAll("[.,-/]",""); // remove all dots,commas, dashes and slashes
        return trimmer;
    }

    @Override
    public boolean isValid() {
        String cnpj = trim();

        if (cnpj.isEmpty()) {
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
     * CNPJ have weight equals to 2
     * @return boolean
     */
    private boolean validateNumberWithTwoVerificationDigits() {
        String cnpj = trim();

        IWeight weight = (long x) -> {
            // (pesos) 6 5 4 3 2 9 8 7 6 5 4 3 2
            return (x % 8) + 2;
        };

        if (getValidLength() != cnpj.length()) {
            return false;
        }

        if (cnpj.matches("\\b(\\d)\\1*\\b")) {
            return false;
        }

        String pos1 = cnpj.substring(0,getValidLength()-2);
        String pos2 = cnpj.substring(0,getValidLength()-1);
        List<Integer> vds = Stream.of(cnpj.substring(getValidLength() - 2).split("")).mapToInt((str) ->
                Integer.valueOf(str)).boxed().collect(Collectors.toList());

        return (calculateRFBCIEFCSAR3(cnpj,pos1,weight) == vds.get(0)) && (calculateRFBCIEFCSAR3(cnpj,pos2,weight) == vds.get(1));
    }

    @Override
    public long calculateRFBCIEFCSAR3(String cnpj, String partial, IWeight weight) {
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
