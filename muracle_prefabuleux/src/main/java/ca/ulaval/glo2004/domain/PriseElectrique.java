package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.List;

public class PriseElectrique extends Accessoire {
    
    public PriseElectrique(Mesure largeur, Mesure hauteur, PointMesure position) {
        super(TypeObjet.PriseElectrique, largeur, hauteur, position);
    }
    
    @Override
    public ListableDTO getDTO(){
        return new PriseElectriqueDTO(this);
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        Cote cote = this.getCote();
        if(this.hauteur == null){
            erreurs.add(new Erreur("La valeur de la hauteur du prise electrique ne doit pas être vide"));
        }
        if(this.largeur == null){
            erreurs.add(new Erreur("La valeur de la largeur du prise electrique ne doit pas être vide"));
        }
        if(this.position == null){
            erreurs.add(new Erreur("La valeur de la position du prise electrique ne doit pas être vide"));
        }
        if(this.position.getX().soustraire(getSalle().getEpaisseurMurs()).compareTo(cote.getPosition().getX())==-1||
                (this.position.getX().ajouter(getSalle().getEpaisseurMurs()).compareTo(cote.getPosition().getX().ajouter(cote.largeur)) ==1))
        {
            erreurs.add(new Erreur("La prise Électrique doit être à l'intérieur du côté"));
        }
        if(this.position.getX().ajouter(largeur).soustraire(getSalle().getEpaisseurMurs()).compareTo(cote.getPosition().getX())==-1|| 
                (this.position.getX().ajouter(largeur).compareTo(cote.getPosition().getX().ajouter(cote.largeur)) ==1))
        {
             erreurs.add(new Erreur("La prise Électrique doit être à l'intérieur du côté"));
        }
        if(this.position.getY().compareTo(cote.getPosition().getY())==-1||
                (this.position.getY().compareTo(cote.getPosition().getY().ajouter(cote.hauteur)) ==1))
        {
            erreurs.add(new Erreur("La prise Électrique doit être à l'intérieur du côté"));
        }
        if(this.position.getY().ajouter(hauteur).compareTo(cote.getPosition().getY())==-1||
                (this.position.getY().ajouter(hauteur).compareTo(cote.getPosition().getY().ajouter(cote.hauteur)) ==1))
        {
            erreurs.add(new Erreur("La priseElectrique doit être à l'intérieur du côté"));
        }
    }
    
    @Override
    public void modifier(ListableDTO DTO) {
        PriseElectriqueDTO priseDTO = (PriseElectriqueDTO) DTO;
        this.hauteur = priseDTO.Hauteur;
        this.largeur = priseDTO.Largeur;
        this.position = priseDTO.Position;
    }
    
    @Override
    public String toString() {
        return String.format("<PriseElectrique %s\\>", attrToString());
    }
}
