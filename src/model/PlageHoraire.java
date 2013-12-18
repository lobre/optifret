package model;

import libs.ParseXmlException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;


/**
 * Class PlageHoraire
 */
public class PlageHoraire implements Comparable<PlageHoraire> {

    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 0;
    //
    // Fields
    //
    private ArrayList<Livraison> m_livraisons;

    private Heure m_heureDebut;
    private Heure m_heureFin;

    private int m_indice;

    //
    // Constructors
    //

    public PlageHoraire(Heure hDebut, Heure hFin) {
        m_livraisons = new ArrayList<Livraison>();
        m_heureDebut = hDebut;
        m_heureFin = hFin;
        m_indice = 0;
    }

    public PlageHoraire() {
        m_livraisons = new ArrayList<Livraison>();
        m_indice = 0;
    }

    //
    // Methods
    //

    @Override
    public int compareTo(PlageHoraire ph) {
        return m_heureDebut.getTotalSeconds() - ph.getHeureDebut().getTotalSeconds();
    }

    public int fromXML(Element e_plage, Plan plan) throws ParseXmlException {

        //récupération heures de début et de départ
        String h1 = e_plage.getAttribute("heureDebut");
        String h2 = e_plage.getAttribute("heureFin");
        Heure hDepart = new Heure();
        Heure hFin = new Heure();

        if (hDepart.fromString(h1) == PARSE_ERROR || hFin.fromString(h2) == PARSE_ERROR || !hDepart.estAvant(hFin)) {
            throw new ParseXmlException("Heure non valide");
        }
        this.setHeureDebut(hDepart);
        this.setHeureFin(hFin);

        //récupération des livraisons de la plage horaire
        NodeList livraisons = e_plage.getElementsByTagName("Livraisons");
        if (livraisons.getLength() != 1) {
            throw new ParseXmlException("Element <Livraisons> introuvable");
        }

        NodeList listeLivraisons = ((Element) livraisons.item(0)).getElementsByTagName("Livraison");
        for (int j = 0; j < listeLivraisons.getLength(); j++) {
            Element eLivraison = (Element) listeLivraisons.item(j);
            Livraison livraison = new Livraison(this);
            livraison.fromXML(eLivraison,plan,this);
            this.addLivraison(livraison);
        }

        return PARSE_OK;
    }
    //
    // Accessor methods
    //
    public void addLivraison(Livraison livraison) {
        livraison.getAdresse().setLivraison(livraison);
        m_livraisons.add(livraison);
    }

    public void removeLivraison(Livraison livraison) {
        livraison.getAdresse().setLivraison(null);
        m_livraisons.remove(livraison);
    }

    public ArrayList<Livraison> getLivraisons() {
        return m_livraisons;
    }


    public void setHeureDebut(Heure newVar) {
        m_heureDebut = newVar;
    }

    public Heure getHeureDebut() {
        return m_heureDebut;
    }


    public void setHeureFin(Heure newVar) {
        m_heureFin = newVar;
    }

    public Heure getHeureFin() {
        return m_heureFin;
    }

    public void setIndice(int m_indice) {
        this.m_indice = m_indice;
    }

    public int getIndice() {
        return m_indice;
    }

    //
    // Other methods
    //
    @Override
    public String toString() {
        return m_heureDebut.toString() + " - " + m_heureFin.toString();
    }
}
