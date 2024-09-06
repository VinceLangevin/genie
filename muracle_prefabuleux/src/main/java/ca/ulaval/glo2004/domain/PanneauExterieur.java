package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class PanneauExterieur extends Panneau {
    public PanneauExterieur(int indexMur, Mur mur) {
        super(indexMur, mur);
    }
    
    @Override
    protected boolean estExterieur() {
        return true;
    }
    
    @Override
    protected void calculerAire() { 
        this.aire = new Area();        

        double anglePliSoudure = getSalle().getAnglePliSoudure();
        
        double margePliage = getSalle().getMargePliage().getTotal();
        double epaisseurMat = getSalle().getEpaisseurMateriaux().getTotal();
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double differenceHauteur = largeurPliSoudure / Math.tan(Math.toRadians(anglePliSoudure));
        double epaisseurMur = getSalle().getEpaisseurMurs().getTotal();
        
        double hypothenuse = Math.sqrt(2 * Math.pow(epaisseurMur, 2)); //c^2 = a^2 + b^2 = 2*a^2; (Ou a et b = epaisseurMur)
        double epaisseurGauche = mur.getEstCoinGauche() ? hypothenuse : epaisseurMur;
        double epaisseurDroit = mur.getEstCoinDroite() ? hypothenuse : epaisseurMur;
        
        double hauteurEpaisseur = hauteur.getTotal() - (2 * epaisseurMat);
        
        double positionX = 0;
        
        Path2D pliSoudureGauche = new Path2D.Double();
        pliSoudureGauche.moveTo(positionX, epaisseurMat + differenceHauteur);
        pliSoudureGauche.lineTo(positionX, hauteur.getTotal() - (epaisseurMat + differenceHauteur));
        
        positionX += largeurPliSoudure;
        pliSoudureGauche.lineTo(positionX, hauteur.getTotal() - epaisseurMat);
        pliSoudureGauche.lineTo(positionX, epaisseurMat);
        pliSoudureGauche.closePath();
        aire.add(new Area(pliSoudureGauche));
        
        Rectangle2D pliEpaisseurGauche = new Rectangle2D.Double(positionX, epaisseurMat, epaisseurGauche, hauteurEpaisseur);
        aire.add(new Area(pliEpaisseurGauche));
        
        positionX += epaisseurGauche;
        Rectangle2D pliageGauche = new Rectangle2D.Double(positionX, epaisseurMat, margePliage, hauteurEpaisseur);
        aire.add(new Area(pliageGauche));
        
        positionX += margePliage;
        positionPanneauCentral = new Point2D.Double(positionX, 0);
        Rectangle2D surfacePanneau = new Rectangle2D.Double(positionX, 0, largeur.getTotal(), hauteur.getTotal());
        aire.add(new Area(surfacePanneau));
        
        positionX += largeur.getTotal();
        Rectangle2D pliageDroit = new Rectangle2D.Double(positionX, epaisseurMat, margePliage, hauteurEpaisseur);
        aire.add(new Area(pliageDroit));
        
        positionX += margePliage;
        Rectangle2D pliEpaisseurDroit = new Rectangle2D.Double(positionX, epaisseurMat, epaisseurDroit, hauteurEpaisseur);
        aire.add(new Area(pliEpaisseurDroit));
        
        positionX += epaisseurDroit;
        Path2D pliSoudureDroit = new Path2D.Double();
        pliSoudureDroit.moveTo(positionX, epaisseurMat);
        pliSoudureDroit.lineTo(positionX, hauteur.getTotal() - epaisseurMat);
        
        positionX += largeurPliSoudure;
        pliSoudureDroit.lineTo(positionX, hauteur.getTotal() - (epaisseurMat + differenceHauteur));
        pliSoudureDroit.lineTo(positionX, epaisseurMat + differenceHauteur);
        pliSoudureDroit.closePath();
        aire.add(new Area(pliSoudureDroit));
    }

    @Override
    protected Area calculerAireAvecAccessoires() {
        Area aireFinale = (Area) aire.clone();
        
        List<DimensionAccessoire> accessoires = obtenirListeAccessoire();
        for(DimensionAccessoire dimension : accessoires) {
            Rectangle2D trou = new Rectangle2D.Double(dimension.PositionX, dimension.PositionY, dimension.Largeur, dimension.Hauteur);
            
            aireFinale.subtract(new Area(trou));
        }
        
        return aireFinale;
    }
    
    @Override
    protected double calculAireTotalePouces() {
        double anglePliSoudure = getSalle().getAnglePliSoudure();
        
        double margePliage = getSalle().getMargePliage().getTotal();
        double epaisseurMat = getSalle().getEpaisseurMateriaux().getTotal();
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double differenceHauteur = largeurPliSoudure / Math.tan(Math.toRadians(anglePliSoudure));
        double epaisseurMur = getSalle().getEpaisseurMurs().getTotal();
        
        double hypothenuse = Math.sqrt(2 * Math.pow(epaisseurMur, 2)); //c^2 = a^2 + b^2 = 2*a^2; (Ou a et b = epaisseurMur)
        double epaisseurGauche = mur.getEstCoinGauche() ? hypothenuse : epaisseurMur;
        double epaisseurDroit = mur.getEstCoinDroite() ? hypothenuse : epaisseurMur;
        
        double hauteurEpaisseur = hauteur.getTotal() - (2 * epaisseurMat);
        
        double aireTotale = 0;
        
        //Panneau principal
        aireTotale += this.largeur.getTotal() * this.hauteur.getTotal();
        
        //2*Pli soudure
        aireTotale += ((hauteurEpaisseur - (2 * differenceHauteur)) * largeurPliSoudure) * 2; //Rectangle
        aireTotale += (largeurPliSoudure * differenceHauteur) * 2; //4 triangles (donc 2 fois l'aire d'un rectangle)
        
        //2*Marge pliage
        aireTotale += (margePliage * hauteurEpaisseur) * 2;
        
        //PliEpaisseurGauche
        aireTotale += epaisseurGauche * hauteurEpaisseur;
        
        //PliEpaisseurDroit
        aireTotale += epaisseurDroit * hauteurEpaisseur;
        
        List<DimensionAccessoire> accessoires = obtenirListeAccessoire(); 
        for(DimensionAccessoire dimension : accessoires) {
            aireTotale -= dimension.Largeur * dimension.Hauteur;
        }
        
        return aireTotale;
    }
    
    private List<DimensionAccessoire> obtenirListeAccessoire() {
        List<DimensionAccessoire> retour = new ArrayList<>();
        List<Accessoire> accessoires  = mur.getAccessoires();        
        
        for(Accessoire accessoire : accessoires) {
            if(accessoire.getType() != TypeObjet.Fenetre && accessoire.getType() != TypeObjet.Porte) continue;
            
            Point2D positionAcc = new Point2D.Double(accessoire.getPosition().getX().getTotal() + positionPanneauCentral.getX() - mur.getPosition().getX().getTotal(),
                                                     accessoire.getPosition().getY().getTotal() + positionPanneauCentral.getY());
            
            DimensionAccessoire dim = new DimensionAccessoire(positionAcc.getX(), positionAcc.getY(), accessoire.getLargeur().getTotal(), accessoire.getHauteur().getTotal());
            
            if(accessoire.getType() == TypeObjet.Fenetre) {
                double ecartMoulure = ((Fenetre) accessoire).getEcartMoulure().getTotal();
                
                dim.PositionX -= ecartMoulure;
                dim.PositionY -= ecartMoulure;
                dim.Largeur += (2 * ecartMoulure);
                dim.Hauteur += (2 * ecartMoulure);
            }
            
            retour.add(dim);
        }
        
        return retour;
    }
    
    @Override
    protected void validerDimension(List<Erreur> erreurs) {
        double anglePliSoudure = getSalle().getAnglePliSoudure();
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double differenceHauteur = largeurPliSoudure / Math.tan(Math.toRadians(anglePliSoudure));
        
        if(differenceHauteur * 2 > this.hauteur.getTotal()) {
            erreurs.add(new Erreur("L'angle de pli doit être plus élevé pour la hauteur du panneau."));
        }
    }
    
    /* Représentation des sommets d'un panneau externe
          X: 0 1  2 3        4  5   6 7
        Y 
        0           8--------10
        1      2--4-6        12-14--16
              /|                     |\
        2    0 |                     |18
             | |                     | |
        3    1 |                     |19
              \|                     |/
        4      3--5-7        13-15--17
        5           9--------11
        
        */
}
