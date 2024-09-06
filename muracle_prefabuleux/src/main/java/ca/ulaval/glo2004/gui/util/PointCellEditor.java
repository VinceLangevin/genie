package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
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


public class PointCellEditor extends DefaultCellEditor implements TableCellEditor {
    PointMesure point;
    
    public PointCellEditor() {
        super(new JTextField());
        
        setClickCountToStart(2);       
    }
    
     @Override
    public Object getCellEditorValue() {
        return point;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        point = (PointMesure)value;
        
        JTextField champTexte = (JTextField) getComponent();
        champTexte.setText(formatterPoint());
        
        return champTexte;
    }
    
    @Override
    public boolean stopCellEditing() {
        boolean estValide = false;
        JTextField champTexte = (JTextField) getComponent();
        String entree = champTexte.getText();
        
        if(entree.matches("^[\\(]?(?:[\\[]?(?:[0-9]+[ ,;]*)+[\\]]?[ ,;]*)+[\\)]?$")) {
            List<Integer> nombres = extraireNombres(entree);
            
            if(nombres.size() != 6) {
                afficherAvertissementFormatage();
            } else if(nombres.get(2) <= 0 || nombres.get(5) <= 0) {
                JOptionPane.showMessageDialog(null, "Un dénominateur ne peut pas être nul ou négatif", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                PointMesure nouveauPoint = new PointMesure(new Mesure(nombres.get(0), nombres.get(1), nombres.get(2)),
                                               new Mesure(nombres.get(3), nombres.get(4), nombres.get(5)));
                
                if(nouveauPoint.getX().compareTo(new Mesure(0)) < 0 || nouveauPoint.getY().compareTo(new Mesure(0)) < 0) {
                    JOptionPane.showMessageDialog(null, "Le point ne peut pas être négatif", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    point = nouveauPoint;
                    estValide = true;
                }
            }
        } else {
            afficherAvertissementFormatage();
        }
        
        champTexte.setText(formatterPoint());
        
        return estValide && super.stopCellEditing();
    }
    
    
    private void afficherAvertissementFormatage() {
        JOptionPane.showMessageDialog(null, "La valeur d'un point devrait respecter le format ([x.entier, x.numérateur, x.dénominateur], [y.entier, y.numérateur, y.dénominateur])", "Erreur", JOptionPane.ERROR_MESSAGE);
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
    
    private String formatterPoint() {
        return String.format("([%d, %d, %d], [%d, %d, %d])", 
                             point.getX().getEntier(), 
                             point.getX().getNumerateur(), 
                             point.getX().getDenominateur(),
                             point.getY().getEntier(),
                             point.getY().getNumerateur(),
                             point.getY().getDenominateur());
    }
}
