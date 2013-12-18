package view;

import model.Livraison;

import javax.swing.*;
import java.awt.*;

/**
 * Classe de rendu d'un &eacute;l&eacute;ment de la liste des livraisons. Affiche les livraisons en retard en surbrillance.
 */
class VueCelluleLivraison extends JLabel implements ListCellRenderer<Object> {
    private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BOLD_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Color BACKGROUND_SELECTED = VuePlan.COULEUR_BACKGROUND;//new Color(36, 61, 217);


    public VueCelluleLivraison() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        setBackground(Color.WHITE);
        setFont(DEFAULT_FONT);
        setForeground(Color.BLACK);
        String repr;
        if (value == null) {
            repr = "Aucune livraison n'a été chargée";
        } else {
            Livraison livraison = (Livraison) value;
            repr = String.format("%s | Livraison %s", livraison.getPlage().toString(),
                    Integer.toString(livraison.getId()));
            if (livraison.isHorsHoraire()) {
                setForeground(Color.RED);
                setFont(BOLD_FONT);
            }
        }

        if (cellHasFocus) {
            setBackground(BACKGROUND_SELECTED);
            setForeground(Color.WHITE);
        }

        setText(repr);

        return this;
    }
}