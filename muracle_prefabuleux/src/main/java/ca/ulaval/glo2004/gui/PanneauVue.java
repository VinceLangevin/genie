package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.Afficheurs.Afficheur;
import ca.ulaval.glo2004.gui.util.PanneauObjetAjoutTransferHandler;
import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.ListableDTO;
import ca.ulaval.glo2004.domain.ObjetPhysique;
import javax.swing.JPanel;
import java.awt.Graphics;
import ca.ulaval.glo2004.domain.TypeObjet;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;

public abstract class PanneauVue extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    protected Afficheur afficheur;
    protected Controleur controleur;
    
    private boolean premierDraw;
    private MouseEvent clicDroitEvent = null;
    private MouseEvent clicGaucheEvent = null;
    private boolean dragClicGauche = false;
    private boolean dragClicDroit = false;
    protected ListableDTO objet ;
    protected ObjetPhysique objetPhysique;
  
    
    //Nécessaire pour pouvoir l'ajouter à la palette
    //Dans la fenêtre, il faut "Customize code" pour appeler le second constructeur sinon le panneau ne fait rien
    public PanneauVue() {
        
    }
    
    public PanneauVue(Controleur controleur, Afficheur afficheur) {
        this.controleur = controleur;
        this.afficheur = afficheur;
        
        this.setTransferHandler(new PanneauObjetAjoutTransferHandler());
        premierDraw = true;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        
        this.addComponentListener(new ResizeListener());
    }
    
    public abstract void ajouterObjet(TypeObjet type, Point position);
    public abstract void selectionner(int positionX, int positionY);
    public abstract void dragObjet(int positionX, int positionY);
    
    public void zoom(int positionX, int positionY) {       
        this.afficheur.zoom(obtenirDifferenceNormaliseCentre(positionX, getSize().width), 
                            obtenirDifferenceNormaliseCentre(positionY, getSize().height));
    }
    
    public void dezoom(int positionX, int positionY) {
        this.afficheur.dezoom(obtenirDifferenceNormaliseCentre(positionX, getSize().width), 
                              obtenirDifferenceNormaliseCentre(positionY, getSize().height));
    }
    
    
    private double obtenirDifferenceNormaliseCentre(int position, int dimension) {
        return ((((double)position * 2) / (double)dimension) - 1);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e) && clicDroitEvent == null) {
            clicGaucheEvent = e;

            selectionner(e.getX(), e.getY());            
            e.consume();
            
            repaint();
        } else if(SwingUtilities.isRightMouseButton(e) && clicGaucheEvent == null) {
            clicDroitEvent = e;
            
            this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            dragClicDroit = true;
            e.consume();
        }
        
        this.requestFocus();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(dragClicGauche) {
                MainWindow.afficherErreur(controleur.confirmerDeplacement());
                
                repaint();
            }
            
            dragClicGauche = false;
            clicGaucheEvent = null;   
        }
        
        if(SwingUtilities.isRightMouseButton(e)) {
            clicDroitEvent = null;
            dragClicDroit = false;
        }
        
        if(!dragClicDroit && !dragClicGauche) {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { 
    }

    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(clicDroitEvent != null) {
            e.consume();

            int dx = e.getX() - clicDroitEvent.getX();
            int dy = e.getY() - clicDroitEvent.getY();
            clicDroitEvent = e;
            
            this.afficheur.deplacer(dx, dy);
            
            repaint();
        }
        
        if(clicGaucheEvent != null && dragClicGauche == false) {
            int dx = e.getX() - clicGaucheEvent.getX();
            int dy = e.getY() - clicGaucheEvent.getY();
            
            if(Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                dragClicGauche = true;
                
                this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));  
            }
        }
        
        if(dragClicGauche) {
            dragObjet(e.getX(),e.getY());
            
            repaint();
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() > 0)
            dezoom(e.getX(), e.getY());
        else
            zoom(e.getX(), e.getY());
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if(afficheur != null) {
            super.paintComponent(g);
            
            if(premierDraw) {
                this.afficheur.centrer(this.getSize().width, this.getSize().height);
                premierDraw = false;
            }
            
            afficheur.draw(g);
        }
    }
    
    private void changerTailleAffichage() {
        afficheur.setTailleAffichage(this.getSize());
        
        repaint();
    }
    
    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {            
            changerTailleAffichage();
        }
    }
}
