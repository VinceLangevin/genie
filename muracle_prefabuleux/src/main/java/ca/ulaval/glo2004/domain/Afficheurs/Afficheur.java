package ca.ulaval.glo2004.domain.Afficheurs;

import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.util.Echelle;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public abstract class Afficheur {
    private final Color COULEUR_SELECTION = new Color(230, 184, 0,255);
    private final Color COULEUR_PAR_DEFAUT = Color.black;
    private final Color COULEUR_SELECTION_MUR = new Color(255,255,0,25);
    private final Color COULEUR_GRILLE = new Color(0, 0, 0, 50);
    
    protected final Controleur controleur;
    protected final Echelle echelle;
    
    private Dimension tailleAffichage;
        
    public Afficheur(Controleur controleur) {
        this.controleur = controleur;
        this.echelle = new Echelle();
    }
    
    public abstract void draw(Graphics g);
    public abstract void centrer(int dimensionX, int dimensionY);
    
    public PointMesure obtenirPositionPouces(int x, int y) {
        return echelle.pixelsVersPouces(x, y, true);
    }
    
    public void selectionner(int x, int y) {
        PointMesure click = echelle.pixelsVersPouces(x, y, true);
        
        System.out.println(String.format("X = %d %d/%d", click.getX().getEntier(), click.getX().getNumerateur(), click.getX().getDenominateur()));
        System.out.println(String.format("Y = %d %d/%d", click.getY().getEntier(), click.getY().getNumerateur(), click.getY().getDenominateur()));
    }
    
    public void deplacer(int dx, int dy) {
        echelle.deplacer(dx, dy);
    }
    
    public void zoom(double dx, double dy) {
        echelle.zoom(dx, dy);
    }
    
    public void dezoom(double dx, double dy) {       
        echelle.dezoom(dx, dy);
    }
    
    public void setTailleAffichage(Dimension tailleAffichage) {
        this.tailleAffichage = tailleAffichage;
    }

    protected void drawRectangle(Graphics g, Mesure origineX, Mesure origineY, Mesure largeur, Mesure hauteur) {
        Point hautGauchePixel = echelle.poucesVersPixels(origineX, origineY, true);
        Point basDroitePixel = echelle.poucesVersPixels(largeur, hauteur, false);
        
        g.drawRect(hautGauchePixel.x, 
                   hautGauchePixel.y, 
                   basDroitePixel.x, 
                   basDroitePixel.y);
    }
    
    protected void drawRectangle(Graphics g, Mesure origineX, Mesure origineY, Mesure largeur, Mesure hauteur, boolean estSelectionne) {
        if(estSelectionne) g.setColor(COULEUR_SELECTION);
        
        drawRectangle(g, origineX, origineY, largeur, hauteur);
        
        if(estSelectionne) g.setColor(COULEUR_PAR_DEFAUT);
    }
    
    protected void drawMur(Graphics g, Mesure origineX, Mesure origineY, Mesure largeur, Mesure hauteur, boolean estSelectionne)
    {
        if(estSelectionne) {
            g.setColor(COULEUR_SELECTION_MUR);
            Point hautGauchePixel = echelle.poucesVersPixels(origineX, origineY, true);
            Point basDroitePixel = echelle.poucesVersPixels(largeur, hauteur, false);
        
            g.fillRect(hautGauchePixel.x, 
                       hautGauchePixel.y, 
                       basDroitePixel.x, 
                       basDroitePixel.y);
        }
    }
    
    protected void drawLigne(Graphics g, Mesure origineX, Mesure origineY, Mesure destinationX, Mesure destinationY) {
        Point originePixel = echelle.poucesVersPixels(origineX, origineY, true);
        Point destinationPixel = echelle.poucesVersPixels(destinationX, destinationY, true);
        
        g.drawLine(originePixel.x, originePixel.y, destinationPixel.x, destinationPixel.y);
    }
    
    protected void drawLigne(Graphics g, Mesure origineX, Mesure origineY, Mesure destinationX, Mesure destinationY, boolean estSelectionne) {
        if(estSelectionne) g.setColor(COULEUR_SELECTION);
        
        drawLigne(g, origineX, origineY, destinationX, destinationY);
        
        if(estSelectionne) g.setColor(COULEUR_PAR_DEFAUT);
    }
    
    protected void drawLigne(Graphics g, PointMesure origine, PointMesure destination) {
        drawLigne(g, origine.getX(), origine.getY(), destination.getX(), destination.getY());
    }
    

    protected void drawLigne(Graphics g, PointMesure origine, PointMesure destination, boolean estSelectionne) {
        if(estSelectionne) g.setColor(COULEUR_SELECTION);
        
        drawLigne(g, origine, destination);
        
        if(estSelectionne) g.setColor(COULEUR_PAR_DEFAUT);
    }    
    
    protected void drawLignePointille(Graphics g, PointMesure origine, PointMesure destination) {
        Point originePixel = echelle.poucesVersPixels(origine.getX(), origine.getY(), true);
        Point destinationPixel = echelle.poucesVersPixels(destination.getX(), destination.getY(), true);
        
        Graphics2D g2d = (Graphics2D) g.create();

        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                        0, new float[]{6}, 0);
        g2d.setStroke(dashed);

        g2d.drawLine(originePixel.x, originePixel.y, destinationPixel.x, destinationPixel.y);

        g2d.dispose();
    }

    protected void drawGrille(Graphics g){
        if(controleur.getAfficherGrille()) {
            Point origine = echelle.poucesVersPixels(0, 0, false);
            
            int distance = echelle.poucesVersPixels(controleur.getDistanceGrille());
            int largeur = tailleAffichage.width; 
            int hauteur = tailleAffichage.height;

            g.setColor(COULEUR_GRILLE);
        
        
            for (int i = 0; i < hauteur; i += distance){
                g.drawLine(origine.x, origine.y + i, largeur, origine.y + i);
            }
            for (int n = 0; n < largeur; n += distance){
                g.drawLine(origine.x + n, origine.y, origine.x + n, hauteur);
            }
            
            g.setColor(COULEUR_PAR_DEFAUT);
        }
        
    }
}
 