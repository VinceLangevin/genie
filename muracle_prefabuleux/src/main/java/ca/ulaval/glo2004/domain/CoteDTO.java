package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Direction;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;


public class CoteDTO extends ListableDTO {
    public Direction Direction;
    public ArrayList<AccessoireDTO> Accessoires;
    public ArrayList<SeparateurDTO> Separateurs;
    public ArrayList<MurDTO> Murs;
    
    public Mesure Largeur;  
    public Mesure Hauteur;
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public CoteDTO(Cote cote) {
        this.Largeur = new Mesure(cote.getLargeur());
	this.Hauteur = new Mesure(cote.getHauteur());
	this.Position = new PointMesure(cote.getPosition());
        this.EstSelectionne = cote.getEtatSelection();
        
        Direction =  cote.getDirection();
        Accessoires = new ArrayList<>();
        for(Accessoire accessoire : cote.getListeAccessoire()) {
            Accessoires.add((AccessoireDTO) accessoire.getDTO());
        }
        
        Separateurs = new ArrayList<>();
        for(Separateur separateur : cote.getListeSeparateur()) {
            Separateurs.add((SeparateurDTO) separateur.getDTO());
        }
        
        
        Murs = new ArrayList<>();
        for(Mur mur : cote.getListeMur()) {
            Murs.add((MurDTO) mur.getDTO());
        }
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur, true));
        proprietes.add(new ProprieteDTO("Hauteur", Hauteur, true));
        
        return proprietes;
    }
}
