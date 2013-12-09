package view;

import model.Noeud;

import java.awt.*;

public class VueNoeud {

    private int m_rayon;
    private Noeud m_noeud;

    private static Color COULEUR_DEFAUT = new Color(202, 216, 255);
    private static Color COULEUR_ENTREPOT = new Color(211, 5, 188);
    private static Color COULEUR_LIVRAISON = new Color(140, 233, 255);

    private static int RAYON_DEFAUT = 5;
    private static int RAYON_LIVRAISON = 7;
    private static int RAYON_ENTREPOT = 9;

    public VueNoeud (Noeud noeud) {
        m_noeud = noeud;
        m_rayon = VueNoeud.RAYON_DEFAUT;
    }
    
    public VueNoeud(){

    }

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
        if (m_noeud.isM_entrepot()) {
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
        if (m_noeud.isM_entrepot()) {
            return COULEUR_ENTREPOT;
        }
        else if (m_noeud.hasLivraison()) {
            return COULEUR_LIVRAISON;
        }
        else {
            return COULEUR_DEFAUT;
        }
    }

 
}

