package view;

import model.Noeud;

import java.awt.*;
import java.awt.event.MouseEvent;

public class VueNoeud {

    private int m_rayon;
    private Color m_couleur;
    private Noeud m_noeud;

    private static Color COULEUR_DEFAUT = new Color(80, 80, 80);
    private static Color COULEUR_ENTREPOT = new Color(211, 5, 188);
    private static Color COULEUR_LIVRAISON = new Color(52, 148, 232);

    private static int RAYON_DEFAUT = 10;
    private static int RAYON_ENTREPOT = 25;
    private static int RAYON_LIVRAISON = 20;

    public VueNoeud (Noeud noeud) {
        m_noeud = noeud;
        m_couleur = VueNoeud.COULEUR_DEFAUT;
        m_rayon = VueNoeud.RAYON_DEFAUT;
    }
    
    public VueNoeud(){

    }

    public int getM_x() {
        return m_noeud.getM_x();
    }

    public int getM_y() {
        return m_noeud.getM_y();
    }

    public int getM_rayon() {
        return m_rayon;
    }

    public Color getM_couleur() {
        return m_couleur;
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed at : (" + e.getX() + ", " + e.getY() + ")");
    }
 
}

