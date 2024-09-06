package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class ObjetPhysique implements java.io.Serializable {
    private final TypeObjet type;
    
    protected Mesure largeur;
    protected Mesure hauteur;
    protected PointMesure position;
    protected boolean estSelectionne;
    
    public ObjetPhysique(TypeObjet type, Mesure largeur, Mesure hauteur) {
        this(type, largeur, hauteur, new PointMesure());
    }
    
    public ObjetPhysique(TypeObjet type, Mesure largeur, Mesure hauteur, PointMesure position) {
        this.type = type;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.position = position;     
        this.estSelectionne = false;
    }
    
    public abstract ListableDTO getDTO();
    protected abstract Salle getSalle();
    public abstract void modifier(ListableDTO dto);
    public abstract void valider(List<Erreur> erreurs);
    
    public Mesure getLargeur() {
        return this.largeur;
    }
    
    public Mesure getHauteur() {
        return this.hauteur;
    }
    
    public PointMesure getPosition() {
        return this.position;
    }
    
    public void setPosition(PointMesure position) {
        this.position = position;
    }
    public void deplacer(PointMesure position){
        setPosition(new PointMesure(position.getX().soustraire(largeur.multiplier(new Mesure(0,1,2))), position.getY().soustraire(hauteur.multiplier(new Mesure(0,1,2)))));
    }
    
    public TypeObjet getType() {
        return this.type;
    }
    
    public boolean getEtatSelection() {
        return this.estSelectionne;
    }
    
    public void setEtatSelection(boolean estSelectionne) {
        this.estSelectionne = estSelectionne;
    }
    
    public void selectionner() {
        setEtatSelection(true);
    }
    
    public void deselectionner() {
        setEtatSelection(false);
    }
    
    public boolean estSelectionner() {
        return this.estSelectionne;
    }
    
    public Rectangle2D getBornes() {
        Rectangle2D bornes = new Rectangle2D.Double(position.getX().getTotal(), 
                                                    position.getY().getTotal(), 
                                                    largeur.getTotal(), 
                                                    hauteur.getTotal());
        
        return bornes;
    }
    
    public boolean contient(ObjetPhysique autre) {
        return getBornes().contains(autre.getBornes());
    }

    public boolean collisionner(ObjetPhysique autre) {
        return getBornes().intersects(autre.getBornes());
    }
    
    public boolean collisionner(PointMesure coord) {
        return getBornes().contains(coord.getX().getTotal(),
                                    coord.getY().getTotal());
    }
    
    protected boolean collisionner(PointMesure coord, PointMesure hautGauche, PointMesure basDroite) {        
        if(hautGauche.getX().compareTo(coord.getX()) > 0 || 
           hautGauche.getY().compareTo(coord.getY()) > 0 ||
           basDroite.getX().compareTo(coord.getX()) < 0 ||
           basDroite.getY().compareTo(coord.getY()) < 0) {
            
            return false;
        }
        
        return true;
    }
    
    protected boolean collisionnerObjet(PointMesure l1,PointMesure r1,PointMesure l2,PointMesure r2){
        if(l1.getX() == r1.getX() || l1.getY() ==r1.getY()||r2.getX() ==l2.getX()||l2.getY() ==r2.getY())
        {
            return false;
        }
        if(l1.getX().compareTo(r2.getX()) == 1||l2.getX().compareTo(r1.getX()) == 1)
        {
            return false;
        }
        if(r1.getY().compareTo(l2.getY()) ==-1 ||r2.getY().compareTo(l1.getY()) ==-1){
            return false;
        }
        return true;
    }
    public boolean supprimer() {
        return false;
    }    
    
    protected String attrToString() {
        return String.format("largeur:\"%.2f\" hauteur:\"%.2f\" position:\"[%.2f, %.2f]\" estSelectionne:\"%b\"", 
                             this.largeur.getTotal(), 
                             this.hauteur.getTotal(), 
                             this.position.getX().getTotal(), 
                             this.position.getY().getTotal(),
                             this.estSelectionne);
    }
}
