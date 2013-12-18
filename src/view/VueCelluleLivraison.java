package view;

import model.Livraison;

import javax.swing.*;
import java.awt.*;

class VueCelluleLivraison extends JLabel implements ListCellRenderer<Object> {
    private static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Color BACKGROUND_SELECTED = VuePlan.COULEUR_BACKGROUND ;//new Color(36, 61, 217);


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
        }
        else {
            Livraison livraison = (Livraison) value;
            repr = String.format("Livraison %s | Client %s", Integer.toString(livraison.getId()),
                    Integer.toString(livraison.getNoClient()));
            if (livraison.isHorsHoraire()) {
                setForeground(Color.RED);
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