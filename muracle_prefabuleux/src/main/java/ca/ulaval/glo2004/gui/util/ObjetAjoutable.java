package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.TypeObjet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class ObjetAjoutable extends JComponent implements MouseListener, MouseMotionListener {
    private final int PADDING = 4;
    
    private Image image;
    private TypeObjet type;
    private String texte;
    private MouseEvent firstMouseEvent = null;
    private boolean premierAffichage = true;
    
    public ObjetAjoutable(Image image, TypeObjet type, String texte) {
        this(image, type, texte, new Font("Segoe UI", Font.BOLD, 12));
    }
    
    public ObjetAjoutable(Image image, TypeObjet type, String texte, Font font) {
        this.image = image;
        this.type = type;
        this.texte = texte;

        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.setFont(font);
        
        this.addComponentListener(new ResizeListener());           
        this.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        
        resizeImage();
    }    
    
    public TypeObjet getType() {
        return type;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            firstMouseEvent = e;
            e.consume();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        firstMouseEvent = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (firstMouseEvent != null) {
            e.consume();

            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
            
            if (dx > 5 || dy > 5) {
                JComponent component = (JComponent) e.getSource();
                TransferHandler handler = component.getTransferHandler();

                handler.exportAsDrag(component, firstMouseEvent, TransferHandler.COPY);
                firstMouseEvent = null;
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics g = graphics.create();
        
        if(premierAffichage) {
            resizeImage();
            premierAffichage = false;
        }
        
        Dimension dimension = this.getSize();
        
        if (image != null) {
            int fontHeight = g.getFontMetrics(this.getFont()).getHeight();
            
            g.drawImage(image, 
                        ((dimension.width - image.getWidth(this)) / 2), 
                        ((dimension.height - image.getHeight(this) - fontHeight ) / 2), 
                        this);
        }
        
        paintTexte(g);
        
        g.dispose();
    }
    
    private void paintTexte(Graphics g) {
        FontMetrics metrics = g.getFontMetrics(this.getFont());
        g.setColor(Color.BLACK);
        
        Dimension dimension = this.getSize();
        int x = (dimension.width - metrics.stringWidth(texte)) / 2;
        int y = dimension.height - PADDING;
        
        g.drawString(texte, x, y);
    }
       
    private void resizeImage() {
        Dimension dimension = this.getSize();
                
        if(image != null && dimension.width > 0 && dimension.height > 0) {    
            int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
            
            double imageWidth = this.image.getWidth(this);
            double imageHeight = this.image.getHeight(this);
            
            double maxScalingFactor = Math.min((dimension.width  - 2 * PADDING) / (imageWidth), (dimension.height - fontHeight  - 3 * PADDING) / (imageHeight));
            
            this.image = image.getScaledInstance((int)(imageWidth * maxScalingFactor), 
                                                 (int)(imageHeight * maxScalingFactor), 
                                                 Image.SCALE_SMOOTH);
        }
    }
    
    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {            
            resizeImage();
        }
    }
}
