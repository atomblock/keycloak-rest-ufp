package br.gov.mec.sso.spi.corporativo.validator.behavior.functional;

@FunctionalInterface
public interface ICheck {

    long apply(long remainder);

}
