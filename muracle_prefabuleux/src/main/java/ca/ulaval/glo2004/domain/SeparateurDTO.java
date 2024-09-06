package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class SeparateurDTO extends ListableDTO {
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public SeparateurDTO(Separateur separateur){
	this.Position = new PointMesure(separateur.getPosition());
        this.EstSelectionne = separateur.getEtatSelection();
    }   
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Position", Position));
        
        return proprietes;
    }
}
