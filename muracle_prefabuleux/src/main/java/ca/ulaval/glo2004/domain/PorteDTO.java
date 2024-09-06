package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class PorteDTO extends AccessoireDTO {
    public Mesure Largeur;  
    public Mesure Hauteur;
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public PorteDTO(Porte porte) {
        super(TypeObjet.Porte);
        
        this.Largeur = new Mesure(porte.getLargeur());
	this.Hauteur = new Mesure(porte.getHauteur());
	this.Position = new PointMesure(porte.getPosition());
        this.EstSelectionne = porte.getEtatSelection();
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
