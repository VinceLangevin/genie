package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Direction;
import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Salle extends ObjetPhysique {
    //Les quatres côtés de la salle sont en ordre Nord, Est, Sud, Ouest pour faciliter la recherche d'un côté en particulier à l'aide de l'enum Direction
    private EnumMap<Direction, Cote> cotes;
  
    
    private Mesure longueur;
    private Mesure epaisseurMurs;
    
    private Mesure hauteurRetoursAir;
    private Mesure distanceSolRetourAir;
    private Mesure hauteurTrouHautRetoursAir;
    
    private double densiteMatierePremiere;//lb/pieds carrées
    private Mesure epaisseurPliSoudure;
    private Mesure epaisseurMateriaux;
    private double masseMaximalePanneau; //Masse en livres
    private double angleDegreePliSoudure;
    
    private Mesure margePliage;
    
    public Salle() {
        this(new Mesure(144), new Mesure(108), new Mesure(96), new Mesure(8));
    }
    
    public Salle(Mesure largeur, Mesure longueur, Mesure hauteur, Mesure epaisseurMurs) {
        super(TypeObjet.Salle, largeur, hauteur, new PointMesure());
        
        this.longueur = longueur;
        this.epaisseurMurs = epaisseurMurs;
        
        hauteurRetoursAir = new Mesure(6);
        distanceSolRetourAir = new Mesure (8);
        hauteurTrouHautRetoursAir = new Mesure(6);
        
        margePliage = new Mesure(0, 1, 4);
        epaisseurMateriaux = new Mesure(0, 1, 2);
        epaisseurPliSoudure = new Mesure(2, 0, 2);
        angleDegreePliSoudure = 45;
        
        masseMaximalePanneau = 250; 
        densiteMatierePremiere = 6.3;
        
       
        creerCotes();
    }
    
    public EnumMap<Direction, Cote> getListeCote(){
        return this.cotes;
    }
    
    public Cote getCote(Direction direction) {
        return this.cotes.get(direction);
    }
    
    public Mesure getLongueur(){
        return this.longueur;
    }
    
    public Mesure getHauteurRetoursAir() {
        return this.hauteurRetoursAir;
    }
    
    public Mesure getHauteurTrouHautRetoursAir() {
        return this.hauteurTrouHautRetoursAir; 
    }
    
    public Mesure getDistanceSolRetoursAir() {
        return this.distanceSolRetourAir;
    }
    
    public Mesure getEpaisseurMurs(){
        return this.epaisseurMurs;
    }
    
    public double getMasseMaximalePanneau() {
        return this.masseMaximalePanneau;
    }
    
    public double getDensiteMatierePremiere() {
        return this.densiteMatierePremiere;
    }
    
    public Mesure getEpaisseurPliSoudure() { 
        return this.epaisseurPliSoudure;
    }
    
    public double getAnglePliSoudure() {
        return this.angleDegreePliSoudure;
    }
    
    public Mesure getMargePliage() {
        return this.margePliage;
    }
    
    public Mesure getEpaisseurMateriaux() {
        return epaisseurMateriaux;
    }
    
    public List<Panneau> getPanneaux( Set<UUID> idPanneaux){
        List<Panneau>panneau = new ArrayList<>();
        for(Cote cote: cotes.values()){
            for(Mur mur: cote.getListeMur()){
                if(idPanneaux.contains(mur.getPanneauExterieur().getID())){
                    panneau.add(mur.getPanneauExterieur());
                }
                if(idPanneaux.contains(mur.getPanneauInterieur().getID())){
                    panneau.add(mur.getPanneauInterieur());
                }
            }
            
        }
        return panneau;
    }
    
    public void ajouterAccessoire(TypeObjet type, PointMesure coord, AccessoireFactory accessoireFactory){
        Direction direction = getCotePointe(coord);
        if(direction != null) {
            Cote cote = cotes.get(direction);
            PointMesure position = getPosition(cote, coord);
            cote.ajouterAccessoire(accessoireFactory.creerAccessoire(type, position));
        }
    }
    
    public void creerSeparateur(PointMesure coord) {
        Direction direction = getCotePointe(coord);
        if(direction != null) {
            Cote cote = cotes.get(direction);
            PointMesure position = getPosition(cote, coord);
            cote.creerSeparateur(position);
        }
    }
               
    public ObjetPhysique selectionner(PointMesure coord) {
        ObjetPhysique objetSelectionne = this;
        Direction direction = getCotePointe(coord);
        
        if(direction != null) {
            Cote cote = cotes.get(direction);
            
            PointMesure positionClick = getPosition(cote, coord);
            objetSelectionne = cote.selectionnerHaut(positionClick);
        } 
        
        
        objetSelectionne.selectionner();
        return objetSelectionne; 
    }
    
    private PointMesure getPosition(Cote cote, PointMesure coord) {
        switch(cote.getDirection()) {
            case Nord:
                return new PointMesure(cote.getLargeur().soustraire(coord.getX()),
                                       coord.getY());               
            case Sud:
                return new PointMesure(coord.getX(), 
                                       this.longueur.soustraire(coord.getY()));
            case Est:
                return new PointMesure(cote.getLargeur().soustraire(coord.getY()), 
                                       this.largeur.soustraire(coord.getX()));                
            case Ouest:
                return new PointMesure(coord.getY(), 
                                       coord.getX());
        }
        
        return new PointMesure();
    }
    
    public PointMesure getPositionSurCote(PointMesure coord){
          Cote coteSelectionne = cotes.get(getCotePointe(coord));
        if(coteSelectionne != null)
        {
            return getPosition(coteSelectionne, coord);
        }
        
         return null;
    }
    
    private Direction getCotePointe(PointMesure coord) {
        
        // 0----------1
        // |4--------5|
        // ||        ||
        // |7--------6|
        // 3----------2
        PointMesure[] points = new PointMesure[8];
        points[0] = new PointMesure(new Mesure(), new Mesure());
        points[1] = new PointMesure(this.largeur, new Mesure());
        points[2] = new PointMesure(this.largeur, this.longueur);
        points[3] = new PointMesure(new Mesure(), this.longueur);
        
        points[4] = new PointMesure(this.epaisseurMurs, this.epaisseurMurs);
        points[5] = new PointMesure(this.largeur.soustraire(this.epaisseurMurs), this.epaisseurMurs);
        points[6] = new PointMesure(this.largeur.soustraire(this.epaisseurMurs), this.longueur.soustraire(this.epaisseurMurs));
        points[7] = new PointMesure(this.epaisseurMurs, this.longueur.soustraire(this.epaisseurMurs));
        
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        for (Direction direction : Direction.values()) {
            int[] noSommets = new int[4];
            switch(direction) {
                case Nord:
                    noSommets = new int[] {0, 1, 5, 4};
                    break;
                case Sud:
                    noSommets = new int[] {7, 6, 2, 3};
                    break;
                case Est:
                    noSommets = new int[] {1, 2, 6, 5};
                    break;
                case Ouest:
                    noSommets = new int[] {0, 4, 7, 3};
                    break;
            }
            
            for (int i = 0; i < noSommets.length; i++) {
                xPoints[i] = (int) points[noSommets[i]].getX().getTotal();
                yPoints[i] = (int) points[noSommets[i]].getY().getTotal();
            }
            
            Polygon perimetreCote = new Polygon(xPoints, yPoints, 4);
            
            if(perimetreCote.contains(coord.getX().getTotal(),
                                      coord.getY().getTotal())) {
                return direction;
            }
        }
        
        return null;
    }
    
    private void creerCotes() {
        this.cotes = new EnumMap<>(Direction.class);
        
        for(Direction direction : Direction.values()) {
            Cote cote;
            
            if(direction == Direction.Nord || direction == Direction.Sud) {
                cote = new Cote(this, this.largeur, this.hauteur, direction);
                Mesure quartLargeur = this.largeur.multiplier(new Mesure(0, 1, 4));
                
                cote.creerSeparateur(new PointMesure(quartLargeur, new Mesure()));
                cote.creerSeparateur(new PointMesure(quartLargeur.multiplier(new Mesure(2)), new Mesure()));
                cote.creerSeparateur(new PointMesure(quartLargeur.multiplier(new Mesure(3)), new Mesure()));
            } else {
                cote = new Cote(this, this.longueur, this.hauteur, direction);
                
                Mesure tierLargeur = this.longueur.multiplier(new Mesure(0, 1, 3));
                cote.creerSeparateur(new PointMesure(tierLargeur, new Mesure()));
                cote.creerSeparateur(new PointMesure(tierLargeur.multiplier(new Mesure(2)), new Mesure()));
            }
            
            cotes.put(direction, cote); 
        }
    }
    
    @Override
    public ListableDTO getDTO(){
        return new SalleDTO(this);
    }
    
    @Override
    protected Salle getSalle() {
        return this;
    }
    
    @Override
    public void valider(List<Erreur> erreurs) {       
        if(this.longueur.getTotal() < 0) {
            erreurs.add(new Erreur("La valeur de la hauteur de la salle est vide"));
        }
        if(this.epaisseurMurs.getTotal() < 0) {
            erreurs.add(new Erreur("La valeur de l'epaisseur du mur est vide"));
        }
        if(this.hauteurRetoursAir.getTotal() < 0) {
            erreurs.add(new Erreur("La valeur de la hauteur retour air est vide"));
        }
        if(this.distanceSolRetourAir.getTotal() < 0){
            erreurs.add(new Erreur("La distance entre le sol et retour air est vide"));
        }
        if(this.densiteMatierePremiere < 0){
            erreurs.add(new Erreur("La densité de la matière première ne peut pas etre inferieur a 0"));
        }
        if(this.masseMaximalePanneau < 0){
            erreurs.add(new Erreur("La masse du mur est inferieur a 0"));
        }
        
        if(this.angleDegreePliSoudure < 5 || this.angleDegreePliSoudure > 90){
            erreurs.add(new Erreur("L'angle dégré pli soudure doit se retrouver dans l'intervalle [5,90]"));
        }
        
        if(this.getEpaisseurMurs().getTotal()< this.hauteurTrouHautRetoursAir.getTotal()){
            erreurs.add(new Erreur("la hauteur des retours d'air ne peut pas être plus grande que l'épaisseur des murs"));
        }
        
        if(this.getEpaisseurMurs().getTotal() > this.getLargeur().multiplier(new Mesure(0,1,2).multiplier(new Mesure(0,4,5))).getTotal()){
            erreurs.add(new Erreur("la salle doit contenir un espace dans le centre"));
        }
        
        if(this.getEpaisseurMurs().getTotal() > this.getLongueur().multiplier(new Mesure(0,1,2).multiplier(new Mesure(0,4,5))).getTotal()){
            erreurs.add(new Erreur("la salle doit contenir un espace dans le centre"));
        
        }
        
        for(Cote cote : cotes.values()) {
            cote.valider(erreurs);
        }
    }
    
    @Override
    public void modifier(ListableDTO DTO) {
        SalleDTO  salleDTO = (SalleDTO) DTO;
        this.largeur = salleDTO.Largeur;
        this.longueur = salleDTO.Longueur;
        this.hauteur = salleDTO.Hauteur;
        this.epaisseurMurs = salleDTO.EpaisseurMurs;
        
        this.hauteurRetoursAir = salleDTO.HauteurRetoursAir;
        this.distanceSolRetourAir = salleDTO.DistanceSolRetourAir;
        this.hauteurTrouHautRetoursAir = salleDTO.HauteurTrouHautRetourAir;
        
        this.densiteMatierePremiere = salleDTO.DensiteMatierePremiere;
        this.masseMaximalePanneau = salleDTO.MasseMaximalePanneau;
        
        this.angleDegreePliSoudure = salleDTO.AngleDegreePliSoudure;
        this.epaisseurPliSoudure = salleDTO.EpaisseurPliSoudure;
        this.epaisseurMateriaux = salleDTO.EpaisseurMateriaux;
        this.margePliage = salleDTO.MargePliage;
        
        update();
    }
    
    public void update() {
        for(Cote cote : cotes.values()) {
            cote.update();
        }
    }
    
       
    @Override
    public String toString() {
        String txtCotes = "";
        for(Cote cote : cotes.values())
            txtCotes += cote.toString() + "\n";
        
        return String.format("<Salle longueur:\"%f\" %s>\n%s<\\Salle>", longueur.getTotal(), attrToString(), txtCotes);
    }

    
}
