package view;

import model.Noeud;

import java.awt.*;

public class VueNoeud {

    private Noeud m_noeud;

    private boolean m_selected;
    private boolean m_focused;

    private static Color COULEUR_DEFAUT = new Color(202, 216, 255);
    private static Color COULEUR_ENTREPOT = new Color(211, 5, 188);
    private static Color COULEUR_LIVRAISON = new Color(143, 210, 53);

    public static int RAYON_DEFAUT = 20;
    private static int RAYON_LIVRAISON = 25;
    private static int RAYON_ENTREPOT = 30;

    // Facteur par lequel les coordonées de la vue sont amplifiées par rapport à ceux du modèle
    public static int AMPLIFICATION_FACTOR = 3;

    private static int FOCUS_FACTOR = 2;
    private static int BORDER_WIDTH = 1;

    private static int FONT_SIZE = 8;
    private static Font ID_FONT = new Font("Arial", Font.BOLD, FONT_SIZE);

    public VueNoeud(Noeud noeud) {
        m_noeud = noeud;
        m_focused = false;
        m_selected = false;
    }

    // Methods

    public void draw(Graphics2D g2) {

        int r = getM_rayon();
        if (!m_noeud.isEntrepot() && (m_selected || m_focused)) {
            r *= FOCUS_FACTOR;
        }

        int x = getM_x() - r;
        int y = getM_y() - r;

        Color color = getM_couleur();

        // Remplissage du cercle
        g2.setColor(color);
        g2.fillOval(x, y, 2 * r, 2 * r);

        // Bordure du cercle
        g2.setStroke(new BasicStroke(BORDER_WIDTH));
        g2.setColor(color.darker());
        g2.drawOval(x, y, 2 * r, 2 * r);

        // Si un noeud est sélectionné, on affiche son ID
        if (m_selected) {
            g2.setFont(ID_FONT);
            String idString = Integer.toString(m_noeud.getM_id());

            int text_x = x + r - (int) (idString.length() * FONT_SIZE * 0.3);
            int text_y = y + r + (int) (FONT_SIZE * 0.3);

            g2.drawString(idString, text_x, text_y);
        }
    }


    // Getters/Setters

    public Noeud getM_noeud() {
        return m_noeud;
    }

    public int getM_x() {
        return m_noeud.getM_x() * AMPLIFICATION_FACTOR;
    }

    public int getM_y() {
        return m_noeud.getM_y() * AMPLIFICATION_FACTOR;
    }

    public int getM_rayon() {
        if (m_noeud.isEntrepot()) {
            return RAYON_ENTREPOT;
        } else if (m_noeud.hasLivraison()) {
            return RAYON_LIVRAISON;
        } else {
            return RAYON_DEFAUT;
        }
    }

    public Color getM_couleur() {
        Color color = COULEUR_DEFAUT;
        if (m_noeud.isEntrepot()) {
            color = COULEUR_ENTREPOT;
        } else if (m_noeud.hasLivraison()) {
            color = COULEUR_LIVRAISON;
        }

        if (m_selected) {
            color = color.brighter();
        }

        return color;
    }

    public void setM_selected(boolean m_selected) {
        this.m_selected = m_selected;
    }

    public void setM_focused(boolean m_focused) {
        this.m_focused = m_focused;
    }
}

