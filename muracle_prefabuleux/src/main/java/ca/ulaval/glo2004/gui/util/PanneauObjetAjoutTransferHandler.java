package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.TypeObjet;
import ca.ulaval.glo2004.gui.PanneauVue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;


public class PanneauObjetAjoutTransferHandler extends TransferHandler {
    DataFlavor draggableFlavor = DataFlavor.stringFlavor;
    
    @Override
    public boolean importData(TransferSupport support) {
        TypeObjet type;
        JComponent component = (JComponent) support.getComponent();
        Transferable t = support.getTransferable();
        
        if (canImport(component, t.getTransferDataFlavors())) {
            PanneauVue panneauVue = (PanneauVue) component;
            
            try {
                type =  (TypeObjet) t.getTransferData(draggableFlavor);
                
                panneauVue.ajouterObjet(type, support.getDropLocation().getDropPoint());
                
                return true;
            } catch (UnsupportedFlavorException ufe) {
                System.out.println("importData: unsupported data flavor");
            } catch (IOException ioe) {
                System.out.println("importData: I/O exception");
            }
        }
        
        return false;
    }
    
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (DataFlavor flavor : flavors) {
            if (draggableFlavor.equals(flavor)) {
                return true;
            }
        }
        
        return false;
    }
}
