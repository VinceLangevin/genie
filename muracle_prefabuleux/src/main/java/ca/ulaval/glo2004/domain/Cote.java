package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Direction;
import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cote extends ObjetPhysique {
    private Salle salle;
    
    private Direction direction;
    private List<Accessoire> accessoires;
    private List<Separateur> separateurs;
    private List<Mur> murs;
    
    public Cote(Salle salle, Mesure largeur, Mesure hauteur, Direction direction) {
        super(TypeObjet.Cote, largeur, hauteur, new PointMesure());
        
        this.direction = direction;
        this.salle = salle;
        
        this.accessoires = new ArrayList<>();
        this.separateurs = new ArrayList<>();
        this.murs = new ArrayList<>();
        
        genererMurs();
    }
    
    public List<Separateur> getListeSeparateur() {
        return this.separateurs;
    }
    
    public List<Mur> getListeMur() {
        return this.murs;
    }
    
    public List<Accessoire> getListeAccessoire() {
        return this.accessoires;
    }
    
    public Direction getDirection(){
        return this.direction;
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {
        if(this.hauteur.getTotal() < 0){
            erreurs.add(new Erreur("La hauteur ne peut pas être négative."));
        }
        if(this.largeur.getTotal() < 0){
            erreurs.add(new Erreur("La largeur doit exister"));
        }
        if(this.accessoires == null){
            erreurs.add(new Erreur("La liste d'accessoires doit exister"));
        }
        if(this.murs == null){
            erreurs.add(new Erreur("Les murs doivent exister"));
        }
        if(this.salle == null){
            erreurs.add(new Erreur("La salle doit exister"));
        }
        
        for(Separateur separateur : separateurs) {
            separateur.valider(erreurs);
            
            if(!getBornesInterieurs().contains( separateur.getPosition().getX().getTotal(),hauteur.multiplier(new Mesure (0,1,2)).getTotal())) {
                erreurs.add(new Erreur("Un séparateur doit se retrouver à l'intérieur du côté."));
            }
        }
        
        for(Mur mur : murs) {
            mur.valider(erreurs);
        }
        
        for(Accessoire accessoire : accessoires) {
            accessoire.valider(erreurs);
            
            if(!interieurContient(accessoire)) {
                erreurs.add(new Erreur("Un accessoire doit se retrouver à l'intérieur du côté"));
            }
        }
        
        validerCollision(erreurs);
    }
    
    private Rectangle2D getBornesInterieurs() {
        Mesure epaisseur = getSalle().getEpaisseurMurs().ajouter(new Mesure(0,1,2)).ajouter(getSalle().getEpaisseurPliSoudure());
        
        Rectangle2D bornes = new Rectangle2D.Double(epaisseur.getTotal(),
                                                    getSalle().getEpaisseurPliSoudure().getTotal(),
                                                    largeur.soustraire(epaisseur.multiplier(new Mesure(2))).getTotal(),
                                                    hauteur.soustraire(getSalle().getEpaisseurPliSoudure().multiplier(new Mesure(2))).getTotal());
        
        return bornes;
    }
     private Rectangle2D getBornesInterieursPorte() {
        Mesure epaisseur = getSalle().getEpaisseurMurs().ajouter(new Mesure(0,1,2)).ajouter(getSalle().getEpaisseurPliSoudure());
        
        Rectangle2D bornes = new Rectangle2D.Double(epaisseur.getTotal(),
                                                    getSalle().getEpaisseurPliSoudure().getTotal(),
                                                    largeur.soustraire(epaisseur.multiplier(new Mesure(2))).getTotal(),
                                                    hauteur.soustraire(getSalle().getEpaisseurPliSoudure()).getTotal());
        
        return bornes;
    }
    
    private boolean interieurContient(ObjetPhysique objet) {
        if(objet.getType() == TypeObjet.Porte){
            return getBornesInterieursPorte().contains(objet.getBornes());
        }
        
        return getBornesInterieurs().contains(objet.getBornes());
    }
    
    private boolean collisionAireSuperieure(ObjetPhysique objetSource, ObjetPhysique autre) {
        Rectangle2D bornesSuperieures = new Rectangle2D.Double(objetSource.getPosition().getX().getTotal(),
                                                               0,
                                                               objetSource.getLargeur().getTotal(),
                                                               objetSource.getPosition().getY().getTotal());
        
        return bornesSuperieures.intersects(autre.getBornes());
    }
    
    private void validerCollision(List<Erreur> erreurs) {
        boolean aCollision = false;
        
        for(Accessoire accessoire : accessoires) {
            for(Accessoire autre : accessoires) {
                if(!accessoire.equals(autre)) {
                    if(accessoire.collisionner(autre)) {
                        erreurs.add(new Erreur("Deux accessoires ne peuvent pas se chevaucher."));
                        aCollision = true;
                        break;
                    } else if ((accessoire.getType() == TypeObjet.RetourAir || accessoire.getType() == TypeObjet.PriseElectrique) && 
                                collisionAireSuperieure(accessoire, autre)) {
                        erreurs.add(new Erreur("L'aire au-dessus d'un retour d'air ou d'une prise éléctrique doit être libre"));
                        aCollision = true;
                        break;
                    }
                }
            }
            
            for(Separateur separateur : separateurs) {
                if(accessoire.collisionner(separateur)) {
                    erreurs.add(new Erreur("Un accessoire ne peut pas chevaucher deux murs."));
                    aCollision = true;
                    break;
                }
            }
            
            if(aCollision) break;
        }
    }
    
    public ObjetPhysique selectionner(PointMesure coord) {
         
        ObjetPhysique objetTouche = this;
        
        if(collisionner(coord)) {
            objetTouche = obtenirObjetTouche(coord);
        }
        
        objetTouche.selectionner();
        return objetTouche;
    }
    
    public ObjetPhysique selectionnerHaut(PointMesure coord) {        
        for(Separateur separateur : separateurs) {
            if(separateur.collisionner(coord)) return separateur;
        }

        for(Accessoire accessoire : accessoires) {
            if(accessoire.getType() == TypeObjet.RetourAir) {
                RetourAir retourAir = (RetourAir) accessoire;
                
                if(retourAir.collisionnerTrouHaut(coord)) return retourAir;
            }
        }

        for(Mur mur : murs) {
            if(mur.collisionner(coord)) return mur;
        }
        
        return this;
    }
    
    public void creerSeparateur(PointMesure coord) {
        separateurs.add(new Separateur(this, coord));
        
        genererMurs();
    }
    
    public void supprimerSeparateur(Separateur separateur) {
        separateurs.remove(separateur);
        
        genererMurs();
    }
    
    public void ajouterAccessoire(Accessoire accessoire) {       
        accessoire.setParent(this);
        
        this.accessoires.add(accessoire);
        
        if(accessoire.getType() == TypeObjet.RetourAir) {
            accessoire.update();
        }
    }
    
    public void supprimerAccessoire(Accessoire accessoire) {
        accessoires.remove(accessoire);
    }
    
    public void supprimerMur(Mur mur) {
        PointMesure positionMur = mur.getPosition();
        
        int indexSeparateurPrecedent = -1;
        int indexSeparateurSuivant = 0;
        
        for (int i = 0; i < separateurs.size(); i++) {
            PointMesure positionSeparateur = separateurs.get(i).getPosition();
            if(positionSeparateur.getX().compareTo(positionMur.getX()) <= 0) {
                indexSeparateurPrecedent = i;
            }
        }
        
        indexSeparateurSuivant = indexSeparateurPrecedent + 1;
        
        if(indexSeparateurSuivant >= 0 && indexSeparateurSuivant < separateurs.size()) {
            separateurs.remove(indexSeparateurSuivant);
        }
        
        if(indexSeparateurPrecedent >= 0) {
            separateurs.remove(indexSeparateurPrecedent);
        }
        
        genererMurs();
    }
    
    protected Mur getMur(PointMesure position) {
        for(Mur mur : murs) {
            if(mur.collisionner(position)) {
                return mur;
            }
        }
        
        return null;
    }
    
    private ObjetPhysique obtenirObjetTouche(PointMesure coord) {
        for(Separateur separateur : separateurs) {
            if(separateur.collisionner(coord))return separateur;
        }

        for(Accessoire accessoire : accessoires) {
            if(accessoire.collisionner(coord)) return accessoire;
        }

        for(Mur mur : murs) {
            if(mur.collisionner(coord)) return mur;
        }
        
        return null;
    }
    
    public void update() {
        if(this.direction == Direction.Nord || this.direction == Direction.Sud) {
            this.largeur = getSalle().getLargeur();
        } else {
            this.largeur = getSalle().getLongueur();
        }
        this.hauteur = getSalle().getHauteur();
        
        for(Separateur separateur : separateurs) {
            separateur.update();
        }
        
        for(Accessoire accessoire : accessoires) {
            accessoire.update();
        }
        
        genererMurs();
    }
    
    public void genererMurs() {
        Collections.sort(separateurs);
        
        murs.clear();
        Mesure dernierePosition = new Mesure(0);
        int index = 1;
        for (Separateur separateur : separateurs) {
            
            Mesure positionSeparateur = separateur.getPosition().getX();
            
            ajouterMur(index, dernierePosition, positionSeparateur);
            dernierePosition = positionSeparateur;
            
            index++;
        }
        
        ajouterMur(index, dernierePosition, this.largeur);
        
        murs.get(0).setEstCoinGauche(true);
        murs.get(murs.size() - 1).setEstCoinDroite(true);
        
        for(Accessoire accessoire : accessoires) {
            if(accessoire.getType() == TypeObjet.RetourAir) {
                RetourAir retourAir = (RetourAir) accessoire;
                
                retourAir.centrer();
            }
        }
    }
    
    private void ajouterMur(int index, Mesure positionGauche, Mesure positionDroite) {
        Mesure largeurMur = positionDroite.soustraire(positionGauche);
            
        murs.add(new Mur(this, largeurMur, this.hauteur, new PointMesure(positionGauche, new Mesure(0)), index));
    }
    
    public List<Accessoire> getAccessoiresMur(Mur mur) {
        List<Accessoire> accessoiresMur = new ArrayList<>();
        
        for(Accessoire accessoire : accessoires) {
            if(mur.contient(accessoire)) {
                accessoiresMur.add(accessoire);
            }
        }
        
        return accessoiresMur;
    }
    
    @Override
    public ListableDTO getDTO(){
        return new CoteDTO(this);
    }
    
    @Override
    protected Salle getSalle() {
        return salle;
    }
    
    @Override
    public void modifier(ListableDTO DTO){
        CoteDTO  coteDTO = (CoteDTO) DTO;
        
        this.direction = coteDTO.Direction;
        this.largeur = coteDTO.Largeur;
        this.hauteur = coteDTO.Hauteur;
        this.position =  coteDTO.Position;
        
        genererMurs();
    }
    
    @Override
    public String toString() {
        String txtLists = "\n";
        
        for(Separateur separateur : separateurs)
            txtLists += separateur.toString() + "\n";
        for(Mur mur : murs)
            txtLists += mur.toString() + "\n";
        for(Accessoire accessoire : accessoires)
            txtLists += accessoire.toString() + "\n";
        
        return String.format("<Cote direction:\"%s\" %s>%s<\\Cote>", direction.toString(), attrToString(), txtLists);
    }

    boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
