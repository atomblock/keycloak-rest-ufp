package br.gov.mec.sso.spi.corporativo.validator.behavior;

import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.ICheck;
import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.IWeight;

/**
 *
 * http://normas.receita.fazenda.gov.br/sijut2consulta/link.action?idAto=20139&visao=anotado
 */
public interface RFBCIEFCSARNumberVerificable<T> {

    long calculateRFBCIEFCSAR3(T entity, String partial, IWeight weight);

}
