package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.Afficheurs.AfficheurPlanSalle;
import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.TypeObjet;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.Point;

public class PanneauVuePlanSalle extends PanneauVue {
    
    //Voir l'explication dans PanneauVue
    public PanneauVuePlanSalle() {
        
    }
    
    public PanneauVuePlanSalle(Controleur controleur) {
        super(controleur, new AfficheurPlanSalle(controleur));
    }
    

    @Override
    public void ajouterObjet(TypeObjet type, Point position) {
        PointMesure pointSelection = afficheur.obtenirPositionPouces(position.x, position.y);
        
        MainWindow.afficherErreur(controleur.ajouterObjetSalle(type, pointSelection));
        
        repaint();
    }
    
    @Override
    public void selectionner(int positionX, int positionY) {
        PointMesure pointSelection = afficheur.obtenirPositionPouces(positionX, positionY);
        
        controleur.selectionnerSalle(pointSelection);
        
        repaint();
    }

    @Override
    public void dragObjet(int positionX, int positionY) {
        PointMesure pointDrag = afficheur.obtenirPositionPouces(positionX, positionY);
        
        controleur.deplacerObjetSalle(pointDrag);
    }
 }
    
    

