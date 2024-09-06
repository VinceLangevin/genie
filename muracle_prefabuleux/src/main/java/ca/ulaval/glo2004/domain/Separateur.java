package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Rectangle2D;
import java.util.List;


public class Separateur extends ObjetPhysique implements Comparable<Separateur> {
    private Cote cote;
    
    public Separateur(Cote coteParent, Mesure positionX) {
        this(coteParent, new PointMesure(positionX, new Mesure(0)));
    }
    
    public Separateur(Cote parent, PointMesure position) {              
        super(TypeObjet.Separateur, new Mesure(2), parent.hauteur, position);
        
        this.cote = parent;
        this.position.setY(new Mesure(0));
    }
       
    public void deplacer(Mesure distance) {        
        PointMesure limiteDeplacement = cote.getPosition();
        limiteDeplacement.translate(cote.getLargeur(), cote.getHauteur());
        
        this.position.translate(distance, 
                                new Mesure(0), 
                                cote.getPosition(), 
                                limiteDeplacement);      
    }
    
    @Override
    public void modifier(ListableDTO separateurDTO) {
        SeparateurDTO DTO = (SeparateurDTO) separateurDTO; 
        
        if(DTO.Position.getX().compareTo(this.position.getX()) != 0) {
            DTO.Position.setY(new Mesure());
            this.position = DTO.Position;   
            
            cote.genererMurs();
        }
    }
    @Override 
    public void setPosition(PointMesure position)
    {
        if(position.getX().compareTo(this.position.getX()) != 0) {
            position.setY(new Mesure());
            this.position = position;   
            
            cote.genererMurs();
        }
    }
    @Override
    public boolean supprimer() {
        this.cote.supprimerSeparateur(this);
        
        return true;
    }
    
    @Override
    public Rectangle2D getBornes() {
        Mesure demiLargeur = largeur.multiplier(new Mesure(0, 1, 2));
        
        Rectangle2D bornes = new Rectangle2D.Double(position.getX().soustraire(demiLargeur).getTotal(), 
                                                    position.getY().getTotal(), 
                                                    largeur.getTotal(), 
                                                    hauteur.getTotal());
        
        return bornes;
    }  
    
    @Override
    public ListableDTO getDTO(){
        return new SeparateurDTO(this);
    }
    
    @Override
    protected Salle getSalle() {
        return cote.getSalle();
    }
    
    protected Cote getCote() {
        return cote;
    }
    
    public void update() {
        this.hauteur = getCote().getHauteur();
        this.largeur = getSalle().getEpaisseurPliSoudure().multiplier(new Mesure(2));
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        if (this.cote.hauteur == null){
            erreurs.add(new Erreur("Le séparateur doit avoir une hauteur"));
        }
        if(this.cote.largeur == null){
            erreurs.add(new Erreur("Le séparateur doit avoir une largeur"));
        }
        if(this.cote.position == null){
            erreurs.add(new Erreur("Le séparateur doit avoir une position"));
        }      
    }
    
    @Override
    public String toString() {
        return String.format("<Separateur %s\\>", attrToString());
    }

    @Override
    public int compareTo(Separateur o) {
        return this.position.getX().compareTo(o.getPosition().getX());
    }

    
}
