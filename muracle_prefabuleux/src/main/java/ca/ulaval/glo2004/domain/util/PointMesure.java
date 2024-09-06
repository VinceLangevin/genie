package ca.ulaval.glo2004.domain.util;

public class PointMesure implements java.io.Serializable {
    private Mesure x;
    private Mesure y;
    
    public PointMesure() {
        this.x = new Mesure();
        this.y = new Mesure();
    }
    
    public PointMesure(Mesure x, Mesure y) {
        this.x = new Mesure(x);
        this.y = new Mesure(y);
    }
    
    public PointMesure(PointMesure p) {
        this(p.getX(), p.getY());
    }
    
    public Mesure getX() {
        return x;
    }
    
    public Mesure getY() {
        return y;
    }
    
    public void setX(Mesure x) {
        this.x = x;
    }
    
    public void setY(Mesure y) {
        this.y = y;
    }
    
    public void setLocation(Mesure x, Mesure y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLocation(PointMesure p) {
        this.x = new Mesure(p.x);
        this.y = new Mesure(p.y);       
    }
    
    public void translate(Mesure dx, Mesure dy) {
        this.x = this.x.ajouter(dx);
        this.y = this.y.ajouter(dy);
    }
    
    public void translate(PointMesure deltaP) {
        this.x = this.x.ajouter(deltaP.getX());
        this.y = this.y.ajouter(deltaP.getY());
    }
    
    /**
     * Déplace le point par les distances passées en paramètre.
     * Le point s'arrêtera au périmètre du rectangle créé à partir de positionMin et positionMax au lieu de les dépasser.
     * @param dx distance parcourue sur l'axe des X
     * @param dy distance parcourue sur l'axe des Y
     * @param positionMin coin haut à gauche du rectangle de limite de déplacement
     * @param positionMax coin bas à droite du rectangle de limite de déplacement
     */
    public void translate(Mesure dx, Mesure dy, PointMesure positionMin, PointMesure positionMax) {
        translate(dx, dy);
        
        if(this.x.compareTo(positionMin.getX()) < 0)
            this.x = positionMin.getX();
        else if(this.x.compareTo(positionMax.getX()) > 0)
            this.x = positionMax.getX();
        
        if(this.y.compareTo(positionMin.getY()) < 0)
            this.y = positionMin.getY();
        else if(this.y.compareTo(positionMax.getY()) > 0)
            this.y = positionMax.getY();
    }
    
    @Override
    public String toString() {
        return String.format("(%s, %s)", this.x.toString(), this.y.toString());
    }
}
