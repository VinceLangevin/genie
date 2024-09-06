 package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class RetourAirDTO extends AccessoireDTO {
    public Mesure Largeur;
    public Mesure Hauteur;
    public Mesure HauteurTrouHaut;
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public RetourAirDTO(RetourAir retourAir){
        super(TypeObjet.RetourAir);
        
        this.Largeur = new Mesure(retourAir.getLargeur());
	this.Hauteur = new Mesure(retourAir.getHauteur());
        this.HauteurTrouHaut = new Mesure(retourAir.getHauteurTrouHaut());
	this.Position = new PointMesure(retourAir.getPosition());
        this.EstSelectionne = retourAir.getEtatSelection();
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur));
        proprietes.add(new ProprieteDTO("Hauteur","Hauteur vue de côté", Hauteur, true));
        proprietes.add(new ProprieteDTO("HauteurTrouHaut", "Hauteur vue du haut", HauteurTrouHaut, true));
        proprietes.add(new ProprieteDTO("Position", Position, true));
        
        return proprietes;
    }
}
