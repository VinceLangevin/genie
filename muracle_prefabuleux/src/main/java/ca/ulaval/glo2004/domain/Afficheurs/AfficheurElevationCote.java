package ca.ulaval.glo2004.domain.Afficheurs;

import ca.ulaval.glo2004.domain.AccessoireDTO;
import ca.ulaval.glo2004.domain.Controleur;
import ca.ulaval.glo2004.domain.CoteDTO;
import ca.ulaval.glo2004.domain.FenetreDTO;
import ca.ulaval.glo2004.domain.MurDTO;
import ca.ulaval.glo2004.domain.PorteDTO;
import ca.ulaval.glo2004.domain.PriseElectriqueDTO;
import ca.ulaval.glo2004.domain.RetourAirDTO;
import ca.ulaval.glo2004.domain.SalleDTO;
import ca.ulaval.glo2004.domain.SeparateurDTO;
import ca.ulaval.glo2004.domain.TypeObjet;
import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.Graphics;


public class AfficheurElevationCote extends Afficheur {
    
    public AfficheurElevationCote(Controleur controleur)  
    {
        super(controleur);     
    }
    
    @Override
    public PointMesure obtenirPositionPouces(int x, int y) {
        PointMesure point = echelle.pixelsVersPouces(x, y, true);
        
        if(controleur.getEstInterieur()) {
            point = inverserPoint(point);
        }
        
        return point;
    }
    
    private PointMesure inverserPoint(PointMesure positionFin){
        CoteDTO cote = controleur.getCoteSelectionne();
        Mesure largeur = cote.Largeur;
        Mesure pointX = largeur.soustraire(positionFin.getX());
        
        return new PointMesure(pointX, positionFin.getY());
    }
    
     @Override
    public void draw(Graphics g) {
        CoteDTO coteDTO = controleur.getCoteSelectionne();
        
        drawCote(g,coteDTO);
        
        drawGrille(g);
    }

    @Override
    public void centrer(int dimensionX, int dimensionY) {
        CoteDTO coteDTO = controleur.getCoteSelectionne();
        echelle.centrer(dimensionX, dimensionY, coteDTO.Largeur, coteDTO.Hauteur);
    }
    
    
    public void drawCote(Graphics g, CoteDTO coteDTO) 
    {
        SalleDTO salleDTO = controleur.getSalle();
        
        if(controleur.getEstInterieur()) {
            drawRectangle(g,
                          coteDTO.Position.getX().ajouter(salleDTO.EpaisseurMurs),
                          coteDTO.Position.getY(),
                          coteDTO.Largeur.soustraire(salleDTO.EpaisseurMurs.multiplier(new Mesure(2))),
                          coteDTO.Hauteur,coteDTO.EstSelectionne);
        } else {
            drawRectangle(g,
                          coteDTO.Position.getX(),
                          coteDTO.Position.getY(),
                          coteDTO.Largeur,coteDTO.Hauteur,
                          coteDTO.EstSelectionne);
            
            //Déssiner les limites pour déplacer les objets
            
            Mesure posX = coteDTO.Position.getX().ajouter(salleDTO.EpaisseurMurs);
            drawLignePointille(g, new PointMesure(posX, new Mesure(0)), 
                                  new PointMesure(posX, coteDTO.Hauteur));
            
            posX = coteDTO.Position.getX().ajouter(coteDTO.Largeur).soustraire(salleDTO.EpaisseurMurs);
            drawLignePointille(g, new PointMesure(posX, new Mesure(0)), 
                                  new PointMesure(posX, coteDTO.Hauteur));
        }
        
        for(SeparateurDTO separateur : coteDTO.Separateurs) {
            drawSeparateur(g,coteDTO, separateur);
        }
        
        for(AccessoireDTO accessoire : coteDTO.Accessoires) {
            drawAccessoire(g,coteDTO,accessoire); 
        }
        
        for(MurDTO mur : coteDTO.Murs) {
            if(mur.EstSelectionne) {                
                if(controleur.getEstInterieur()) {
                    PointMesure positionFin = new PointMesure(mur.Position.getX().ajouter(mur.Largeur),mur.Position.getY());
                    PointMesure pointInverse = inverserPoint(positionFin);
                    if (mur.Position.getX().compareTo(new Mesure(0) )==0 && mur.Position.getX().ajouter(mur.Largeur).compareTo(coteDTO.Largeur)==0)
                    {
                        drawMur(g,pointInverse.getX().ajouter(salleDTO.EpaisseurMurs),pointInverse.getY(),
                        mur.Largeur.soustraire(salleDTO.EpaisseurMurs.multiplier(new Mesure(2))),mur.Hauteur,mur.EstSelectionne);
                    }
                    else if(mur.Position.getX().compareTo(new Mesure(0))==0)
                    {
                        drawMur(g,pointInverse.getX(),pointInverse.getY(),
                        mur.Largeur.soustraire(salleDTO.EpaisseurMurs),mur.Hauteur,mur.EstSelectionne);
                    }
                    else if (mur.Position.getX().ajouter(mur.Largeur).compareTo(coteDTO.Largeur)==0) {
                        drawMur(g,pointInverse.getX().ajouter(salleDTO.EpaisseurMurs),pointInverse.getY(),
                        mur.Largeur.soustraire(salleDTO.EpaisseurMurs),mur.Hauteur,mur.EstSelectionne);
                    }
                    else {
                        drawMur(g,pointInverse.getX(),pointInverse.getY(),mur.Largeur,mur.Hauteur,mur.EstSelectionne);
                    }
                  
                }
                else {
                    drawMur(g,mur.Position.getX(),mur.Position.getY(),mur.Largeur,mur.Hauteur,mur.EstSelectionne);
                }
            }
        }
    }

    public void drawSeparateur(Graphics g,CoteDTO coteDTO, SeparateurDTO separateur) {   
        if(controleur.getEstInterieur())
        {
            drawLigne(g,coteDTO.Largeur.soustraire(separateur.Position.getX()), new Mesure(0),
              coteDTO.Largeur.soustraire(separateur.Position.getX()),coteDTO.Hauteur,separateur.EstSelectionne);        
        } else {
            drawLigne(g,separateur.Position.getX(), new Mesure(0),
                     separateur.Position.getX(),coteDTO.Hauteur,separateur.EstSelectionne);
        }
    }
    
    public void drawAccessoire(Graphics g, CoteDTO coteDTO, AccessoireDTO accessoire) {
        Mesure largeur = new Mesure();
        Mesure hauteur = new Mesure();
        Mesure ecartMoulure = new Mesure();
        Mesure ecartTotal = new Mesure();
        PointMesure position = new PointMesure();
        PointMesure positionFin = new PointMesure();
        boolean estSelectionne = false;
        
        switch(accessoire.Type){
            case RetourAir:
                RetourAirDTO retourAir = (RetourAirDTO)accessoire;
                largeur = retourAir.Largeur;
                hauteur = retourAir.Hauteur;
                position = retourAir.Position;
                positionFin = new PointMesure(position.getX().ajouter(largeur),position.getY());
                estSelectionne = retourAir.EstSelectionne;
                break;
            case Fenetre:
                FenetreDTO fenetre = (FenetreDTO)accessoire;
                largeur = fenetre.Largeur;
                hauteur = fenetre.Hauteur;
                ecartMoulure = fenetre.EcartMoulure;
                ecartTotal = ecartMoulure.multiplier(new Mesure (2));
                position = fenetre.Position;
                positionFin = new PointMesure(position.getX().ajouter(largeur),position.getY());
                estSelectionne = fenetre.EstSelectionne;
                break;
            case PriseElectrique:
                PriseElectriqueDTO priseElectrique = (PriseElectriqueDTO)accessoire;
                largeur = priseElectrique.Largeur;
                hauteur = priseElectrique.Hauteur;
                position = priseElectrique.Position;
                positionFin = new PointMesure(position.getX().ajouter(largeur),position.getY());
                estSelectionne = priseElectrique.EstSelectionne;
                break;
            
            case Porte:
                PorteDTO porte = (PorteDTO)accessoire;
                largeur = porte.Largeur;
                hauteur = porte.Hauteur;
                position = porte.Position;
                positionFin = new PointMesure(position.getX().ajouter(largeur),position.getY());
                estSelectionne = porte.EstSelectionne;
                break;
        }
        
        
        if(controleur.getEstInterieur()) {
            if(accessoire.Type == TypeObjet.Fenetre) {
                PointMesure pointInverse = inverserPoint(positionFin);
                drawRectangle(g, pointInverse.getX().soustraire(ecartMoulure),
                                 pointInverse.getY().soustraire(ecartMoulure),
                                 largeur.ajouter(ecartTotal),
                                 hauteur.ajouter(ecartTotal), estSelectionne);
            }
            
            PointMesure pointInverse = inverserPoint(positionFin);
            drawRectangle(g, pointInverse.getX(), pointInverse.getY(), largeur, hauteur, estSelectionne);
        } 
        else if(accessoire.Type != TypeObjet.RetourAir && accessoire.Type != TypeObjet.PriseElectrique) {
            if(accessoire.Type == TypeObjet.Fenetre) {
                drawRectangle(g, position.getX().soustraire(ecartMoulure),
                                 position.getY().soustraire(ecartMoulure),
                                 largeur.ajouter(ecartTotal),
                                 hauteur.ajouter(ecartTotal),
                                 estSelectionne);
            }

            drawRectangle(g, position.getX(), position.getY(), largeur, hauteur, estSelectionne);
        }           
    }
}

