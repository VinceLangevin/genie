package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.List;

public class SalleDTO extends ListableDTO {
    public ArrayList<CoteDTO> Cotes;
    
    public double DensiteMatierePremiere;
    public Mesure EpaisseurPliSoudure;
    public Mesure EpaisseurMateriaux;
    public Mesure EpaisseurMurs;
    
    public Mesure HauteurRetoursAir;
    public Mesure HauteurTrouHautRetourAir;
    public Mesure DistanceSolRetourAir;
    
    public double MasseMaximalePanneau;
    public double AngleDegreePliSoudure;  
    public Mesure MargePliage;
    public Mesure Largeur;  
    public Mesure Longueur;
    public Mesure Hauteur;
    public PointMesure Position;
    public boolean EstSelectionne;
    
    public SalleDTO(Salle salle){
        this.Largeur = new Mesure(salle.getLargeur());
        this.Longueur = new Mesure(salle.getLongueur());
	this.Hauteur = new Mesure(salle.getHauteur());
	this.Position = new PointMesure(salle.getPosition());
        this.EstSelectionne = salle.getEtatSelection();
        
        this.EpaisseurMurs = new Mesure(salle.getEpaisseurMurs());
        this.EpaisseurMateriaux = new Mesure(salle.getEpaisseurMateriaux());
        
        this.HauteurRetoursAir = new Mesure(salle.getHauteurRetoursAir());
        this.HauteurTrouHautRetourAir = new Mesure(salle.getHauteurTrouHautRetoursAir());
        this.DistanceSolRetourAir = new Mesure(salle.getDistanceSolRetoursAir());
        
        this.MasseMaximalePanneau = salle.getMasseMaximalePanneau();
        this.DensiteMatierePremiere = salle.getDensiteMatierePremiere();
        this.EpaisseurPliSoudure = new Mesure(salle.getEpaisseurPliSoudure());
        this.AngleDegreePliSoudure = salle.getAnglePliSoudure();
        this.MargePliage = new Mesure(salle.getMargePliage());
        
        this.Cotes = new ArrayList<>();
        for(Cote cote : salle.getListeCote().values()) {
            this.Cotes.add((CoteDTO) cote.getDTO());
        }
    }
    
    @Override
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {
        List<ProprieteDTO> proprietes = new ArrayList<>();
        proprietes.add(new ProprieteDTO("Largeur", Largeur));
        proprietes.add(new ProprieteDTO("Longueur", Longueur));
        proprietes.add(new ProprieteDTO("Hauteur", Hauteur));
        
        proprietes.add(new ProprieteDTO("EpaisseurMurs", "Epaisseur des murs", EpaisseurMurs));
        
        proprietes.add(new ProprieteDTO("MargePliage", "Marge de pliage", MargePliage));
        proprietes.add(new ProprieteDTO("EpaisseurMateriaux", "Epaisseur des matériaux", EpaisseurMateriaux));
        proprietes.add(new ProprieteDTO("EpaisseurPliSoudure", "Epaisseur du pli de soudure", EpaisseurPliSoudure));
        proprietes.add(new ProprieteDTO("AngleDegreePliSoudure", "Angle du pli de soudure", AngleDegreePliSoudure));
        
        proprietes.add(new ProprieteDTO("HauteurRetoursAir", "Hauteur vue de côté des retours d'air", HauteurRetoursAir));
        proprietes.add(new ProprieteDTO("HauteurTrouHautRetourAir", "Hauteur vue de haut des retours d'air", HauteurTrouHautRetourAir));
        proprietes.add(new ProprieteDTO("DistanceSolRetourAir", "Distance du sol des retours d'air", DistanceSolRetourAir));
        
        proprietes.add(new ProprieteDTO("MasseMaximalePanneau", "Masse maximale des panneaux", MasseMaximalePanneau));        
        proprietes.add(new ProprieteDTO("DensiteMatierePremiere", "Densité de la matière première", DensiteMatierePremiere));
       
        return proprietes;
    }
}



