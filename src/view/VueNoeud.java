package view;

import model.Noeud;

import java.awt.*;

/**
 * Vue d'un noeud dans la map. Peut avoir plusieurs états (sélectionné, survolé, ...)
 */
public class VueNoeud {
    private Noeud m_noeud;

    private boolean m_selected;
    private boolean m_focused;

    /**
     * Couleur d'un noeud "normal"
     */
    private static final Color COULEUR_DEFAUT = new Color(202, 216, 255);
    /**
     * Couleur de l'entrepot
     */
    private static final Color COULEUR_ENTREPOT = new Color(193, 28, 185);
    /**
     * Couleur d'une livraison hors-horaire
     */
    private static final Color COULEUR_LIVRAISON_HORSHORAIRE = new Color(207, 0, 11);

    /**
     * Rayon (par d&eacute;faut) du noeud
     */
    public static final int RAYON_DEFAUT = 13;
    private static final int RAYON_LIVRAISON = 17;
    private static final int RAYON_ENTREPOT = 24;
    /**
     *  Facteur par lequel les coordon&eacute;es de la vue sont amplifi&eacute;es par rapport &agrave; ceux du mod&egrave;le
     */
    public static final int AMPLIFICATION_FACTOR = 2;

    private static double FOCUS_FACTOR = 1.5;

    private static int FONT_SIZE = 16;
    private static Font ID_FONT = new Font("Arial", Font.BOLD, FONT_SIZE);

    public VueNoeud(Noeud noeud) {
        m_noeud = noeud;
        m_focused = false;
        m_selected = false;
    }

    //
    // Getters/Setters
    //
    public Noeud getNoeud() {
        return m_noeud;
    }

    public int getX() {
        return m_noeud.getX() * AMPLIFICATION_FACTOR;
    }

    public int getY() {
        return m_noeud.getY() * AMPLIFICATION_FACTOR;
    }

    public void setSelected(boolean m_selected) {
        this.m_selected = m_selected;
    }

    public void setFocused(boolean m_focused) {
        this.m_focused = m_focused;
    }

    //
    // Other methods
    //

    /**
     * M&eacute;thode de dessin du noeud repr&eacute;sent&eacute; par la VueNoeud.
     * @param g2 contexte graphique dans lequel doit &ecirc;tre dessin&eacute; le noeud
     */
    public void draw(Graphics2D g2) {

        int r = getRayon();
        if (!m_noeud.isEntrepot() && (m_selected || m_focused)) {
            r *= FOCUS_FACTOR;
        }

        int x = getX() - r;
        int y = getY() - r;

        Color color = getCouleur();

        if (m_noeud.hasLivraison() && m_noeud.getLivraison().isHorsHoraire()) {
            r += 3;
        }

        // Ombre du cercle
        g2.setColor(color.darker());
        g2.fillOval(x, y, 2 * (r + 1), 2 * (r + 1));


        // Remplissage du cercle
        g2.setColor(color);
        g2.fillOval(x, y, 2 * r, 2 * r);

        // Si un noeud est sélectionné, on affiche son ID ; Si c'est un entrepot, on affiche un grand E par dessus
        if (m_selected || m_noeud.isEntrepot() || m_noeud.hasLivraison()) {
            g2.setFont(ID_FONT);
            String message;
            if (m_noeud.hasLivraison() && m_noeud.getLivraison().isHorsHoraire()) {
                message = "!";
                g2.setColor(Color.WHITE);
            } else if (m_noeud.isEntrepot()) {
                message = "E";
                g2.setColor(getCouleur().darker());
            } else {
                message = Integer.toString(m_noeud.getId());
                g2.setColor(getCouleur().darker());
            }
            int text_x = x + r - g2.getFontMetrics().stringWidth(message) / 2;
            int text_y = y + r + g2.getFontMetrics().getHeight() / 3;

            g2.drawString(message, text_x, text_y);
        }
    }


    /**
     * Donne le rayon de la VueNoeud en fonction de la nature du noeud
     * @return rayon de la VueNoeud
     */
    public int getRayon() {
        if (m_noeud.isEntrepot()) {
            return RAYON_ENTREPOT;
        } else if (m_noeud.hasLivraison()) {
            return RAYON_LIVRAISON;
        } else {
            return RAYON_DEFAUT;
        }
    }

    /**
     * Donne la couleur du noeud en fonction de la nature du noeud (entrepot, livraison, livraison en retard) et de
     * l'&eacute;tat de la VueNoeud (s&eacute;lectionn&eacute;e, survol&eacute;e)
     * @see model.Noeud
     * @see model.Livraison
     * @return couleur de la VueNoeud
     */
    public Color getCouleur() {
        Color color = COULEUR_DEFAUT;
        if (m_noeud.isEntrepot()) {
            color = COULEUR_ENTREPOT;
        } else if (m_noeud.hasLivraison()) {
            if (m_noeud.getLivraison().isHorsHoraire()) {
                color = COULEUR_LIVRAISON_HORSHORAIRE;
            } else {
                color = VueTroncon.getCouleurPlageHoraire(m_noeud.getLivraison().getPlage());
            }
        }

        if (m_selected) {
            color = color.brighter();
        }

        return color;
    }

}

