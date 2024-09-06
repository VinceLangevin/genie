package ca.ulaval.glo2004.domain.Afficheurs;

import ca.ulaval.glo2004.domain.AccessoireDTO;
import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.CoteDTO;
import ca.ulaval.glo2004.domain.RetourAirDTO;
import ca.ulaval.glo2004.domain.SalleDTO;
import ca.ulaval.glo2004.domain.util.Direction;
import ca.ulaval.glo2004.domain.SeparateurDTO;
import ca.ulaval.glo2004.domain.TypeObjet;
import ca.ulaval.glo2004.domain.util.PointMesure;
import ca.ulaval.glo2004.domain.util.Mesure;
import java.awt.Graphics;

public class AfficheurPlanSalle extends Afficheur {
    
    public AfficheurPlanSalle(Controleur controleur){
        super(controleur);
    }
    
    @Override
    public void centrer(int dimensionX, int dimensionY) {
        SalleDTO salle = controleur.getSalle();
        
        echelle.centrer(dimensionX, dimensionY, salle.Largeur, salle.Longueur);
    }
    
    //Référence : Largeur = X; Longueur = Y;
    @Override
    public void draw(Graphics g) {
        SalleDTO salle = controleur.getSalle();
        
        drawGrille(g);
        
        drawDelimitations(g, salle);
        
        drawJointsCotes(g, salle);
        
        for(CoteDTO cote : salle.Cotes) {
            for(SeparateurDTO separateur : cote.Separateurs) {
                drawSeparateur(g, salle, separateur, cote.Direction);
            }
            
            for(AccessoireDTO accessoire : cote.Accessoires) {
                if(accessoire.Type == TypeObjet.RetourAir) {
                    RetourAirDTO retourAir = (RetourAirDTO) accessoire;
                    
                    drawRetourAir(g, salle, retourAir, cote.Direction);
                }
            }
        }
    }
    
    private void drawDelimitations(Graphics g, SalleDTO salle) {
        //Extérieur 
        drawRectangle(g, new Mesure(), new Mesure(), salle.Largeur, salle.Longueur);
        
        //Intérieur
        Mesure doubleEpaisseurs = salle.EpaisseurMurs.multiplier(new Mesure(2));
        drawRectangle(g, salle.EpaisseurMurs, salle.EpaisseurMurs, salle.Largeur.soustraire(doubleEpaisseurs), salle.Longueur.soustraire(doubleEpaisseurs));
    }
    
    private void drawJointsCotes(Graphics g, SalleDTO salle) {
        //Joint des côtés (Origine = Point extérieur; Destination = Point intérieur)
        PointMesure origine = new PointMesure();
        PointMesure destination = new PointMesure(salle.EpaisseurMurs, salle.EpaisseurMurs);
        
        //Nord-Ouest (Haut gauche)
        drawLigne(g, origine, destination);
        
        //Nord-Est (Haut droite)
        origine.setX(salle.Largeur);
        destination.setX(origine.getX().soustraire(salle.EpaisseurMurs));
        
        drawLigne(g, origine, destination);
        
        //Sud-Est (Bas droite)
        origine.setY(salle.Longueur);
        destination.setY(origine.getY().soustraire(salle.EpaisseurMurs));
        
        drawLigne(g, origine, destination);
        
        //Sud-Ouest (Bas gauche)
        origine.setX(new Mesure());
        destination.setX(salle.EpaisseurMurs);
        
        drawLigne(g, origine, destination);
    }

    private void drawSeparateur(Graphics g, SalleDTO salle, SeparateurDTO separateur, Direction direction) {
        PointMesure origine = new PointMesure();
        PointMesure destination = new PointMesure();
        
        switch(direction) {
            case Nord:
                origine.setLocation(salle.Largeur.soustraire(separateur.Position.getX()), new Mesure());
                destination.setLocation(salle.Largeur.soustraire(separateur.Position.getX()), salle.EpaisseurMurs);
                break;
                
            case Est:
                origine.setLocation(salle.Largeur, salle.Longueur.soustraire(separateur.Position.getX()));
                destination.setLocation(salle.Largeur.soustraire(salle.EpaisseurMurs), salle.Longueur.soustraire(separateur.Position.getX()));
                break;
            
            case Sud:
                origine.setLocation(separateur.Position.getX(), salle.Longueur);
                destination.setLocation(separateur.Position.getX(), salle.Longueur.soustraire(salle.EpaisseurMurs));
                break;
                
            case Ouest:
                origine.setLocation(new Mesure(), separateur.Position.getX());
                destination.setLocation(salle.EpaisseurMurs, separateur.Position.getX());
                break;
        }
        
        drawLigne(g, origine, destination, separateur.EstSelectionne);
    }
    
    private void drawRetourAir(Graphics g, SalleDTO salle, RetourAirDTO retourAir, Direction direction) {
        PointMesure origine = new PointMesure();
        PointMesure dimension = new PointMesure();
        
        Mesure hauteurCentree = salle.EpaisseurMurs.soustraire(retourAir.HauteurTrouHaut).multiplier(new Mesure(0, 1, 2));
        
        switch(direction) {
            case Nord:
                origine.setLocation(salle.Largeur.soustraire(retourAir.Position.getX()).soustraire(retourAir.Largeur), hauteurCentree);
                dimension.setLocation(retourAir.Largeur, retourAir.HauteurTrouHaut);
                break;
                
            case Est:
                origine.setLocation(salle.Largeur.soustraire(hauteurCentree).soustraire(retourAir.HauteurTrouHaut), salle.Longueur.soustraire(retourAir.Position.getX()).soustraire(retourAir.Largeur));
                dimension.setLocation(retourAir.HauteurTrouHaut, retourAir.Largeur);
                break;
            
            case Sud:
                origine.setLocation(retourAir.Position.getX(), salle.Longueur.soustraire(hauteurCentree).soustraire(retourAir.HauteurTrouHaut));
                dimension.setLocation(retourAir.Largeur, retourAir.HauteurTrouHaut);
                break;
                
            case Ouest:
                origine.setLocation(hauteurCentree, retourAir.Position.getX());
                dimension.setLocation(retourAir.HauteurTrouHaut, retourAir.Largeur);
                break;
        }
        
        drawRectangle(g, origine.getX(), origine.getY(), dimension.getX(), dimension.getY(), retourAir.EstSelectionne);
    }
}

  

