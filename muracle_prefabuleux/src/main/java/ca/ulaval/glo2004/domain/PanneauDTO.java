package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PanneauDTO extends ListableDTO {
    public final UUID Id;
    public String Nom;
    public Mesure Largeur;
    public Mesure Hauteur;
    public PointMesure Position;
    public Area Aire;
    
    public boolean EstSelectionne;
    
    public PanneauDTO(Panneau panneau) {
        this.Largeur = new Mesure(panneau.getLargeur());
	this.Hauteur = new Mesure(panneau.getHauteur());
	this.Position = new PointMesure(panneau.getPosition());
        this.EstSelectionne = panneau.getEtatSelection();
        this.Aire = (Area) panneau.getAire().clone();  
        this.Id = panneau.getID();
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur, true));
        proprietes.add(new ProprieteDTO("Hauteur", Hauteur, true));
        proprietes.add(new ProprieteDTO("Position", Position, true));
        
        return proprietes;
    }
    
    @Override 
    public String toString() {
        return Nom;
    }
}
