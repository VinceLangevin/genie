package ca.ulaval.glo2004.domain.util;

import java.awt.Point;


public class Echelle {
    private final double ZOOM_FACTEUR = 1.1;
    private final int PRECISION_DEPLACEMENT_ZOOM = (int) Math.pow(2, 12);
    private final int DEPLACEMENT_MAX_ZOOM = (int) Math.sqrt(PRECISION_DEPLACEMENT_ZOOM);
    
    private PointMesure translation;
    private double pixelsParPouce;
    
    public Echelle() {
        translation = new PointMesure();
        translation.translate(new Mesure(40), new Mesure(40));
        pixelsParPouce = 5;
    }
    
    public void centrer(int dimensionX, int dimensionY, Mesure largeurObjet, Mesure hauteurObjet) {
        double ratioX = (double) dimensionX / largeurObjet.getTotal();
        double ratioY = (double) dimensionY / hauteurObjet.getTotal();
        
        pixelsParPouce = Math.min(ratioX, ratioY) * 0.8;
        
        Mesure dimXPo = pixelsVersPouces(dimensionX);
        Mesure dimYPo = pixelsVersPouces(dimensionY);

        
        Mesure posX = dimXPo.soustraire(largeurObjet).multiplier(new Mesure(0, 1, 2));
        Mesure posY = dimYPo.soustraire(hauteurObjet).multiplier(new Mesure(0, 1, 2));
        translation = new PointMesure(posX, posY);
    }
    
    public PointMesure pixelsVersPouces(int x, int y, boolean translate) {
        Mesure xPo = pixelsVersPouces(x);
        Mesure yPo = pixelsVersPouces(y);
        
        if(translate) {
            xPo = xPo.soustraire(translation.getX());
            yPo = yPo.soustraire(translation.getY());
        }
        
        return new PointMesure(xPo, yPo);
    }
    
    public Mesure pixelsVersPouces(int pixels) {
        int entier = (int) (pixels / pixelsParPouce);
        int numerateur = (int) (pixels % pixelsParPouce);
        int denominateur = (int) pixelsParPouce;
        
        return new Mesure(entier, numerateur, denominateur <= 0 ? 1 : denominateur);
    }
    
    public Point poucesVersPixels(Mesure x, Mesure y, boolean translate) {  
        if(translate) {
            x = x.ajouter(translation.getX());
            y = y.ajouter(translation.getY());
        }
        
        return new Point(poucesVersPixels(x), 
                         poucesVersPixels(y));
    }
    
    public int poucesVersPixels(Mesure pouces) {
        return (int) (pouces.getTotal()  * pixelsParPouce);
    } 
    
    public Point poucesVersPixels(double x, double y, boolean translate) {
        if(translate) {
            x += translation.getX().getTotal();
            y += translation.getY().getTotal();
        }
        
        
        return new Point(poucesVersPixels(x),
                         poucesVersPixels(y));
    }
    
    public int poucesVersPixels(double pouces) {
        return (int) (pouces * pixelsParPouce);
    }
    
    public void zoom(double dx, double dy) {
        pixelsParPouce *= ZOOM_FACTEUR; 
        translation = new PointMesure(translation.getX().multiplier(new Mesure(0, 10, 11)), 
                                      translation.getY().multiplier(new Mesure(0, 10, 11)));
        
          
        Mesure deltaX = new Mesure((int)(dx * -PRECISION_DEPLACEMENT_ZOOM)).multiplier(new Mesure(0, (int) (DEPLACEMENT_MAX_ZOOM / pixelsParPouce), PRECISION_DEPLACEMENT_ZOOM));
        Mesure deltaY = new Mesure((int)(dy * -PRECISION_DEPLACEMENT_ZOOM)).multiplier(new Mesure(0, (int) (DEPLACEMENT_MAX_ZOOM / pixelsParPouce), PRECISION_DEPLACEMENT_ZOOM));
        deplacerOrigine(new PointMesure(deltaX, deltaY));
    }
    
    public void dezoom(double dx, double dy) {
        pixelsParPouce /= ZOOM_FACTEUR;
        translation = new PointMesure(translation.getX().multiplier(new Mesure(1, 1, 10)), 
                                      translation.getY().multiplier(new Mesure(1, 1, 10)));
                
        Mesure deltaX = new Mesure((int)(dx * PRECISION_DEPLACEMENT_ZOOM)).multiplier(new Mesure(0, (int) (DEPLACEMENT_MAX_ZOOM / pixelsParPouce), PRECISION_DEPLACEMENT_ZOOM));
        Mesure deltaY = new Mesure((int)(dy * PRECISION_DEPLACEMENT_ZOOM)).multiplier(new Mesure(0, (int) (DEPLACEMENT_MAX_ZOOM / pixelsParPouce), PRECISION_DEPLACEMENT_ZOOM));
        deplacerOrigine(new PointMesure(deltaX, deltaY));
    }
    
    public void deplacer(int dx, int dy) {  
        deplacerOrigine(new PointMesure(pixelsVersPouces(dx), 
                                        pixelsVersPouces(dy)));
    }
    
    private void deplacerOrigine(PointMesure difference) {
        translation.translate(difference);
    }
}
