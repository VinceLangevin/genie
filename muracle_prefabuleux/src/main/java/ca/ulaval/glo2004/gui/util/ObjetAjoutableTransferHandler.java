package ca.ulaval.glo2004.gui.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;


public class ObjetAjoutableTransferHandler extends TransferHandler {
    ObjetAjoutable source;
   
    
    @Override
    protected Transferable createTransferable(JComponent component) {
        source =  (ObjetAjoutable) component;
        return new ObjetAjoutableTransferable(source.getType());
    }
    
    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }
    
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        source = null;
    }
    
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {        
        return false;
    }
}
