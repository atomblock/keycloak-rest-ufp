package br.gov.mec.sso.spi.corporativo.validator.behavior;

import br.gov.mec.sso.spi.corporativo.validator.enumerator.ValidatorSignal;

public interface Assignable {

    void assign(ValidatorSignal signal);

}
