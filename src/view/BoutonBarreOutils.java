package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Bouton de la barre d'outils.
 *
 * @see view.BarreOutils
 */
public class BoutonBarreOutils extends JButton {
    private static final Insets margins = new Insets(0, 0, 0, 0);
    private static final File imagesFolder = new File("img");

    public BoutonBarreOutils(Icon icon) {
        super(icon);
        setMargin(margins);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
    }

    public BoutonBarreOutils(String imageFile) {
        this(new ImageIcon(new File(imagesFolder, imageFile).getAbsolutePath()));
    }

    public BoutonBarreOutils(String imageFile, String text) {
        this(new ImageIcon(new File(imagesFolder, imageFile).getAbsolutePath()));
        setToolTipText(text);
        setText(text);
    }
}