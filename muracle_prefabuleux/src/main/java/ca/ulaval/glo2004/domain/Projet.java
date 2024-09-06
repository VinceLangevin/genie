package ca.ulaval.glo2004.domain;

import java.io.*;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;




public final class Projet {
    private final String EXTENSION_FICHIER = ".mur";
    
    private String cheminFichier;
    private Salle salle;
    
    public Projet() {
        salle = new Salle();
        cheminFichier = "";
    }
    
    public Projet(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        
        this.chargerFichier(cheminFichier);
    }
    
    public Salle getSalle() {
        return salle;
    }
    
    public String getCheminFichier() {
        return cheminFichier;
    }
    
    public void setCheminFichier(String cheminFichier) {
        if(!cheminFichier.endsWith(EXTENSION_FICHIER))
            cheminFichier += EXTENSION_FICHIER;
        
        this.cheminFichier = cheminFichier;
    }
    
    public void enregistrer() {
        if(!cheminFichier.isBlank())
        {
            FileOutputStream fileOut = null;
            ObjectOutputStream out = null;
        
            try {
                fileOut = new FileOutputStream(cheminFichier);
                out = new ObjectOutputStream(fileOut);

                out.writeObject(salle);
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                close(out);
                close(fileOut);
            }  
        }       
    }
    
    private void chargerFichier(String cheminFichier) {
        if(cheminFichier.isBlank())
            throw new IllegalArgumentException("Il n'est pas possible de charger un chemin vide.");
        
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        
        try {
            fileIn = new FileInputStream(cheminFichier);
            in = new ObjectInputStream(fileIn);
            
            this.salle = (Salle)in.readObject();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(in);
            close(fileIn);
        }
    }
    
    public String obtenirEtat() {
        String etat = "";
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream out = null;
        
        try {
            byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
            
            out.writeObject(salle);
            
            etat = Base64.getEncoder().encodeToString(byteOut.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            close(out);
            close(byteOut);
        }
        
        return etat;
    }
    
    public void chargerEtat(String etat) {
        ByteArrayInputStream bytesIn = null;
        ObjectInputStream in = null;
        
        try 
        {
            byte [] donnees = Base64.getDecoder().decode(etat);
            bytesIn = new ByteArrayInputStream(donnees);
            in = new ObjectInputStream(bytesIn);
            
            this.salle = (Salle) in.readObject();            

        } catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
           close(in);
           close(bytesIn);
        }
    }
    

    

    
    //https://stackoverflow.com/questions/2699209/java-io-ugly-try-finally-block
    private void close(Closeable stream) {
        if(stream == null)
            return;
        
        try {
            stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}
