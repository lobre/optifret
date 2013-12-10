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

    private static int RAYON_DEFAUT = 5;
    private static int RAYON_LIVRAISON = 7;
    private static int RAYON_ENTREPOT = 14;
    private static int ZOOM_FACTOR = 2;
    private static int BORDER_WIDTH = 1;

    public VueNoeud (Noeud noeud) {
        m_noeud = noeud;
        m_focused = false;
        m_selected = false;
    }
    
    public VueNoeud(){
    }

    // Methods

    public void draw(Graphics2D g2) {

        int r = getM_rayon();
        if(!m_noeud.isEntrepot() && (m_selected || m_focused)){
            r *= ZOOM_FACTOR;
        }

        int x = m_noeud.getM_x() - r;
        int y = m_noeud.getM_y() - r;

        Color color = getM_couleur();

        // Remplissage du cercle
        g2.setColor(color);
        g2.fillOval(x, y, 2 * r, 2 * r);

        // Bordure du cercle
        g2.setStroke(new BasicStroke(BORDER_WIDTH));
        g2.setColor(color.darker());
        g2.drawOval(x, y, 2 * r, 2 * r);
    }


    // Getters/Setters

    public Noeud getM_noeud() {
        return m_noeud;
    }

    public int getM_x() {
        return m_noeud.getM_x();
    }

    public int getM_y() {
        return m_noeud.getM_y();
    }

    public int getM_rayon() {
        if (m_noeud.isEntrepot()) {
            return RAYON_ENTREPOT;
        }
        else if (m_noeud.hasLivraison()) {
            return RAYON_LIVRAISON;
        }
        else {
            return RAYON_DEFAUT;
        }
    }

    public Color getM_couleur() {
        if (m_noeud.isEntrepot()) {
            return COULEUR_ENTREPOT;
        }
        else if (m_noeud.hasLivraison()) {
            return COULEUR_LIVRAISON;
        }
        else {
            return COULEUR_DEFAUT;
        }
    }

    public void setM_selected(boolean m_selected) {
        this.m_selected = m_selected;
    }

    public void setM_focused(boolean m_focused) {
        this.m_focused = m_focused;
    }
}

