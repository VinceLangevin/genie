package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.util.Erreur;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Panneau extends ObjetPhysique {
    private String nom;
    private final UUID id;
    protected transient Area aire;
    protected transient Point2D positionPanneauCentral;
    protected Mur mur;
   
    public Panneau(int indexMur, Mur mur) {
        super(TypeObjet.Separateur, mur.getLargeur(), mur.getHauteur());

        this.mur = mur;
        this.position = new PointMesure(mur.position);
        this.id = UUID.randomUUID();
        
        this.nom = String.format("%s_Mur%d_%s", mur.getCote().getDirection(), indexMur, estExterieur() ? "Exterieur" : "Interieur");
    }
    
    public UUID getID() {
        return id;
    }
    
    @Override
    public ListableDTO getDTO() {
        return new PanneauDTO(this);
    }
    
    @Override 
    protected Salle getSalle() {
        return mur.getSalle();
    }
    
    public Area getAire() {
        calculerAire();
        
        return calculerAireAvecAccessoires();
    }   
    
    protected abstract double calculAireTotalePouces();
    protected abstract Area calculerAireAvecAccessoires();
    protected abstract void calculerAire();
    protected abstract boolean estExterieur();
    protected abstract void validerDimension(List<Erreur> erreurs);
    
    @Override
    public void valider(List<Erreur> erreurs) {                       
        if(nom.isEmpty()){
            erreurs.add(new Erreur("Le panneau doit avoir un nom pour sa référence"));
        }

        if(hauteur.getTotal() < 0) {
            erreurs.add(new Erreur("La hauteur du panneau doit exister être supérieure à 0"));
        }
        
        if(largeur.getTotal() < 0) {
            erreurs.add(new Erreur("La largeur du panneau être supérieure à 0"));
        }        
        
        Area tempAire = getAire();
        if(tempAire.isEmpty()){
            erreurs.add(new Erreur("L'aire du panneau ne peut pas etre vide"));
        }
        
        double aireTotale = calculAireTotalePouces();
        double poidsTotal = aireTotale * getSalle().getDensiteMatierePremiere() / 144; //1 pieds^2 = 144 pouces^2
        
        if(poidsTotal > getSalle().getMasseMaximalePanneau()) {
            erreurs.add(new Erreur(String.format("Le poids du panneau %s (%.2f lbs) dépasse le poids maximal alloué", nom, poidsTotal, aireTotale)));
        }
        
        validerDimension(erreurs);
    }
    
    public void exporter(String chemin) throws ParserConfigurationException, TransformerException {
        String ns = "http://www.w3.org/2000/svg";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        List<String> polygons = obtenirSommet();

        Document doc = builder.newDocument();

        Element root = doc.createElementNS(ns, "svg");
        doc.appendChild(root);
        for(String string : polygons){ 
            Element panneau = doc.createElementNS(ns, "polygon");
            panneau.setAttribute("points",string);
            panneau.setAttribute("stroke-width", "0.2");
            panneau.setAttribute("stroke", "black");
            panneau.setAttribute("fill", "none");

            root.appendChild(panneau);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);

        File myFile = new File(String.format("%s/%s.svg", chemin, nom));

        StreamResult file = new StreamResult(myFile);

        transf.transform(source, file);
    }
    
    private List<String> obtenirSommet() {
        List<String> polygons = new ArrayList<>();
        List<String> sommetsPolygons = new ArrayList<>();
        
        PathIterator itr = calculerAireAvecAccessoires().getPathIterator(null);
        
        double[] coords = new double[6];

        while(!itr.isDone()) {
            int retour = itr.currentSegment(coords);
            
            switch(retour) {
                case PathIterator.SEG_MOVETO:
                    sommetsPolygons.add(String.format("%f, %f", coords[0], coords[1]));
                    break;
                case PathIterator.SEG_LINETO:
                    sommetsPolygons.add(String.format("%f, %f", coords[0], coords[1]));
                    break;
                case PathIterator.SEG_CLOSE:
                    polygons.add(String.join(" ", sommetsPolygons));
                    sommetsPolygons.clear();
                    break;
            }
            itr.next();
        }
        
        return polygons;
    }
    
    @Override
    public void modifier(ListableDTO panneauDTO) {
        PanneauDTO dto = (PanneauDTO) panneauDTO;
        this.hauteur = dto.Hauteur;
        this.largeur = dto.Largeur;
        this.position = dto.Position;
        this.aire = dto.Aire;
    }
    
    protected class DimensionAccessoire {
        public double PositionX;
        public double PositionY;
        public double Largeur;
        public double Hauteur;
        
        public DimensionAccessoire(double posX, double posY, double largeur, double hauteur) {
            this.PositionX = posX;
            this.PositionY = posY;
            this.Largeur = largeur;
            this.Hauteur = hauteur;
        }
    }
}
