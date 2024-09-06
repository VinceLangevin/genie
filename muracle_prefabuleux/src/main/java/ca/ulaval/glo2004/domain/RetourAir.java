package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.List;

public class RetourAir extends Accessoire {
    private Mesure hauteurTrouHaut;
    
    public RetourAir(Mesure largeur, Mesure hauteur, PointMesure position) {
        super(TypeObjet.RetourAir, largeur, hauteur, position);
        
    }
    
    @Override
    public ListableDTO getDTO(){
        return new RetourAirDTO(this);
    }
    
    public Mesure getHauteurTrouHaut() {
        return this.hauteurTrouHaut;
    }
            
    public void centrer() {
        Mur mur = getCote().getMur(position);
        
        if(mur != null) {
            Mesure posX = mur.getPosition().getX();
            
            if(mur.getEstCoinGauche()) {
                posX = posX.ajouter(getSalle().getEpaisseurMurs());
            }

            posX = posX.ajouter(mur.getLargeurInterieur().soustraire(this.getLargeur()).multiplier(new Mesure(0, 1, 2)));

            this.position.setX(posX);
        }
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        if(this.hauteur.compareTo(new Mesure()) <= 0){
            erreurs.add(new Erreur("La valeur de la hauteur du retour d'air ne doit pas être vide"));
        }
        if(this.largeur == null){
            erreurs.add(new Erreur("La valeur de la largeur du retour d'air ne doit pas être vide"));
        }
        if(this.position == null){
            erreurs.add(new Erreur("La valeur de la position du retour d'air ne doit pas être vide"));
        }
        
    }
    
    @Override
    public void modifier(ListableDTO DTO) {
        RetourAirDTO  retourAirDTO = (RetourAirDTO) DTO;
        this.largeur = retourAirDTO.Largeur;
        
        update();
    }
    
    @Override
    public void update() {
        this.hauteur = getSalle().getHauteurRetoursAir();
        this.hauteurTrouHaut = getSalle().getHauteurTrouHautRetoursAir();
        this.position.setY(getSalle().getHauteur().soustraire(getSalle().getDistanceSolRetoursAir()).soustraire(hauteur));
        
        centrer();
    }  
    
    @Override
    public void setParent(Cote cote) {
        super.setParent(cote);
        
        update();
    }
    
    public boolean collisionnerTrouHaut(PointMesure coord) {
        PointMesure hautGauche = new PointMesure(this.position.getX(), 
                                                 getSalle().getEpaisseurMurs().soustraire(this.hauteurTrouHaut).multiplier(new Mesure(0, 1, 2)));
        
        
        PointMesure basDroite = new PointMesure(hautGauche);
        basDroite.translate(this.largeur, this.hauteurTrouHaut);
        
        if(hautGauche.getX().compareTo(coord.getX()) > 0 || 
           hautGauche.getY().compareTo(coord.getY()) > 0 ||
           basDroite.getX().compareTo(coord.getX()) < 0 ||
           basDroite.getY().compareTo(coord.getY()) < 0) {
            
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("<RetourAir %s\\>", attrToString());
    }
}
