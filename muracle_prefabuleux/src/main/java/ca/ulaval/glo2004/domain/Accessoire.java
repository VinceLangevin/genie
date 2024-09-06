package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;

public abstract class Accessoire extends ObjetPhysique {
    private Cote cote;
    
    public Accessoire(TypeObjet type, Mesure largeur, Mesure hauteur) {
        this(type, largeur, hauteur, new PointMesure());       
    }
    
    public Accessoire(TypeObjet type, Mesure largeur, Mesure hauteur, PointMesure position) {
        super(type, largeur, hauteur, position);
    }
    
    public void setParent(Cote cote) {
        this.cote = cote;
    }
    
    public void update() {
        
    }
    
    @Override
    public boolean supprimer() {
        if(this.cote != null) {
            this.cote.supprimerAccessoire(this);
            
            return true;
        }
        
        return false;
    }
    
    @Override
    protected Salle getSalle() {
        return cote.getSalle();
    }
    
    protected Cote getCote() {
        return cote;
    }
    
    @Override
    public String toString() {
        return String.format("<Accessoire %s\\>", attrToString());
    }
}
