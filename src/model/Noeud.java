package model;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Noeud
 */
public class Noeud {

    //
    // Fields
    //

    public static int PARSE_ERROR = -1;
    public static int PARSE_OK = 0;

    private int m_id;
    private int m_x;
    private int m_y;
    private boolean m_entrepot;
    private ArrayList<Troncon> m_troncons;

    Livraison m_livraison;

    //
    // Constructors
    //
    public Noeud() {
        m_livraison = null;
        m_entrepot = false;
        m_troncons = new ArrayList<Troncon>();
    }

    public Noeud(int id, int x, int y) {
        m_livraison = null;
        m_troncons = new ArrayList<Troncon>();

        m_id = id;
        m_x = x;
        m_y = y;
    }

    //
    // Methods
    //


    //
    // Accessor methods
    //


    public int getM_id() {
        return m_id;
    }


    public int getM_x() {
        return m_x;
    }
    public int getM_y() {
        return m_y;
    }

    public ArrayList<Troncon> getM_troncons() {
        return m_troncons;
    }

    public void setM_livraison(Livraison m_livraison) {
        this.m_livraison = m_livraison;
    }
    public Livraison getM_livraison() {
        return m_livraison;
    }

    public void setM_entrepot(boolean m_entrepot) {
        this.m_entrepot = m_entrepot;
    }
    public boolean isEntrepot() {
        return m_entrepot;
    }
//
    // Other methods
    //

    public boolean hasLivraison() {
        return (m_livraison != null);
    }

    public int fromXML(Element noeud_xml) {
        try {
            m_id = Integer.parseInt(noeud_xml.getAttribute("id"));
            m_x = Integer.parseInt(noeud_xml.getAttribute("x"));
            m_y = Integer.parseInt(noeud_xml.getAttribute("y"));
        } catch (NullPointerException ne) {
            return Noeud.PARSE_ERROR;
        }

        return Noeud.PARSE_OK;

    }

    public int tronconsFromXML(Element noeud_xml, HashMap<Integer, Noeud> noeuds) {
        m_troncons = new ArrayList<Troncon>();
        NodeList liste_troncons_xml = noeud_xml.getElementsByTagName("TronconSortant");

        for (int i = 0; i < liste_troncons_xml.getLength(); i++) {
            Element troncon_xml = (Element) liste_troncons_xml.item(i);
            Troncon troncon = new Troncon();
            int status = troncon.fromXML(troncon_xml, this, noeuds);
            if (status != Troncon.PARSE_OK) {
                return Noeud.PARSE_ERROR;
            }
            m_troncons.add(troncon);
        }

        return Noeud.PARSE_OK;
    }

    public String toString() {
        return "[x=" + m_x + ", y=" + m_y + "]";
    }
}
