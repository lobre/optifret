package model;

import javafx.util.Pair;
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
    // TODO : replace private statements where needed


    public String getM_nom() {
        return m_nom;
    }

    public float getM_longueur() {
        return m_longueur;
    }

    public float getM_vitesse() {
        return m_vitesse;
    }

    public Noeud getArrivee() {
        return m_arrivee;
    }

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

        return Troncon.PARSE_OK;

    }


    public Pair<Noeud, Noeud> getPair() {
        return new Pair<Noeud, Noeud>(m_depart, m_arrivee);
    }
    public Pair<Noeud, Noeud> getOppositePair() {
        return new Pair<Noeud, Noeud>(m_arrivee, m_depart);
    }

    public Boolean estDeSensPositif(){
        return m_depart.getM_id() < m_arrivee.getM_id();
    }

    public double getAngle() {
        return Math.atan2(m_arrivee.getM_y() - m_depart.getM_y(), m_arrivee.getM_x() - m_depart.getM_x());
    }

    public double angleAvec(Troncon t2) {
        return getAngle() - t2.getAngle();
    }

}
