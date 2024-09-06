package ca.ulaval.glo2004.domain.util;


public class Erreur {
    private final String message;
    
    public Erreur(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
