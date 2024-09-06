package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.ProprieteDTO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class TableProprietesModel extends AbstractTableModel {
    private final int COLONNE_NOM = 0;
    private final int COLONNE_VALEUR = 1;
    
    private final List<ProprieteDTO> proprietes;

    public TableProprietesModel() {
        proprietes = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return proprietes.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }
        
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case COLONNE_NOM: return "Nom";
            case COLONNE_VALEUR: return "Valeur";
        }
        
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == COLONNE_NOM)
            return proprietes.get(rowIndex).getNomAffichage();
        else if(columnIndex == COLONNE_VALEUR)
            return proprietes.get(rowIndex).getValeur();
        
        return null;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLONNE_VALEUR && !proprietes.get(rowIndex).getLectureSeule();
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == COLONNE_VALEUR) {            
            Class test = proprietes.get(rowIndex).getValeur().getClass();
            if(test == Double.class) {
                aValue = Double.valueOf(aValue.toString());
            }
            
            proprietes.get(rowIndex).setValeur(aValue);
            
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
    
    public void clear() {
        if(!proprietes.isEmpty()) {
            int derniereRow = proprietes.size() - 1;
            proprietes.clear();

            fireTableRowsDeleted(0, derniereRow);
        }
    }
    
    public void ajouter(ProprieteDTO propriete) {
        this.proprietes.add(propriete);
        
        int row = proprietes.indexOf(propriete);
        fireTableRowsInserted(row, row);
    }
    
    public void ajouter(List<ProprieteDTO> proprietes) {
        if(!proprietes.isEmpty()) {
            int derniereRow = this.proprietes.size();
            this.proprietes.addAll(proprietes);
            
            fireTableRowsDeleted(derniereRow, derniereRow + proprietes.size() - 1);
        }
    }
    
    public List<ProprieteDTO> getProprietes() {
        return proprietes;
    }
}
