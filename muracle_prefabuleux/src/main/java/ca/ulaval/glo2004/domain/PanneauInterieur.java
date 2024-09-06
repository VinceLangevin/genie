package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class PanneauInterieur extends Panneau {
    
    public PanneauInterieur(int indexMur, Mur mur) {
        super(indexMur, mur);       
    }
    
    @Override
    protected boolean estExterieur() {
        return false;
    }
    
    @Override
    protected void calculerAire() { 
        this.aire = new Area();
        this.largeur = mur.getLargeurInterieur(); 
        
        double anglePliSoudure = getSalle().getAnglePliSoudure();
        
        double margePliage = getSalle().getMargePliage().getTotal();
        double epaisseurMur = getSalle().getEpaisseurMurs().getTotal();
        double epaisseurMat = getSalle().getEpaisseurMateriaux().getTotal();
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double differenceHauteur = largeurPliSoudure / Math.tan(Math.toRadians(anglePliSoudure));
        
        double largeurEpaisseur = largeur.getTotal() - (2 * epaisseurMat);
        
        double positionXGaucheJointEpaisseurSoudure = epaisseurMat;
        double positionXDroiteJointEpaisseurSoudure = largeur.getTotal() - epaisseurMat;
        if(mur.getEstCoinGauche()) {
            positionXGaucheJointEpaisseurSoudure -= epaisseurMur;
        }
        
        if(mur.getEstCoinDroite()) {
            positionXDroiteJointEpaisseurSoudure += epaisseurMur;
        }
        
        double positionY = 0;
        
        Path2D pliSoudureHaut = new Path2D.Double();
        pliSoudureHaut.moveTo(positionXGaucheJointEpaisseurSoudure + differenceHauteur, positionY);
        pliSoudureHaut.lineTo(positionXDroiteJointEpaisseurSoudure - differenceHauteur, positionY);
        
        positionY += largeurPliSoudure;
        pliSoudureHaut.lineTo(positionXDroiteJointEpaisseurSoudure, positionY);
        pliSoudureHaut.lineTo(positionXGaucheJointEpaisseurSoudure, positionY);
        pliSoudureHaut.closePath();
        aire.add(new Area(pliSoudureHaut));
        
        Path2D pliEpaisseurHaut = new Path2D.Double();        
        pliEpaisseurHaut.moveTo(positionXGaucheJointEpaisseurSoudure, positionY);
        pliEpaisseurHaut.lineTo(positionXDroiteJointEpaisseurSoudure, positionY);
        
        positionY += epaisseurMur;
        pliEpaisseurHaut.lineTo(largeur.getTotal() - epaisseurMat, positionY);
        pliEpaisseurHaut.lineTo(epaisseurMat, positionY);
        pliEpaisseurHaut.closePath();
        aire.add(new Area(pliEpaisseurHaut));
        
        Rectangle2D pliageHaut = new Rectangle2D.Double(epaisseurMat, positionY, largeurEpaisseur, margePliage);
        aire.add(new Area(pliageHaut));
        
        positionY += margePliage;
        positionPanneauCentral = new Point2D.Double(0, positionY);
        Rectangle2D surfacePanneau = new Rectangle2D.Double(0, positionY, largeur.getTotal(), hauteur.getTotal());
        aire.add(new Area(surfacePanneau));
        
        positionY += hauteur.getTotal();
        Rectangle2D pliageBas = new Rectangle2D.Double(epaisseurMat, positionY, largeurEpaisseur, margePliage);
        aire.add(new Area(pliageBas));
        
        positionY += margePliage;
        Path2D pliEpaisseurBas = new Path2D.Double();      
        pliEpaisseurBas.moveTo(epaisseurMat, positionY);
        pliEpaisseurBas.lineTo(largeur.getTotal() - epaisseurMat, positionY);
        
        positionY += epaisseurMur;
        pliEpaisseurBas.lineTo(positionXDroiteJointEpaisseurSoudure, positionY);
        pliEpaisseurBas.lineTo(positionXGaucheJointEpaisseurSoudure, positionY);
        pliEpaisseurBas.closePath();
        aire.add(new Area(pliEpaisseurBas));
        
        Path2D pliSoudureBas = new Path2D.Double();
        pliSoudureBas.moveTo(positionXGaucheJointEpaisseurSoudure, positionY);
        pliSoudureBas.lineTo(positionXDroiteJointEpaisseurSoudure, positionY);
        
        positionY += largeurPliSoudure;
        pliSoudureBas.lineTo(positionXDroiteJointEpaisseurSoudure - differenceHauteur, positionY);
        pliSoudureBas.lineTo(positionXGaucheJointEpaisseurSoudure + differenceHauteur, positionY);
        pliSoudureBas.closePath();
        aire.add(new Area(pliSoudureBas));
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
    
    private List<DimensionAccessoire> obtenirListeAccessoire() {
        List<DimensionAccessoire> retour = new ArrayList<>();
        List<Accessoire> accessoires  = mur.getAccessoires();      
        
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double epaisseurMur = getSalle().getEpaisseurMurs().getTotal();
        
        for(Accessoire accessoire : accessoires) {            
            Point2D positionAcc = new Point2D.Double(accessoire.getPosition().getX().getTotal() + positionPanneauCentral.getX() - mur.getPosition().getX().getTotal() - (mur.getEstCoinGauche() ? epaisseurMur : 0),
                                                     accessoire.getPosition().getY().getTotal() + positionPanneauCentral.getY());
            
            DimensionAccessoire dim = new DimensionAccessoire(positionAcc.getX(), positionAcc.getY(), accessoire.getLargeur().getTotal(), accessoire.getHauteur().getTotal());
            
            switch(accessoire.getType()) { 
                case Porte:
                    double margePliage = getSalle().getMargePliage().getTotal();
                    
                    dim.Hauteur += margePliage + epaisseurMur + largeurPliSoudure;
                    break;
                    
                case Fenetre:
                    double ecartMoulure = ((Fenetre) accessoire).getEcartMoulure().getTotal();
                    
                    dim.PositionX -= ecartMoulure;
                    dim.PositionY -= ecartMoulure;
                    dim.Largeur += 2 * ecartMoulure;
                    dim.Hauteur += 2 * ecartMoulure;
                    break;
                    
                case RetourAir:
                    RetourAir retourAir = (RetourAir) accessoire;
                    double hauteurHaut = retourAir.getHauteurTrouHaut().getTotal();
                    
                    DimensionAccessoire dimTrouHaut = new DimensionAccessoire(positionAcc.getX(),
                                                                              largeurPliSoudure + ((epaisseurMur - hauteurHaut) / 2), 
                                                                              accessoire.getLargeur().getTotal(), 
                                                                              hauteurHaut);
                    retour.add(dimTrouHaut);
            }
            
            retour.add(dim);
        }                   
        
        return retour;
    }
    
    @Override
    protected double calculAireTotalePouces(){
        double aireTotale = 0;
        
        double anglePliSoudure = getSalle().getAnglePliSoudure();
        
        double margePliage = getSalle().getMargePliage().getTotal();
        double epaisseurMur = getSalle().getEpaisseurMurs().getTotal();
        double epaisseurMat = getSalle().getEpaisseurMateriaux().getTotal();
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double differenceHauteur = largeurPliSoudure / Math.tan(Math.toRadians(anglePliSoudure));
        
        double largeurEpaisseur = largeur.getTotal() - (2 * epaisseurMat);
        double hauteurPliSoudure = (largeurEpaisseur - (2 * differenceHauteur));
        
        //Panneau principal
        aireTotale += this.largeur.getTotal() * this.hauteur.getTotal();
        
        //2*Marge pliage
        aireTotale += (margePliage * largeurEpaisseur) * 2;
        
        //PlisEpaisseurs
        aireTotale += epaisseurMur * largeurEpaisseur * 2;
        
        if(mur.getEstCoinGauche()) {
            aireTotale += epaisseurMur * 2; //Le triangle est la moitié d'un carré de longueur "épaisseur mur"
            hauteurPliSoudure += epaisseurMur; 
        }
        
        if(mur.getEstCoinDroite()) {
            aireTotale += epaisseurMur * 2; //Le triangle est la moitié d'un carré de longueur "épaisseur mur"
            hauteurPliSoudure += epaisseurMur; 
        }
        
        //2*Pli soudure        
        aireTotale += hauteurPliSoudure * largeurPliSoudure * 2; //Rectangle
        aireTotale += (largeurPliSoudure * differenceHauteur) * 2; //4 triangles (donc 2 fois l'aire d'un rectangle)
        
        List<DimensionAccessoire> accessoires = obtenirListeAccessoire(); 
        for(DimensionAccessoire dimension : accessoires) {
            aireTotale -= dimension.Largeur * dimension.Hauteur;
        }
        
        return aireTotale;
    }
    
        /* Représentation des sommets d'un panneau interne
          X:0 1 2      3 4 5    X:0123          4567
        Y                      Y
        0       1------0       0    1------------0
               /        \          /              \
        1     3----------2     1  3----------------2
              |          |         \              /
              |          |          \            /
        2     5----------4     2     5----------4
              |          |           |          |
        3   9-7----------6-8   3   9-7----------6-8 
            |              |       |              |
            |              |       |              |
            |              |       |              |
            |              |       |              |
        4  11-13--------12-10  4  11-13--------12-10
              |          |           |          |
        5     15--------14     5     15--------14
              |          |          /            \
              |          |         /              \
        6     17--------16     6  17--------------16
               \        /          \              /
        7       19----18       7    19----------18
        */

    @Override
    protected void validerDimension(List<Erreur> erreurs) {
        double anglePliSoudure = getSalle().getAnglePliSoudure();
        double largeurPliSoudure = getSalle().getEpaisseurPliSoudure().getTotal();
        double differenceHauteur = largeurPliSoudure / Math.tan(Math.toRadians(anglePliSoudure));
        
        if(differenceHauteur * 2 > this.largeur.getTotal()) {
            erreurs.add(new Erreur("L'angle de pli doit être plus élevé pour la largeur du panneau."));
        }
    }
}
