package br.gov.mec.sso.spi.corporativo.validator;

import br.gov.mec.sso.spi.corporativo.validator.behavior.Assignable;
import br.gov.mec.sso.spi.corporativo.validator.enumerator.ValidatorSignal;

// |--------------------------------------------------------------------------------\
// | AbstractValidator.java                                                         |
// |--------------------------------------------------------------------------------|
// | Definição das propriedades primárias que cada especialização deverá conter.    |
// |                                                                                |
// \--------------------------------------------------------------------------------/

public abstract class AbstractValidator<T> implements Assignable {

    private T entity;
    private ValidatorSignal signal;

    public AbstractValidator(T t) {
        this.entity = t;
    }

    /**
     * Implementar este método com a validação a ser especializada para a entidade em si.
     * @return boolean
     */
    public abstract boolean isValid();

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * Registra o nome do recurso ou entidade a ser validada para identificação externa.
     * @return String
     */
    public abstract String getName();

    /**
     * Responsável para obter o status corrente do validador
     * @return ValidatorSignal
     */
    public ValidatorSignal getSignal() {
        if (signal == null) {
            signal = ValidatorSignal.UNCHECKED;
        }
        return signal;
    }

    /**
     * Registra um sinal de validação.
     * @param signal
     */
    public void setSignal(ValidatorSignal signal) {
        this.signal = signal;
    }
}
