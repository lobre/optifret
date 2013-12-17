package model;

import libs.ParseXmlException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Plan
 */
public class Plan {

    //
    // Fields
    //

    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 0;

    private Noeud m_racine;
    private HashMap<Integer, Noeud> m_noeuds;
    private ArrayList<Troncon> m_troncons;


    //
    // Constructors
    //

    /**
     * Constructeur de la classe
     */
    public Plan() {
        m_noeuds = new HashMap<Integer, Noeud>();
        m_troncons = new ArrayList<Troncon>();
    }

    //
    // Methods
    //

    /**
     * Dissocie les noeuds de 'm_noeuds' des livraisons et de l'entrepôt
     */
    public void resetNoeuds() {
        for (Noeud n : m_noeuds.values()) {
            n.setM_entrepot(false);
            n.setM_livraison(null);
        }
    }

    /**
     * Instancie les noeuds et le tronçons composant l'instance de Plan
     * @param racineXML balise 'Reseau' contenant des balises 'Noeud'
     * @return          PARSE_OK, si l'instanciation est réussie
     */
    public int fromXML(Element racineXML) {
        HashMap<Integer, Noeud> noeuds = new HashMap<Integer, Noeud>();
        ArrayList<Troncon> troncons = new ArrayList<Troncon>();

        NodeList liste_noeuds = racineXML.getElementsByTagName("Noeud");

        if (liste_noeuds.getLength() < 1) {
            throw new ParseXmlException("liste de noeud vide");
        }

        // On commence par parser la "base" des noeuds (sans les tronçons)
        for (int i = 0; i < liste_noeuds.getLength(); i++) {
            Element noeud_xml = (Element) liste_noeuds.item(i);
            Noeud noeud = new Noeud();
            int status = noeud.fromXML(noeud_xml);
            if (status != Noeud.PARSE_OK) {
                throw new ParseXmlException("parsing noeud vide");
            }
            if (noeuds.get(noeud.getM_id())==null)
            {
                noeuds.put(noeud.getM_id(), noeud);
            }
            else {
                throw new ParseXmlException("Id noeud non-unique");
            }
        }

        // Une fois le parsing des noeuds complétés, on rajoute à chaque noeud ses tronçons
        for (int i = 0; i < liste_noeuds.getLength(); i++) {
            Element noeud_xml = (Element) liste_noeuds.item(i);

            Noeud noeud = noeuds.get(i);
            int status = noeud.tronconsFromXML(noeud_xml, noeuds);
            if (status != Noeud.PARSE_OK) {
                return Plan.PARSE_ERROR;
            }

            // Mise à jour de la liste de tous les tronçons
            troncons.addAll(noeud.getM_troncons());

        }

        m_noeuds = noeuds;
        m_racine = noeuds.get(0);
        m_troncons = troncons;

        return Plan.PARSE_OK;

    }

    //
    // Accessor methods
    //

    public Noeud getM_racine() {
        return m_racine;
    }

    public void setM_racine(Noeud m_racine) {
        this.m_racine = m_racine;
    }

    public Noeud getNoeudParID(int id) {
        return m_noeuds.containsKey(id) ? m_noeuds.get(id) : null;
    }

    public HashMap<Integer, Noeud> getM_noeuds() {
        return m_noeuds;
    }

    public void setM_noeuds(HashMap<Integer, Noeud> m_noeuds) {
        this.m_noeuds = m_noeuds;
    }

    public void setM_troncons(ArrayList<Troncon> m_troncons) {
        this.m_troncons = m_troncons;
    }

    public ArrayList<Troncon> getM_troncons() {
        return m_troncons;
    }

}
