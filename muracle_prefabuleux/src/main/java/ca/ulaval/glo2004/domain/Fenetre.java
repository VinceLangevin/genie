package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Rectangle2D;
import java.util.List;


public class Fenetre extends Accessoire {
    private Mesure ecartMoulure;
    
    public Fenetre(Mesure largeur, Mesure hauteur, PointMesure position, Mesure ecartMoulure) {
        super(TypeObjet.Fenetre, largeur, hauteur, position);
        
        this.ecartMoulure = ecartMoulure;
    }
    
    public Mesure getEcartMoulure() {
        return ecartMoulure;
    }
    
    public void setEcartMoulure(Mesure ecartMoulure) {
        this.ecartMoulure = ecartMoulure;
    }

    @Override
    public ListableDTO getDTO() {
        return new FenetreDTO(this);
    }
    
    @Override
    public Rectangle2D getBornes() {
        Mesure doubleMoulure = ecartMoulure.multiplier(new Mesure(2));
        Rectangle2D bornes = new Rectangle2D.Double(position.getX().soustraire(ecartMoulure).getTotal(), 
                                                    position.getY().soustraire(ecartMoulure).getTotal(), 
                                                    largeur.ajouter(doubleMoulure).getTotal(), 
                                                    hauteur.ajouter(doubleMoulure).getTotal());
        
        return bornes;
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        Cote cote = this.getCote();
        if(this.hauteur == null){
            erreurs.add(new Erreur("La valeur de la hauteur de la fenêtre ne doit pas être vide"));
        }
        if(this.position == null){
            erreurs.add(new Erreur("La valeur de la position de la fenêtre ne doit pas être vide"));
        }
        if(this.largeur == null){
            erreurs.add(new Erreur("La valeur de la largeur de la fenêtre ne doit pas être vide"));
        }
        if(this.ecartMoulure == null){
            erreurs.add(new Erreur("La valeur de l'écart Moulure de la fenêtre ne doit pas être vide"));
        }       
    }
    
    @Override
    public void modifier(ListableDTO DTO) {
        FenetreDTO fenetreDTO = (FenetreDTO) DTO;
        this.ecartMoulure = fenetreDTO.EcartMoulure;
        this.hauteur = fenetreDTO.Hauteur;
        this.largeur = fenetreDTO.Largeur;
        this.position = fenetreDTO.Position;
    }
    
    @Override
    public String toString() {
        return String.format("<Fenetre %s ecartMoulure=\"%.2f\"\\>", attrToString(), ecartMoulure.getTotal());
    }
}
