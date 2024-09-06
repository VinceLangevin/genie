package ca.ulaval.glo2004.gui.util;

import ca.ulaval.glo2004.domain.TypeObjet;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class ObjetAjoutableTransferable implements Transferable {
    private final DataFlavor flavor = DataFlavor.stringFlavor;
    private final TypeObjet type;

        public ObjetAjoutableTransferable(TypeObjet type) {
            this.type = type;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
              throw new UnsupportedFlavorException(flavor);
            }
          
            return type;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { flavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return this.flavor.equals(flavor);
        }
}
