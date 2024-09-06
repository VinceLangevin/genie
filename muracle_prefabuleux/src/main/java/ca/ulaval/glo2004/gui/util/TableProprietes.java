package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.util.Mesure;
import ca.ulaval.glo2004.domain.util.PointMesure;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TableProprietes extends JTable {
    private Class classeModifie; 
    
    public TableProprietes() {
        initialiserGestionCell();
    }
    
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        classeModifie = null;
        int modelColumn = convertColumnIndexToModel(column);
        if (modelColumn == 1) {
            Object objet = getModel().getValueAt(row, modelColumn);
            
            if(objet != null) {
                return getDefaultRenderer(objet.getClass());
            }
        }
        
        return super.getCellRenderer(row, column);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        TableCellEditor ret;
        classeModifie = null;
        int modelColumn = convertColumnIndexToModel(column);
        if (modelColumn == 1) {
            Object objet = getModel().getValueAt(row, modelColumn);
            
            if(objet != null) {
                return getDefaultEditor(objet.getClass());
            }
        } 
        
        ret = super.getCellEditor(row, column);
        
        return ret;
    }
    
    @Override
    public Class getColumnClass(int column) {
        return classeModifie != null ? classeModifie : super.getColumnClass(column);
    }
    
    private void initialiserGestionCell() {        
        this.setDefaultEditor(Mesure.class, new MesureCellEditor());
        this.setDefaultRenderer(Mesure.class, new MesureCellRenderer());
        
        this.setDefaultEditor(PointMesure.class, new PointCellEditor());
        this.setDefaultRenderer(PointMesure.class, new PointCellRenderer());
    }
}
