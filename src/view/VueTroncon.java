package view;

import model.Troncon;

import java.awt.*;

public class VueTroncon {

    private Troncon m_troncon;

    private static Color COULEUR_DEFAUT = new Color(89, 147, 221);
    private static int STROKE_SIZE  = 2;

    private static int RUE_FONTSIZE = 6;
    private static Font RUE_FONT = new Font("Arial", Font.PLAIN, RUE_FONTSIZE);
    private static int RUE_Y_OFFSET = 5;

    private static double ANGLE_TOLERANCE = Math.PI / 10;

    public VueTroncon(Troncon troncon) {
        m_troncon = troncon;
    }

    // Methods
    public void draw(Graphics2D g2, int voies) {
        g2.setColor(COULEUR_DEFAUT);
        g2.setStroke(new BasicStroke(STROKE_SIZE * voies));

        int x1 = m_troncon.getArrivee().getM_x();
        int y1 = m_troncon.getArrivee().getM_y();
        int x2 = m_troncon.getDepart().getM_x();
        int y2 = m_troncon.getDepart().getM_y();

        g2.drawLine(x1, y1, x2, y2);

        // Affichage du nom de la rue
        // TODO : Corriger ça pour que ce soit plus clair, ou l'enlever sinon
        /**
        g2.setColor(COULEUR_DEFAUT.brighter());
        g2.setFont(RUE_FONT);

        int t_x = (x1 + x2) / 2;
        int t_y = (y1 + y2) / 2;
        double angle = Math.atan2(Math.abs(y2 - y1), Math.abs(x2 - x1));
        if ( Math.abs(angle - Math.PI / 2) < ANGLE_TOLERANCE) {
            t_y += RUE_Y_OFFSET;
        }
        g2.drawString(m_troncon.getM_nom(), t_x, t_y);
        **/
    }

    // Getters/Setters
    public Troncon getM_troncon() {
        return m_troncon;
    }
}
