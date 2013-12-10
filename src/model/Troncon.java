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

    public Pair<Noeud, Noeud> getPair() {
        return new Pair<Noeud, Noeud>(m_depart, m_arrivee);
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

}
