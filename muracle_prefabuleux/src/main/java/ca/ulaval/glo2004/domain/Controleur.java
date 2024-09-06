package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Direction;
import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.GestionnaireEtatProjet;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Controleur {
    private final GestionnaireEtatProjet gestionnaireEtat;
    private final AccessoireFactory accessoireFactory;
    private final ArrayList<IChangementSelectionObservateur> observateursChangementSelection;
    
    private Projet projet;
    private ObjetPhysique objetSelectionne;
    private Direction directionSelectionne;
    private boolean estInterieur;
    private boolean afficherGrille;
    private Mesure distanceGrille;
    private Mur dernierMur;
    
    public Controleur() {
        directionSelectionne = Direction.Nord;
        this.projet = new Projet();
        this.observateursChangementSelection = new ArrayList<>();
        this.gestionnaireEtat = new GestionnaireEtatProjet(this.projet);
        this.accessoireFactory = new AccessoireFactory(this.projet.getSalle()); 
    }
    
    public void creerProjet() {
        deselectionner();
        dernierMur = null;
        
        projet = new Projet();
        
        gestionnaireEtat.setProjet(projet);
        accessoireFactory.setSalle(projet.getSalle());
    }
    
    public void ouvrirProjet(String cheminFichier) {
        deselectionner();
        dernierMur = null;
        
        projet = new Projet(cheminFichier);
        
        gestionnaireEtat.setProjet(projet);
        accessoireFactory.setSalle(projet.getSalle());
    }
    
    public String getCheminProjet() {
        return projet.getCheminFichier();
    }
    
    public void setCheminProjet(String cheminFichier) {
        projet.setCheminFichier(cheminFichier);
    }
    
    public void enregistrerProjet() {
        projet.enregistrer();
    }
    
    public void annuler() {
        gestionnaireEtat.annuler(); 
        
        if(objetSelectionne != null) objetSelectionne.selectionner(); 
    }
    
    public void refaire() {
        gestionnaireEtat.refaire();
    }
    
    public void exporter(String chemin, List<PanneauDTO> panneauxAExporter) throws ParserConfigurationException, TransformerException {
        Set<UUID> idPanneaux = new HashSet<>();
        for(PanneauDTO panneau : panneauxAExporter) {
            idPanneaux.add(panneau.Id);
        }
        
        for(Panneau panneau:projet.getSalle().getPanneaux(idPanneaux)){
            panneau.exporter(chemin);
        }
    }
    
    public List<Erreur> ajouterObjetSalle(TypeObjet type, PointMesure coord) {
        if(type == TypeObjet.Separateur){
            projet.getSalle().creerSeparateur(coord);
        } else if(type == TypeObjet.RetourAir) {
            projet.getSalle().ajouterAccessoire(type, coord, accessoireFactory);
        }    
        
        return validerEtatSalle();
    }
    
    public List<Erreur> ajouterObjetCote(TypeObjet type, PointMesure coord) {        
        Cote cote = projet.getSalle().getCote(directionSelectionne);
   
        if(type == TypeObjet.Separateur) {
            cote.creerSeparateur(coord);
        } else if(estInterieur || (type != TypeObjet.RetourAir && type != TypeObjet.PriseElectrique)) {
            cote.ajouterAccessoire(accessoireFactory.creerAccessoire(type, coord));
        }
        
        return validerEtatSalle();
    }
    
    public List<Erreur> supprimerObjetSelectionne() {
        if(objetSelectionne != null && objetSelectionne.supprimer()) {
            deselectionner();

            return validerEtatSalle();
        }

        return new ArrayList<>();
    }
    
    public void selectionnerSalle(PointMesure coord) {
        changerSelection(projet.getSalle().selectionner(coord));
    }
    
    public void selectionnerCote(PointMesure coord) {
        Cote cote = projet.getSalle().getCote(directionSelectionne);        
        changerSelection(cote.selectionner(coord));
    }
    
    private void changerSelection(ObjetPhysique nouvelObjetSelectionne) {
        if(nouvelObjetSelectionne != objetSelectionne) {
            deselectionner();
            objetSelectionne = nouvelObjetSelectionne;
            notifierChangementSelection();
            
            if(objetSelectionne.getType() == TypeObjet.Mur) {
                dernierMur = (Mur) objetSelectionne;
            }
        }
    }
    
    public void deselectionner() {
        if(objetSelectionne != null) {
            objetSelectionne.deselectionner();
            
            objetSelectionne = null;
            notifierChangementSelection();
        }
    }
    
    public ListableDTO getObjetSelectionne(){
        
        if(objetSelectionne != null){
            return objetSelectionne.getDTO();
        }
        return null;
    }   
    
    public MurDTO getMurSelectionne() {
        if(dernierMur == null) {
            dernierMur = projet.getSalle().getCote(Direction.Nord).getListeMur().get(0);
        }
       
        return (MurDTO) dernierMur.getDTO();
    }
    
    public SalleDTO getSalle() {
        return (SalleDTO) projet.getSalle().getDTO();
    }
    
    public CoteDTO getCoteSelectionne() {
        return (CoteDTO) projet.getSalle().getCote(directionSelectionne).getDTO();
    }
    
    public void setDirectionSelectionne(Direction direction) {
        this.directionSelectionne = direction;
    }
    
    public void setEstInterieur(boolean interieur) {
         this.estInterieur = interieur;
    }
    
    public boolean getEstInterieur()
    {
        return estInterieur;
    }
    
    public void setAfficherGrille(boolean grille) {
         this.afficherGrille = grille;
    }
    
    public boolean getAfficherGrille()
    {
        return afficherGrille;
    }
    
    public void setDistanceGrille(Mesure distance){
        this.distanceGrille = distance;
    }
    
    public Mesure getDistanceGrille() {
        if (distanceGrille == null){
            return distanceGrille = new Mesure(12,0,1);
        }
        else{
            return distanceGrille;
        }
    }
        
    public List<Erreur> modifierObjetSelectionne(ListableDTO objetDTO) {
        objetSelectionne.modifier(objetDTO);
        
        return validerEtatSalle();
    }  
    
    public void deplacerObjetCote(PointMesure pointDrag) { 
        
        if(objetSelectionne.getType() != TypeObjet.Cote){
            objetSelectionne.deplacer(pointDrag);
            projet.getSalle().getCote(directionSelectionne).update();
        }
        
    }
    
    public void deplacerObjetSalle(PointMesure pointDrag) {
        PointMesure position = projet.getSalle().getPositionSurCote(pointDrag);
        
        if(position != null) {
            objetSelectionne.deplacer(position);
            projet.getSalle().update();
        }
    }
    
    public List<Erreur> confirmerDeplacement() {
        notifierChangementSelection();
        
        return validerEtatSalle();
    }
    
    public void ajouterObservateurChangementSelection(IChangementSelectionObservateur observateur) {
        if(!observateursChangementSelection.contains(observateur)) {
            observateursChangementSelection.add(observateur);
        }
    }
    
    public void enleverObservateurChangementSelection(IChangementSelectionObservateur observateur) {
        observateursChangementSelection.remove(observateur);
    }
    
    public void notifierChangementSelection() {
        for(IChangementSelectionObservateur observateur : observateursChangementSelection) {
            observateur.onObjetSelectionneChange();
        }
    }
    
    private List<Erreur> validerEtatSalle() {
        List<Erreur> erreurs = new ArrayList<>();
        
        this.projet.getSalle().valider(erreurs);
        
        if(!erreurs.isEmpty()) {
            deselectionner();
            
            gestionnaireEtat.rechargerDerniereEtat();
            
            //if(objetSelectionne != null) objetSelectionne.selectionner();
        } else {
            if(objetSelectionne != null) objetSelectionne.deselectionner();
            
            gestionnaireEtat.enregistrerEtat();
            
            if(objetSelectionne != null) objetSelectionne.selectionner();
        }
        
        return erreurs;
    }
       
    @Override
    public String toString() {
        String typeObjSel = objetSelectionne == null ? "null" : objetSelectionne.getClass().getName();
        
        return String.format("<Controleur typeSelection=\"%s\">\n%s\n%s\n<\\Controleur>", typeObjSel, projet.getSalle().toString(), gestionnaireEtat.toString());
    }
}
