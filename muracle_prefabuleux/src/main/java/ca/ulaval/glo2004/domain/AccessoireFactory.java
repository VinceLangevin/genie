package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;


public class AccessoireFactory {
    private Salle salle;
    
    private final Mesure largeurParDefautFenetre = new Mesure(40);
    private final Mesure hauteurParDefautFenetre = new Mesure(50);
    private final Mesure ecartMoulureParDefautFenetre = new Mesure(0, 1, 8);

    private final Mesure largeurParDefautPorte = new Mesure(39);
    private final Mesure hauteurParDefautPorte  = new Mesure(88);
    
    private final Mesure largeurParDefautPriseElectrique = new Mesure(2, 0, 2);
    private final Mesure hauteurParDefautPriseElectrique = new Mesure(4, 0, 2);
    
    private final Mesure largeurParDefautRetourAir = new Mesure(12);
    
    
    public AccessoireFactory(Salle salle) {
        this.salle = salle;        
    }
    
    public void setSalle(Salle salle) {
        this.salle = salle;
    }
    
    public Accessoire creerAccessoire(TypeObjet typeAccessoire, PointMesure position) {
        Accessoire nouvelAccessoire;
        
        switch(typeAccessoire) {
            case Fenetre:
                nouvelAccessoire = new Fenetre(largeurParDefautFenetre, hauteurParDefautFenetre, position, ecartMoulureParDefautFenetre);
                break;
                
            case Porte:
                
                nouvelAccessoire = new Porte(largeurParDefautPorte, hauteurParDefautPorte, position);
                break;
                
            case PriseElectrique:
                nouvelAccessoire =  new PriseElectrique(largeurParDefautPriseElectrique, hauteurParDefautPriseElectrique, position);
                break;
                
            case RetourAir: 
                nouvelAccessoire = new RetourAir(largeurParDefautRetourAir, salle.getHauteurRetoursAir(), position);
                break;
                
            default:
                throw new IllegalArgumentException(String.format("Le type d'accessoire {0} n'est pas géré.", typeAccessoire));
        }
        
        decentrerPosition(nouvelAccessoire);
        
        if(typeAccessoire == TypeObjet.Porte) {
            position.setY(salle.getHauteur().soustraire(hauteurParDefautPorte));
        }
        
        return nouvelAccessoire;
    }
    
    /**
     * Calcul les coordonnées du point en haut à gauche de l'accessoire à partir de ses dimensions 
     * et des coordonnées de son point milieu
     * @return les coordonnées du point en haut à gauche de l'accessoire
     */
    private void decentrerPosition(Accessoire accessoire) {
        PointMesure nouvellePosition = accessoire.getPosition();
        nouvellePosition.translate(accessoire.getLargeur().multiplier(new Mesure(0, -1, 2)), 
                                   accessoire.getHauteur().multiplier(new Mesure(0, -1, 2)));
        
        accessoire.setPosition(nouvellePosition);
    }
}
