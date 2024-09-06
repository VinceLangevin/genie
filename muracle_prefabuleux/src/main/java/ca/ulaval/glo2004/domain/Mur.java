package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;


public class Mur extends ObjetPhysique {
    private final Cote cote;
    
    private Panneau panneauInterieur;
    private Panneau panneauExterieur;
    
    private boolean estCoinGauche;
    private boolean estCoinDroite;
    
    public Mur(Cote parent, Mesure largeur, Mesure hauteur, PointMesure position, int indexMur) {
        super(TypeObjet.Mur, largeur, hauteur, position);
        
        this.estCoinGauche = false;
        this.estCoinDroite = false;
        this.cote = parent;
        this.panneauExterieur = new PanneauExterieur(indexMur, this);
        this.panneauInterieur = new PanneauInterieur(indexMur, this);
    }
    
    
    public boolean getEstCoinGauche() {
        return this.estCoinGauche;
    }
    
    public void setEstCoinGauche(boolean estCoinGauche) {
        this.estCoinGauche = estCoinGauche;
    }
    
    public boolean getEstCoinDroite() {
        return this.estCoinDroite;
    }
    
    public void setEstCoinDroite(boolean estCoinDroite) {
        this.estCoinDroite = estCoinDroite;
    }
    
    public Panneau getPanneauInterieur() {
        return this.panneauInterieur;
    }
    
    public Panneau getPanneauExterieur() {
        return this.panneauExterieur;
    }
    
    public List<Accessoire> getAccessoires() {
        return cote.getAccessoiresMur(this);
    }
    
    public Mesure getLargeurInterieur() {
        Mesure largeurRet = this.largeur;
        
        if(estCoinDroite) {
            largeurRet = largeurRet.soustraire(getSalle().getEpaisseurMurs());
        }
        
        if(estCoinGauche) {
            largeurRet = largeurRet.soustraire(getSalle().getEpaisseurMurs());
        }
        
        return largeurRet;
    }
    
    @Override
    public boolean supprimer() {
        this.cote.supprimerMur(this);
        
        return true;
    }
    
    @Override
    public ListableDTO getDTO() {
        return new MurDTO(this);
    }
    
    protected Cote getCote() {
        return cote;
    }
    
    @Override 
    protected Salle getSalle() {
        return cote.getSalle();
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        if(this.hauteur == null){
            erreurs.add(new Erreur("La valeur de la hauteur du mur ne doit pas être vide"));
        }
        if(this.position == null){
            erreurs.add(new Erreur("La valeur de la position du mur ne doit pas être vide"));
        }
        if(this.largeur == null){
            erreurs.add(new Erreur("La valeur de la largeur du mur ne doit pas être vide"));
        }
        if(this.largeur.getTotal() < 4){
            erreurs.add(new Erreur("La largeur du mur doit être supérieur à 4 pouces"));
        }

        
        panneauExterieur.valider(erreurs);
        panneauInterieur.valider(erreurs);
    }
    
    @Override
    public void modifier(ListableDTO murDTO) {
        MurDTO dto = (MurDTO) murDTO;
        this.hauteur = dto.Hauteur;
        this.largeur = dto.Largeur;
        this.position = dto.Position;
    }
    
    @Override
    public String toString() {
        return String.format("<Mur %s\\>", attrToString());
    }
}
