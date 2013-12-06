package model;

import org.w3c.dom.Element;

import java.util.HashMap;

/**
 * Class Troncon
 */
public class Troncon {

    //
    // Fields
    //

    public static int PARSE_OK = 0;
    public static int PARSE_ERROR = -1;

    private String m_nom;
    private float m_longueur;
    private float m_vitesse;
    private Noeud m_arrivee;
    private Noeud m_depart;

    //
    // Constructors
    //
    public Troncon() {
    }

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Set the value of m_nom
     *
     * @param newVar the new value of m_nom
     */
    private void setM_nom(String newVar) {
        m_nom = newVar;
    }

    /**
     * Get the value of m_nom
     *
     * @return the value of m_nom
     */
    private String getM_nom() {
        return m_nom;
    }

    /**
     * Set the value of m_longueur
     *
     * @param newVar the new value of m_longueur
     */
    public void setM_longueur(float newVar) {
        m_longueur = newVar;
    }

    /**
     * Get the value of m_longueur
     *
     * @return the value of m_longueur
     */
    public float getM_longueur() {
        return m_longueur;
    }

    /**
     * Set the value of m_vitesse
     *
     * @param newVar the new value of m_vitesse
     */
    public void setM_vitesse(float newVar) {
        m_vitesse = newVar;
    }

    /**
     * Get the value of m_vitesse
     *
     * @return the value of m_vitesse
     */
    public float getM_vitesse() {
        return m_vitesse;
    }

    /**
     * Set the value of m_arrivee
     *
     * @param newVar the new value of m_arrivee
     */
    public void setArrivee(Noeud newVar) {
        m_arrivee = newVar;
    }

    /**
     * Get the value of m_arrivee
     *
     * @return the value of m_arrivee
     */
    public Noeud getArrivee() {
        return m_arrivee;
    }

    /**
     * Set the value of m_depart
     *
     * @param newVar the new value of m_depart
     */
    public void setDepart(Noeud newVar) {
        m_depart = newVar;
    }

    /**
     * Get the value of m_depart
     *
     * @return the value of m_depart
     */
    public Noeud getDepart() {
        return m_depart;
    }

    //
    // Other methods
    //

    public int fromXML(Element noeud_xml, Noeud depart, HashMap<Integer, Noeud> noeuds) {

        try {
            m_depart = depart;
            int arrivee_id = Integer.parseInt(noeud_xml.getAttribute("destination"));
            m_arrivee = noeuds.get(arrivee_id);

            m_vitesse = Float.parseFloat(noeud_xml.getAttribute("vitesse").replace(",", "."));
            m_longueur = Float.parseFloat(noeud_xml.getAttribute("longueur").replace(",", "."));
            m_nom = noeud_xml.getAttribute("nomRue");

            if (m_nom.isEmpty()) {
                return Troncon.PARSE_ERROR;
            }
        } catch (NullPointerException ne) {
            return Troncon.PARSE_ERROR;
        }

        System.out.println("    Troncon : " + m_nom + "; Depart: " + m_depart.getM_id() + "; Arrivee: " + m_arrivee.getM_id());

        return Troncon.PARSE_OK;

    }

}
