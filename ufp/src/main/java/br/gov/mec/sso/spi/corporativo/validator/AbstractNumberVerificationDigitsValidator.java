package br.gov.mec.sso.spi.corporativo.validator;

import br.gov.mec.sso.spi.corporativo.validator.enumerator.ValidatorSignal;

// /--------------------------------------------------------------------------------\
// | AbstractNumberVerificationDigitsValidator.java                                         |
// |--------------------------------------------------------------------------------|
// | Classe abstrata para implementação de recurso de validação baseado em uma      |
// | sequência numérica. Exemplo: CPF, CNPJ entre outros.                           |
// \--------------------------------------------------------------------------------/

public abstract class AbstractNumberVerificationDigitsValidator<T> extends AbstractValidator<T> {

    public AbstractNumberVerificationDigitsValidator(T o) {
        super(o);
    }

    /**
     * Utilizado para ser consumido pelo método <code>getRegex()</code>.
     * @return boolean
     */
    public abstract boolean isRespectingRegex();

    @Override
    public void assign(ValidatorSignal signal) {
        this.setSignal(signal);
    }

    /**
     * Define o tamanho certo do valor a ser tratado.
     * Em caso de tamanho incerto, retornar o valor 0.
     * @return int
     */
    public abstract int getValidLength();

    /**
     * Utilizado para definir o regex padrão para validação do atributo.
     * @return String
     */
    public abstract String getRegex();

    /**
     * Não pode ser generalizado pois cada implementação pode ou não conter
     * um comportamento de limpeza do atributo.
     * @return T
     */
    public abstract T trim();

}
