package ca.ulaval.glo2004.domain.Afficheurs;

import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.MurDTO;
import ca.ulaval.glo2004.domain.util.Mesure;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;

public class AfficheurProfilDecoupagePanneau extends Afficheur {
    private final int DISTANCE_SEPARATION_PANNEAUX = 50;
    
    public AfficheurProfilDecoupagePanneau(Controleur controleur){
        super(controleur);
    }
    
    @Override
    public void centrer(int dimensionX, int dimensionY) {
        MurDTO mur = controleur.getMurSelectionne();
        
        
        echelle.centrer(dimensionX,
                        dimensionY,
                        mur.PanneauExterieur.Largeur.ajouter(mur.PanneauInterieur.Largeur).ajouter(new Mesure(DISTANCE_SEPARATION_PANNEAUX)),
                        mur.PanneauInterieur.Hauteur);
    }
    
    @Override
    public void draw(Graphics g) {
        MurDTO mur = controleur.getMurSelectionne();
        
        Mesure offsetY = controleur.getSalle().EpaisseurPliSoudure.ajouter(controleur.getSalle().EpaisseurMurs);
        Mesure offsetX = mur.Largeur.ajouter(controleur.getSalle().EpaisseurPliSoudure.multiplier(new Mesure(2)).ajouter(controleur.getSalle().EpaisseurMurs.multiplier(new Mesure(2))));
        
        AffineTransform deplacement = new AffineTransform();
        deplacement.setToTranslation(offsetX.ajouter(new Mesure(DISTANCE_SEPARATION_PANNEAUX)).getTotal(), -offsetY.getTotal());
        
        drawArea(g, mur.PanneauExterieur.Aire);
        
        Area airePanneauInterieurDeplacee = new Area(mur.PanneauInterieur.Aire);
        airePanneauInterieurDeplacee.transform(deplacement);
        drawArea(g, airePanneauInterieurDeplacee);
        
        drawGrille(g);
    }
   
    private void drawArea(Graphics g, Area area) {
        PathIterator itr = area.getPathIterator(null);
        
        double[] coords = new double[6];
        Point srcCoords = new Point();
        Point startCoords = new Point();
        while(!itr.isDone()) {
            int retour = itr.currentSegment(coords);
            
            switch(retour) {
                case PathIterator.SEG_MOVETO:
                    startCoords = echelle.poucesVersPixels(coords[0], coords[1], true);
                    srcCoords = startCoords;
                    break;
                case PathIterator.SEG_LINETO:
                    Point destCoords = echelle.poucesVersPixels(coords[0], coords[1], true);
                    
                    g.drawLine(srcCoords.x, srcCoords.y, destCoords.x, destCoords.y);
                    
                    srcCoords = destCoords;
                    break;
                case PathIterator.SEG_CLOSE:
                    srcCoords = echelle.poucesVersPixels(coords[0], coords[1], true);
                    
                    g.drawLine(srcCoords.x, srcCoords.y, startCoords.x, startCoords.y);
                    break;
            }
            
            itr.next();
        }
    }
}
