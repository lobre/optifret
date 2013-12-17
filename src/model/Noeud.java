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

    /**
     * Constructeur de la classe
     */
    public Noeud() {
        m_livraison = null;
        m_entrepot = false;
        m_troncons = new ArrayList<Troncon>();
    }

    /**
     * Constructeur de la classe
     * @param id adresse unique du noeud
     * @param x  abscisse du noeud dans le plan
     * @param y  ordonn&eacute;e du noeud dans le plan
     */
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

    /**
     * V&eacute;rifie si une livraison doit &ecirc;tre effectu&eacute;e &agrave; l'adresse du noeud
     * @return vrai, si une livraison est associ&eacute;e au noeud
     *         faux, sinon
     */
    public boolean hasLivraison() {
        return (m_livraison != null);
    }

    /**
     * Définie les attributs m_id, m_x et m_y de l'instance
     * @param noeud_xml balise 'Noeud' contenant les attributs 'id', 'x' et 'y'
     * @return PARSE_OK, si tous les attributs ont pu être définis
     *         PARSE_ERROR, sinon
     */
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

    /**
     * Instancie les tron&ccedil;ons r&eacute;li&eacute;s au noeud
     * @param noeud_xml balise 'Noeud' contenant des balises 'TronconSortant'
     * @param noeuds    hashmap contenant, entre autres, les noeuds extr&eacute;mit&eacute;s des tron&ccedil;ons &agrave; instancier
     * @return PARSE_OK, si l'instanciation r&eacute;ussit
     *                  PARSE_ERROR, sinon
     */
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


    //
    // Accessor methods
    //

    public String toString() {
        return " x = " + m_x + ", y = " + m_y;
    }

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

}
