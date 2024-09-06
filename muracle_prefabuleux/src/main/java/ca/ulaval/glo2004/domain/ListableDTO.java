package ca.ulaval.glo2004.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ListableDTO {   
    
    public List<ProprieteDTO> getProprietesModifiablesUtilisateur() {        
        return new ArrayList<>();
    }
    
    /**
     * Utilise la réflection pour obtenir tous ses champs publiques et les retourne en tant que ProprieteDTO
     * @return les champs publiques de la classe
     */
    public List<ProprieteDTO> getProprietes() {
        List<ProprieteDTO> proprietes = new ArrayList<>();

        Field[] champs = this.getClass().getDeclaredFields();
        
        for(Field champ : champs) {
            try {
                if(champ.canAccess(this)) { 
                    proprietes.add(new ProprieteDTO(champ.getName(), champ.get(this)));
                }
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
        
        return proprietes;
    }    
    
    /**
     * Utilise la réflection pour mettre à jour la valeur des propriétés passées en paramètre
     * @param proprietes propriétés à mettre à jour
     * @throws IllegalAccessException si un champ à modifié n'est pas accessible en raison d'un modifier (private/protected)
     * @throws IllegalArgumentException si le type de la valeur n'est pas le même que le type du champ
     * @throws NoSuchFieldException si le champ n'existe pas
     */
    public void setProprietes(List<ProprieteDTO> proprietes) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Class classe = this.getClass();
        
        for (ProprieteDTO propriete : proprietes) {
            Field field = classe.getDeclaredField(propriete.getNom());

            field.set(this, propriete.getValeur());
        }
    }    
}
