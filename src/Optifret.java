import controller.Controleur;

import javax.swing.*;

public class Optifret {

    // Point d'entrée de l'application:
    public static void main(String[] args) {
        // Paramètre Swing pour utiliser une apparence native
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Interface native non gérée, fallback sur l'interface Swing par défaut.");
        }

        new Controleur();
    }


}
