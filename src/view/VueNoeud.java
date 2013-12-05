package view;

import model.Noeud;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;

public class VueNoeud {

    private int m_rayon;
    private Color m_couleur;
    private Noeud m_noeud;

    public VueNoeud (Noeud noeud) {
        m_noeud = noeud;
        m_couleur = new Color(80, 80, 80);
        m_rayon = 10;
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
 
}

