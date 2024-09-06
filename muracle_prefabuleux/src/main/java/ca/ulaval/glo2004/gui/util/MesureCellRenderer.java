package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.util.Mesure;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MesureCellRenderer implements TableCellRenderer {
    JLabel label;
    
    public MesureCellRenderer() {
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.TRAILING);
                
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Mesure mesure = (Mesure) value;
        
        label.setText(String.format("%d, %d/%d", mesure.getEntier(), mesure.getNumerateur(), mesure.getDenominateur()));
        
        return label;
    }
}
