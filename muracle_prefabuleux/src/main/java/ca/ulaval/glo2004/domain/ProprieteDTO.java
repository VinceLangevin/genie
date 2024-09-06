package ca.ulaval.glo2004.domain;

public class ProprieteDTO {
    private final String nom;
    private final String nomAffichage;
    private final boolean lectureSeule;
    private Object valeur;
    
    public ProprieteDTO(String nom, Object valeur) {
        this(nom, nom, valeur, false);
    }
    
    public ProprieteDTO(String nom, Object valeur, boolean lectureSeule) {
        this(nom, nom, valeur, lectureSeule);
    }
    
    public ProprieteDTO(String nom, String nomAffichage, Object valeur) {
        this(nom, nomAffichage, valeur, false);
    }
    
    public ProprieteDTO(String nom, String nomAffichage, Object valeur, boolean lectureSeule) {
        this.nom = nom;
        this.nomAffichage = nomAffichage;
        this.valeur = valeur;
        this.lectureSeule = lectureSeule;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getNomAffichage() {
        return this.nomAffichage;
    }
    
    public boolean getLectureSeule() {
        return lectureSeule;
    }
       
    public Object getValeur() {
        return valeur;
    }
    
    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }
}
