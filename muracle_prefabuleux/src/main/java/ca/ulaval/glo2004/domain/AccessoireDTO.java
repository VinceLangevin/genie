package ca.ulaval.glo2004.domain;


public abstract class AccessoireDTO extends ListableDTO {
    public TypeObjet Type;
    
    public AccessoireDTO(TypeObjet type) {
        this.Type = type;
    }
}
