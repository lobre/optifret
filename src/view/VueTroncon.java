package view;

import model.Troncon;

import java.awt.*;

public class VueTroncon {

    private Troncon m_troncon;

    public static Color COULEUR_DEFAUT = new Color(89, 147, 221);
    public static int STROKE_SIZE  = 5;

    public VueTroncon(Troncon troncon) {
        m_troncon = troncon;
    }

    // Methods
    public void draw(Graphics2D g2) {
        g2.setColor(COULEUR_DEFAUT);
        g2.setStroke(new BasicStroke(STROKE_SIZE));

        int x1 = m_troncon.getArrivee().getM_x();
        int y1 = m_troncon.getArrivee().getM_y();
        int x2 = m_troncon.getDepart().getM_x();
        int y2 = m_troncon.getDepart().getM_y();

        g2.drawLine(x1, y1, x2, y2);
    }

}
