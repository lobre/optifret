package model;

import libs.ParseXmlException;
import org.w3c.dom.Element;

/**
 * Class Livraison
 */
public class Livraison {

    // TODO : ajouter le temps de livraison prévisionnel (mis à jour par les feuilles de route)
    public static int PARSE_ERROR = -1;
    public static int PARSE_OK = 0;
    //
    // Fields
    //
    private int m_id;
    private int m_retard = 0;
    private int m_noClient;
    private boolean m_effectuee = false;

    private PlageHoraire m_plage;

    private Noeud m_adresse;

    private Heure m_heureLivraison;

    //
    // Constructors
    //

    /**
     * Constructeur par défaut d'une livraison
     */
    public Livraison(PlageHoraire plage) {
        m_plage = plage;
    }

    /**
     * Constructeur avec paramètres
     * @param id, l'identifiant de la livraison
     * @param client, le numéro du client correspondant
     * @param adresse, Noeud correspondant à la livraison
     * @param plage, plage horaire de la livraison
     */
    public Livraison(int id, int client, Noeud adresse, PlageHoraire plage) {
        m_id = id;
        m_noClient = client;
        m_adresse = adresse;
        m_adresse.setM_livraison(this);
        m_plage = plage;
    }

    //
    // Methods
    //
    public int fromXML(Element eLivraison, Plan plan, PlageHoraire plage) {
        try {
            m_id = Integer.parseInt(eLivraison.getAttribute("id"));
            m_noClient = Integer.parseInt(eLivraison.getAttribute("client"));
            int adNoeud = Integer.parseInt(eLivraison.getAttribute("adresse"));
            m_adresse = plan.getNoeudParID(adNoeud);
            for (Livraison l: plage.getM_livraisons()){
                if (l.getId() == m_id){
                    throw new ParseXmlException("id livraison non-unique");
                }
            }
            if (m_adresse == null) {
                throw new ParseXmlException("null node exception");
            }
        } catch (NullPointerException ne) {
            return PARSE_ERROR;
        }
        m_adresse.setM_livraison(this);
        return PARSE_OK;
    }


    //
    // Accessor methods
    //


    /**
     * Donne l'id de la livraison
     * @return l'id de la livraison
     */
    public int getId() {
        return m_id;
    }

    /**
     * Donne l'id du client de la livraison
     * @return l'id du client de la livraison
     */
    public int getNoClient() {
        return m_noClient;
    }

    /**
     * Donne la plage horaire correspondant à la livraison
     * @return la plage horaire correspondant à la livraison
     */
    public PlageHoraire getPlage() {
        return m_plage;
    }

    /**
     * Donne le noeud associé à la livraison
     * @return le noeud associé à la livraison
     */
    public Noeud getAdresse() {
        return m_adresse;
    }

    public void setHeureLivraison(Heure m_heureLivraison) {
        this.m_heureLivraison = m_heureLivraison;
    }

    public Heure getHeureLivraison() {
        return m_heureLivraison;
    }

    //
    // Other methods
    //

}
