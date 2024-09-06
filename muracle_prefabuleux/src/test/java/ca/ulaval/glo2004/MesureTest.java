package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domain.util.Mesure;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class MesureTest {
    
    @Test
    public void initDouble_Standard() {
       Mesure resultat = new Mesure(2.25);

       assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(2, 1, 4));    
    }
    
    @Test
    public void initDouble_Complexe() {
       Mesure resultat = new Mesure(0.33);

       assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(0, 33, 100));    
    }
    
    @Test
    public void initDouble_LongueFraction() {
       Mesure resultat = new Mesure(1.2575);

       assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(1, 103, 400));    
    }
    
    @Test
    public void initDouble_Zero() {
       Mesure resultat = new Mesure(0);

       assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure());    
    }
    
     @Test
     public void ajouter_Standard() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(5, 6, 7);

        Mesure resultat = lhs.ajouter(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(13, 46, 21));    
     }
     
     @Test
     public void ajouter_Zero() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(0, 0, 1);

        Mesure resultat = lhs.ajouter(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(8, 4, 3));    
     }
     
     @Test
     public void ajouter_NumerateurZero_DenominateurDifferentUn() {
        Mesure lhs = new Mesure(4, 0, 2);
        Mesure rhs = new Mesure(2, 5, 4);

        Mesure resultat = lhs.ajouter(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(6, 5, 4));    
     }
     
     @Test
     public void ajouter_EntierNegatif() {
        Mesure lhs = new Mesure(4, 1, 4);
        Mesure rhs = new Mesure(-2, 3, 4);

        Mesure resultat = lhs.ajouter(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(1, 1, 2));    
     }
     
     @Test
     public void ajouter_EntierNegatifFractionAvecEntier() {
        Mesure lhs = new Mesure(10, 1, 4);
        Mesure rhs = new Mesure(-2, 18, 4);

        Mesure resultat = lhs.ajouter(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(3, 3, 4));    
     }
     
     @Test
     public void ajouter_DeuxEntiersNegatifs() {
        Mesure lhs = new Mesure(-4, 1, 4);
        Mesure rhs = new Mesure(-2, 3, 4);

        Mesure resultat = lhs.ajouter(rhs);

        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(-7, 0, 1));    
     }
     
     @Test
     public void ajouter_FractionNegative() {
        Mesure lhs = new Mesure(2, 3, 4);
        Mesure rhs = new Mesure(0, -1, 2);

        Mesure resultat = lhs.ajouter(rhs);

        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(2, 1, 4));    
     }
     
     @Test
     public void soustraire_Standard() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(5, 6, 7);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(3, 10, 21));      
     }
     
     @Test
     public void soustraire_Zero() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(0, 0, 1);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(8, 4, 3));      
     }
     
     @Test
     public void soustraire_EntierSeul() {
        Mesure lhs = new Mesure(8, 12, 16);
        Mesure rhs = new Mesure(4, 0, 1);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(4, 12, 16));      
     }
     
     @Test
     public void soustraire_FractionSeule() {
        Mesure lhs = new Mesure(8, 12, 16);
        Mesure rhs = new Mesure(0, 2, 32);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(8, 11, 16));      
     }
     
     @Test
     public void soustraire_FractionReductible() {
        Mesure lhs = new Mesure(8, 12, 16);
        Mesure rhs = new Mesure(4, 2, 32);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(4, 11, 16));      
     }
     
     @Test
     public void soustraire_EntierNegatif() {
        Mesure lhs = new Mesure(8, 12, 16);
        Mesure rhs = new Mesure(-4, 2, 32);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(12, 13, 16));      
     }
     
     @Test
     public void soustraire_FractionNegative() {
        Mesure lhs = new Mesure(8, 12, 16);
        Mesure rhs = new Mesure(0, -2, 32);
         
        Mesure resultat = lhs.soustraire(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(8, 13, 16));      
     }
     
     @Test
     public void multiplier_Standard() {
        Mesure lhs = new Mesure(8, 4, 3);//9.33 *
        Mesure rhs = new Mesure(5, 6, 7);//5.86 = 54.66667 
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(54, 2, 3));
     }
     
     @Test
     public void multiplier_Fraction() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(0, 1, 2);
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(4, 2, 3));
     }
     
     @Test
     public void multiplier_Zero() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(0, 0, 1);
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(0, 0, 1));
     }
     
     @Test
     public void multiplier_DeuxFractions() {
        Mesure lhs = new Mesure(0, 2, 3);
        Mesure rhs = new Mesure(0, 6, 7);
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(0, 4, 7));
     }
     
     @Test
     public void multiplier_EntierNegatif() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(-5, 6, 7);
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(-54, -2, 3));
     }
     
     @Test
     public void multiplier_FractionNegatif() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(0, -1, 2);
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(-4, -2, 3));
     }
     
     @Test
     public void multiplier_DeuxNegatifs() {
        Mesure lhs = new Mesure(-8, 4, 3);
        Mesure rhs = new Mesure(-5, 6, 7);
         
        Mesure resultat = lhs.multiplier(rhs);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(54, 2, 3));
     }
     
     @Test
     public void reduireFraction_Standard() {
        Mesure resultat = Mesure.reduireFraction(10, 5, 10);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(10, 1, 2));
     }
     
     @Test
     public void reduireFraction_StandardNbPremier() {
        Mesure resultat = Mesure.reduireFraction(10, 7, 35);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(10, 1, 5));
     }
     
     @Test
     public void reduireFraction_Irreductible() {
        Mesure resultat = Mesure.reduireFraction(10, 7, 19);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(10, 7, 19));
     }
     
     @Test
     public void reduireFraction_FractionPlusGrandeUn() {
        Mesure resultat = Mesure.reduireFraction(10, 100, 12);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(18, 1, 3));
     }
     
     @Test
     public void reduireFraction_FractionNegative() {
        Mesure resultat = Mesure.reduireFraction(-10, -100, 12);
         
        assertThat(resultat).isEquivalentAccordingToCompareTo(new Mesure(-18, -1, 3));
     }
         
     @Test
     public void compareTo_PlusGrand() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(5, 6, 7);
         
        int resultat = lhs.compareTo(rhs);
         
        assertThat(resultat).isGreaterThan(0);
     }
     
     @Test
     public void compareTo_PlusPetit() {
        Mesure lhs = new Mesure(8, 4, 3);
        Mesure rhs = new Mesure(10, 2, 5);
         
        int resultat = lhs.compareTo(rhs);
         
        assertThat(resultat).isLessThan(0);
     }
     
     @Test
     public void compareTo_Egal() {
        Mesure lhs = new Mesure(10, 2, 5);
        Mesure rhs = new Mesure(10, 2, 5);
         
        int resultat = lhs.compareTo(rhs);
         
        assertThat(resultat).isEqualTo(0);
     }
     
     @Test
     public void compareTo_PlusPetitFraction() {
        Mesure lhs = new Mesure(10, 1, 5);
        Mesure rhs = new Mesure(10, 2, 5);
         
        int resultat = lhs.compareTo(rhs);
         
        assertThat(resultat).isLessThan(0);
     }
     
     @Test
     public void getTotal_Standard() {
        Mesure lhs = new Mesure(5, 2, 4);
         
        double resultat = lhs.getTotal();
         
        assertThat(resultat).isEqualTo(5.5);
     }
     
     @Test
     public void getTotal_TotalNegatif() {
        Mesure lhs = new Mesure(-5, -2, 4);
         
        double resultat = lhs.getTotal();
         
        assertThat(resultat).isEqualTo(-5.5);
     }
    
     @Test
     public void getTotal_EntierNegatif() {
        Mesure lhs = new Mesure(-5, 2, 4);
         
        double resultat = lhs.getTotal();
         
        assertThat(resultat).isEqualTo(-5.5);
     }
     
     @Test
     public void getTotal_NumerateurNegatif() {
        Mesure lhs = new Mesure(5, -2, 4);
         
        double resultat = lhs.getTotal();
         
        assertThat(resultat).isEqualTo(-5.5);
     }
}
