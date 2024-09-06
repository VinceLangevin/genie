package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.Afficheurs.AfficheurElevationCote;
import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.TypeObjet;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.Point;

public class PanneauVueElevationCote extends PanneauVue {
    
    //Voir l'explication dans PanneauVue
    public PanneauVueElevationCote() {
        
    }
    
    public PanneauVueElevationCote(Controleur controleur) {
        super(controleur, new AfficheurElevationCote(controleur));
    }

    @Override
    public void ajouterObjet(TypeObjet type, Point position) {
        PointMesure pointSelection = afficheur.obtenirPositionPouces(position.x, position.y);
        
        MainWindow.afficherErreur(controleur.ajouterObjetCote(type, pointSelection));
        
        repaint();
    }
    
    @Override
    public void selectionner(int positionX, int positionY) {
        PointMesure pointSelection = afficheur.obtenirPositionPouces(positionX, positionY);
        
        controleur.selectionnerCote(pointSelection);
        
        repaint();
    }

    @Override
    public void dragObjet(int positionX, int positionY) {
        PointMesure pointDrag = afficheur.obtenirPositionPouces(positionX, positionY);
        
        controleur.deplacerObjetCote(pointDrag);
    }
}
