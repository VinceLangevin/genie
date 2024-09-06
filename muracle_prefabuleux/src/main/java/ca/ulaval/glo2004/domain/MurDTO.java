package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class MurDTO extends ListableDTO {
    public Mesure Largeur;
    public Mesure Hauteur;
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public PanneauDTO PanneauInterieur;
    public PanneauDTO PanneauExterieur;
    
    public MurDTO(Mur mur) {
        this.Largeur = new Mesure(mur.getLargeur());
	this.Hauteur = new Mesure(mur.getHauteur());
	this.Position = new PointMesure(mur.getPosition());
        this.EstSelectionne = mur.getEtatSelection();
        
        this.PanneauInterieur = (PanneauDTO) mur.getPanneauInterieur().getDTO();
        this.PanneauExterieur = (PanneauDTO) mur.getPanneauExterieur().getDTO();
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur, true));
        proprietes.add(new ProprieteDTO("Hauteur", Hauteur, true));
        proprietes.add(new ProprieteDTO("Position", Position, true));
        
        return proprietes;
    }
}
