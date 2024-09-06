package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.List;


public class Porte extends Accessoire {
    
    public Porte(Mesure largeur, Mesure hauteur, PointMesure position) {
        super(TypeObjet.Porte, largeur, hauteur, position);
    }
    
    @Override
    public ListableDTO getDTO() {
        return new PorteDTO(this);
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        Cote cote = this.getCote();
        if(this.hauteur == null){
            erreurs.add(new Erreur("La porte de la hauteur de la porte ne doit pas être vide"));
        }
        if(this.largeur == null){
            erreurs.add(new Erreur("La porte de la largeur de la porte ne doit pas être vide"));
        }
        if(this.position == null){
            erreurs.add(new Erreur("La porte de la position de la porte ne doit pas être vide"));
        }
        if(this.position.getX().soustraire(getSalle().getEpaisseurMurs()).compareTo(cote.getPosition().getX().ajouter(getSalle().getEpaisseurMateriaux() ))==-1||
                (this.position.getX().ajouter(getSalle().getEpaisseurMurs()).compareTo(cote.getPosition().getX().ajouter(cote.largeur)
                .ajouter(getSalle().getEpaisseurMateriaux())) ==1))
        {
            erreurs.add(new Erreur("La porte doit être à l'intérieur du côté"));
        }
        if(this.position.getX().ajouter(largeur).soustraire(getSalle().getEpaisseurMurs()).compareTo(cote.getPosition().getX())==-1|| 
                (this.position.getX().ajouter(largeur).compareTo(cote.getPosition().getX().ajouter(cote.largeur)) ==1))
        {
             erreurs.add(new Erreur("La porte doit être à l'intérieur du côté"));
        }
    }
    
    @Override
    public void modifier(ListableDTO DTO) {
        PorteDTO porteDTO = (PorteDTO) DTO;
        this.hauteur = porteDTO.Hauteur;
        this.largeur = porteDTO.Largeur;
        this.position = porteDTO.Position;
        
        position.setY(getSalle().getHauteur().soustraire(hauteur));
    }
    
    
        @Override 
    public void setPosition(PointMesure position)
    {
   
        
        if(position.getX().compareTo(this.position.getX()) != 0) {
            position.setY(new Mesure());
            this.position = position;   
            position.setY(getSalle().getHauteur().soustraire(hauteur));
        }
        
    }
    
    @Override
    public String toString() {
        return String.format("<Porte %s\\>", attrToString());
    }
    
    @Override 
    public void update(){
        position.setY(getSalle().getHauteur().soustraire(hauteur));
    }
    
}
