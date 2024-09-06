package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.util.PointMesure;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class PointCellRenderer implements TableCellRenderer {
    JLabel label;
    
    public PointCellRenderer() {
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.TRAILING);
        
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        PointMesure point = (PointMesure) value;
        
        label.setText(String.format("(%d, %d/%d), (%d, %d/%d)",
                                    point.getX().getEntier(), 
                                    point.getX().getNumerateur(), 
                                    point.getX().getDenominateur(),
                                    point.getY().getEntier(),
                                    point.getY().getNumerateur(),
                                    point.getY().getDenominateur()));
        
        return label;
    }
}
