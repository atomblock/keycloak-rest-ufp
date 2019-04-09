package br.gov.mec.sso.spi.corporativo.validator.enumerator;

// |--------------------------------------------------------------------------------\
// | ValidatorSignal.java                                                           |
// |--------------------------------------------------------------------------------|
// | Enum para servir de sinalizador para as implementações de validação.           |
// |                                                                                |
// \--------------------------------------------------------------------------------/

public enum ValidatorSignal {

    UNCHECKED(0L,"UNCHECKED",null),
    VALID(1L,"VALID",null),
    INVALID(2L,"INVALID",null);

    private Long code;
    private String description;
    private String name;

    ValidatorSignal(Long code, String description, String name) {
        this.code = code;
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
