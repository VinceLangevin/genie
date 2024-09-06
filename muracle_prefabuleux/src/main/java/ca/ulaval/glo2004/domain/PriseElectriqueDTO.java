package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class PriseElectriqueDTO extends AccessoireDTO {
    public Mesure Largeur;
    public Mesure Hauteur;
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public PriseElectriqueDTO(PriseElectrique priseElectrique) {
        super(TypeObjet.PriseElectrique);
        
        this.Largeur = new Mesure(priseElectrique.getLargeur());
	this.Hauteur = new Mesure(priseElectrique.getHauteur());
	this.Position = new PointMesure(priseElectrique.getPosition());
        this.EstSelectionne = priseElectrique.getEtatSelection();
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur));
        proprietes.add(new ProprieteDTO("Hauteur", Hauteur));
        proprietes.add(new ProprieteDTO("Position", Position));
        
        return proprietes;
    }
}
