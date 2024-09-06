package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.Afficheurs.AfficheurProfilDecoupagePanneau;
import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.TypeObjet;
import java.awt.Point;


public class PanneauVueDecoupagePanneau extends PanneauVue {
    
    public PanneauVueDecoupagePanneau() {
        
    }
    
    public PanneauVueDecoupagePanneau(Controleur controleur) {
        super(controleur, new AfficheurProfilDecoupagePanneau(controleur));
    }

    @Override
    public void ajouterObjet(TypeObjet type, Point position) { }
    
    @Override
    public void selectionner(int positionX, int positionY) { }

    @Override
    public void dragObjet(int positionX, int positionY) { }
}
