package view;

import javax.swing.*;
import java.awt.*;

public class ZoneNotification extends JLabel {

    private Font m_font;

    private static int HAUTEUR_DEFAUT = 35;
    private static int LARGEUR_DEFAUT = 350;

    private static Color COULEUR_INFO = new Color(60, 140, 235);
    private static Color COULEUR_SUCCES = new Color(42, 210, 48);
    private static Color COULEUR_ERREUR = new Color(230, 68, 25);

    public ZoneNotification() {
        m_font = new Font("Arial", Font.BOLD, 14);
        setFont(m_font);
        setForeground(Color.white);
        setOpaque(true);
        setBackground(COULEUR_INFO);

        setPreferredSize(new Dimension(LARGEUR_DEFAUT, HAUTEUR_DEFAUT));
    }

    public void setSuccessMessage(String text) {
        setBackground(COULEUR_SUCCES);
        setText("  " + text);
    }

    public void setErrorMessage(String text) {
        setBackground(COULEUR_ERREUR);
        setText("  " + text);
    }

    public void setInfoMessage(String text) {
        setBackground(COULEUR_INFO);
        setText("  " + text);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paintComponent(g2);
    }
}
