package ca.ulaval.glo2004;

import ca.ulaval.glo2004.gui.MainWindow;
import javax.swing.JFrame;

public class App {
    
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.pack();
        mainWindow.setExtendedState(mainWindow.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mainWindow.setVisible(true); 
    }
}

