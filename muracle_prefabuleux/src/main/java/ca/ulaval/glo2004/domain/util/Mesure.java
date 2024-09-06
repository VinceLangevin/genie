package ca.ulaval.glo2004.domain.util;

/**
 * Une mesure en pouces en trois parties : un entier et une fraction qui est divisée en numérateur et dénominateur
 * 
 * Les mesures négatives sont permises.
 * @author Anthony Sirois
 */
public final class Mesure implements java.io.Serializable, Comparable<Mesure> {
    private final int DENOMINATEUR_MAX = 8192;
    private final int NB_MAX_DECIMAL = 8;

    private int entier;
    private int numerateur;
    private int denominateur;
    
    public Mesure() {
        this(0);
    }
    
    public Mesure(int entier) {
        this(entier, 0, 1);
    }
    
    public Mesure(int entier, int numerateur, int denominateur) {
        init(entier, numerateur, denominateur);
    }
    
    public Mesure(double nombre) {
        String nombreTexte = String.valueOf(nombre);
        int nbDecimals = Math.min(nombreTexte.length() - 1 - nombreTexte.indexOf('.'), NB_MAX_DECIMAL);        

        int denom = 1;
        for(int i = 0; i < nbDecimals; i++){
           nombre *= 10;
           denom *= 10;
        }
        int num = (int) Math.round(nombre);

        Mesure resultat = reduireFraction(0, num, denom);
        
        init(resultat.getEntier(), resultat.getNumerateur(), resultat.getDenominateur());
    }
    
    public Mesure(Mesure m) {
        this(m.getEntier(), m.getNumerateur(), m.getDenominateur());
    }
    
    private void init(int entier, int numerateur, int denominateur) {
        if(denominateur <= 0)
           throw new IllegalArgumentException(String.format("Le dénominateur ne peut pas être négatif ou égale à 0; %d", denominateur));
        
        this.entier = entier;
        this.numerateur = numerateur;
        this.denominateur = denominateur;
        
        if(this.entier > 0 && this.numerateur < 0) this.entier *= -1;
        if(this.numerateur > 0 && this.entier < 0) this.numerateur *= -1;
        
        if(denominateur > DENOMINATEUR_MAX) {
            this.numerateur = (int)((double) numerateur / (double) denominateur * DENOMINATEUR_MAX);
            this.denominateur = DENOMINATEUR_MAX;
        }
    }
    
    public int getEntier() {
        return entier;
    }  
    
    public int getNumerateur() {
        return numerateur;
    }
        
    public int getDenominateur() {
        return denominateur;
    }
    
    public Mesure ajouter(Mesure autre) {        
        int resultatEntier = this.entier + autre.entier;
        int resultatNumerateur = (this.numerateur * autre.getDenominateur()) + (autre.getNumerateur() * this.denominateur);
        int resultatDenominateur = this.denominateur * autre.denominateur;

        return reduireFraction(resultatEntier, resultatNumerateur, resultatDenominateur);
    }
    
    public Mesure soustraire(Mesure autre) {       
        int resultatEntier = this.entier - autre.entier;
        int resultatNumerateur = (this.numerateur * autre.denominateur) - (autre.numerateur * this.denominateur);
        int resultatDenominateur = this.denominateur * autre.denominateur;
        
        return reduireFraction(resultatEntier, resultatNumerateur, resultatDenominateur);
    }
    
    public Mesure multiplier(Mesure autre) {
        int resultatEntier = this.entier * autre.getEntier();
        
        int multiplicationAutreEntierNumerateur = autre.getEntier() * this.numerateur * autre.getDenominateur();
        int multiplicationEntierAutreNumerateur = this.entier * autre.getNumerateur() * this.denominateur;
        int multiplicationFractions = this.numerateur * autre.getNumerateur();
                
        int resultatNumerateur = multiplicationAutreEntierNumerateur + multiplicationEntierAutreNumerateur + multiplicationFractions;
        int resultatDenominateur = this.denominateur * autre.getDenominateur();
        
        return reduireFraction(resultatEntier, resultatNumerateur, resultatDenominateur);
    }
    
    @Override
    public int compareTo(Mesure autre) {
        double difference = (this.getTotal() - autre.getTotal());
        
        //Le cast en int arrondi à 0 si la différence entre les deux valeurs est 0 < x < 1
        if(difference == 0)
            return 0;
        else if(difference > 0)
            return 1;
        else
            return -1;
    }
    
    public double getTotal() {       
        return this.entier + ((double)this.numerateur / (double)this.denominateur);
    }
    
    public static Mesure reduireFraction(int entier, int numerateur, int denominateur) {    
        entier += numerateur / denominateur;
        numerateur = numerateur % denominateur;
        
        int denominateurCommun = getPlusGrandDenominateurCommun(Math.abs(numerateur), denominateur);
        
        numerateur /= denominateurCommun;
        denominateur /= denominateurCommun;
        
        //balancer signes 
        if(entier < 0 && numerateur > 0) {
            entier += 1;
            numerateur -= denominateur;
        } else if(entier > 0 && numerateur < 0) {
            entier -= 1;
            numerateur += denominateur;
        }
        
        return new Mesure(entier, numerateur, denominateur);
    }
    
    //https://stackoverflow.com/questions/6618994/simplifying-fractions-in-java
    private static int getPlusGrandDenominateurCommun(int a, int b) {
        if (b == 0) return a;
        return getPlusGrandDenominateurCommun(b, a % b);
    }
    
    @Override
    public String toString() {
        return String.format("%d %d/%d", this.entier, this.numerateur, this.denominateur);
    }
    
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
