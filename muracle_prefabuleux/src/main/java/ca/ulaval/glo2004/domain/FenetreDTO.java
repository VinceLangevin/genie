package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class FenetreDTO extends AccessoireDTO {
    public Mesure Largeur;
    public Mesure Hauteur;
    public PointMesure Position;
    public Mesure EcartMoulure;
    public boolean EstSelectionne;
    
    public FenetreDTO(Fenetre fenetre) {
        super(TypeObjet.Fenetre);
        this.Largeur = new Mesure(fenetre.getLargeur());
	this.Hauteur = new Mesure(fenetre.getHauteur());
	this.Position = new PointMesure(fenetre.getPosition());
        this.EcartMoulure = new Mesure(fenetre.getEcartMoulure());
        this.EstSelectionne = fenetre.getEtatSelection();
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur));
        proprietes.add(new ProprieteDTO("Hauteur", Hauteur));
        proprietes.add(new ProprieteDTO("Position", Position));
        proprietes.add(new ProprieteDTO("EcartMoulure", "Ecart des moulures", EcartMoulure));
        
        return proprietes;
    }
    
}
