package ca.ulaval.glo2004.domain.util;

import ca.ulaval.glo2004.domain.Projet;
import java.util.Stack;

public final class GestionnaireEtatProjet {
    private final Stack<String> etatsUndo;    
    private final Stack<String> etatsRedo;
    private Projet projet;
    
    public GestionnaireEtatProjet(Projet projet) 
    {
        this.projet = projet;
        this.etatsUndo = new Stack<>();
        this.etatsRedo = new Stack<>();
        enregistrerEtat();
    }
    
    public void setProjet(Projet projet) {
        this.projet = projet;
        
        this.etatsUndo.clear();
        this.etatsRedo.clear();
        enregistrerEtat();
    }
  
    public void enregistrerEtat()
    {
        etatsUndo.push(projet.obtenirEtat());
        etatsRedo.clear();
    }
    
    public void rechargerDerniereEtat() {
        if(!etatsUndo.empty()) {
            projet.chargerEtat(etatsUndo.peek());
        }
    }
    
    public void annuler()
    {
        if(etatsUndo.size() > 1)
        {
            etatsRedo.push(etatsUndo.pop());
            
            projet.chargerEtat(etatsUndo.peek());
        }
    }
    
    public void refaire()
    {
        if(!etatsRedo.empty())
        {
            etatsUndo.push(etatsRedo.peek());
            
            projet.chargerEtat(etatsRedo.pop());
        }
    }
    
    @Override
    public String toString() {
        return String.format("<GestionnaireEtat nbUndo:\"%d\" nbRedo:\"%d\"\\>", etatsUndo.size(), etatsRedo.size());
    }
    
}