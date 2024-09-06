
package ca.ulaval.glo2004.domain.util;
import ca.ulaval.glo2004.domain.util.Mesure;

public class ConvertisseurPixelsPouces {
    
    
    
    public  ConvertisseurPixelsPouces()
            
    {
        
    }
    
  public int poucesVersPixels(Mesure pouces)
  {
      int mesureConverti;
      return  mesureConverti = (int) (pouces.getTotal() * 3);
  }
 
  public Mesure pixelsVersPouces(int pixels)
  {
      int mesureConverti = pixels/3;
      int mesureFraction = pixels%3;
      return  new Mesure(mesureConverti, mesureFraction,3);
  }

  public PointMesure poucesVersPixels(PointMesure coordPouces)
  {
      Mesure convertisseur = new Mesure(3);
      Mesure mesureConvertiX = coordPouces.getX().multiplier(convertisseur);       
      Mesure mesureConvertiY = coordPouces.getY().multiplier(convertisseur);
        
      return new PointMesure(mesureConvertiX,mesureConvertiY);
  }
  public PointMesure pixelsVersPouces(PointMesure coordPixels)
  {
      Mesure convertisseur = new Mesure(0,1,3);
      Mesure mesureConvertiX = coordPixels.getX().multiplier(convertisseur);       
      Mesure mesureConvertiY = coordPixels.getY().multiplier(convertisseur);
        
      return new PointMesure(mesureConvertiX,mesureConvertiY);
  }
  
}
