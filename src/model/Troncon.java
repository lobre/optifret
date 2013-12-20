package model;

import javafx.util.Pair;
import org.w3c.dom.Element;

import java.util.HashMap;

/**
 * Tron&ccedil;on: relie deux noeuds sur une certaine longueur, avec une certaine vitesse moyenne.
 */
public class Troncon {

    //
    // Fields
    //

    public static final int PARSE_OK = 0;
    public static final int PARSE_ERROR = -1;

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


    public String getNom() {
        return m_nom;
    }

    public float getLongueur() {
        return m_longueur;
    }

    public float getVitesse() {
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

    /**
     * Parse la repr&eacute;sentation XML d'un Troncon.
     * @param noeud_xml le noeud XML &agrave; parser
     * @param depart le noeud de d&eacute;part du Troncon
     * @param noeuds une map (id de noeud => Noeud) contenant tous les noeuds du Plan
     * @return PARSE_OK si le parsing s'est correctement pass&eacute;, PARSE_ERROR sinon.
     */
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

    /**
     * Renvoie une paire (Noeud de d&eacute;part, Noeud d'arriv&eacute;e)
     * @return Paire (Noeud de d&eacute;part, Noeud d'arriv&eacute;e)
     */
    public Pair<Noeud, Noeud> getPair() {
        return new Pair<Noeud, Noeud>(m_depart, m_arrivee);
    }

    /**
     * Renvoie une paire (Noeud d'arriv&eacute;e, Noeud de d&eacute;part)
     * @return Paire (Noeud d'arriv&eacute;e, Noeud de d&eacute;part)
     */
    public Pair<Noeud, Noeud> getOppositePair() {
        return new Pair<Noeud, Noeud>(m_arrivee, m_depart);
    }

    /**
     * Indique si le Troncon a un noeud de d&eacute;part &agrave; l'id inf&eacute;rieur &agrave; celui du noeud d'arriv&eacute;e. (sens arbitraire)
     * @return vrai si le noeud de d&eacute;part a un id inf&eacute;rieur &agrave; celui du noeud d'arriv&eacute;e.
     */
    public Boolean estDeSensPositif(){
        return m_depart.getId() < m_arrivee.getId();
    }

    public double getAngle() {
        return Math.atan2(m_arrivee.getY() - m_depart.getY(), m_arrivee.getX() - m_depart.getX());
    }

    public double angleAvec(Troncon t2) {
        return t2.getAngle()-getAngle() ;
    }

}
