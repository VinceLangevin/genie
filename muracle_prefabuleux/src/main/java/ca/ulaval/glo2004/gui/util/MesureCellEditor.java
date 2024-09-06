package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.util.Mesure;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;


public class MesureCellEditor extends DefaultCellEditor implements TableCellEditor {
    Mesure mesure; 
    
    public MesureCellEditor() {
        super(new JTextField());
        
        setClickCountToStart(2);       
    }
    
    @Override
    public Object getCellEditorValue() {
        return mesure;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        mesure = (Mesure)value;
        
        JTextField champTexte = (JTextField) getComponent();
        champTexte.setText(formatterMesure());
        
        return champTexte;
    }
    
    @Override
    public boolean stopCellEditing() {
        boolean estValide = false;
        JTextField champTexte = (JTextField) getComponent();
        String entree = champTexte.getText();
        
        if(entree.matches("^[\\[]?[0-9,; ]+[\\]]?$")) {
            List<Integer> nombres = extraireNombres(entree);
            
            if(nombres.size() != 3) {
                afficherAvertissementFormatage();
            } else if(nombres.get(2) <= 0) {
                JOptionPane.showMessageDialog(null, "Le dénominateur ne peut pas être nul ou négatif", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                Mesure nouvelMesure = new Mesure(nombres.get(0), nombres.get(1), nombres.get(2));
                
                if(nouvelMesure.compareTo(new Mesure(0)) < 0) {
                    JOptionPane.showMessageDialog(null, "La mesure ne peut pas être négative", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    mesure = nouvelMesure;
                    estValide = true;
                }
            }
        } else {
            afficherAvertissementFormatage();
        }
        
        champTexte.setText(formatterMesure());
        
        return estValide && super.stopCellEditing();
    }
    
    private void afficherAvertissementFormatage() {
        JOptionPane.showMessageDialog(null, "La valeur d'une mesure devrait respecter le format [entier, numérateur, dénominateur]", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    private List<Integer> extraireNombres(String input) {
        Pattern intPattern = Pattern.compile("-?\\d+");
        Matcher matcher = intPattern.matcher(input);
        
        List<Integer> intList = new ArrayList<>();
        while(matcher.find()) {
            intList.add(Integer.parseInt(matcher.group()));
        }
        
        return intList;
    }
    
    private String formatterMesure() {
        return String.format("[%d, %d, %d]", mesure.getEntier(), mesure.getNumerateur(), mesure.getDenominateur());
    }
}
