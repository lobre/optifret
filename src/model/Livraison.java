package model;

import libs.ParseXmlException;
import org.w3c.dom.Element;

/**
 * Class Livraison
 */
public class Livraison {

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
     * Constructeur par d&eacute;faut d'une livraison
     */
    public Livraison(PlageHoraire plage) {
        m_plage = plage;
        m_heureLivraison = null;
    }

    /**
     * Constructeur avec param&egrave;tres
     * @param id l'identifiant de la livraison
     * @param client le num&eacute;ro du client correspondant
     * @param adresse Noeud correspondant &agrave; la livraison
     * @param plage plage horaire de la livraison
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
     * Donne la plage horaire correspondant &agrave; la livraison
     * @return la plage horaire correspondant &agrave; la livraison
     */
    public PlageHoraire getPlage() {
        return m_plage;
    }

    /**
     * Donne le noeud associ&eacute; &agrave; la livraison
     * @return le noeud associ&eacute; &agrave; la livraison
     */
    public Noeud getAdresse() {
        return m_adresse;
    }

    public void setHeureLivraison(Heure m_heureLivraison) {
        this.m_heureLivraison = m_heureLivraison;
    }

    public boolean isHorsHoraire(){
        if (m_heureLivraison!=null && (m_heureLivraison.compareTo(m_plage.getHeureFin())>0 ||
                m_heureLivraison.compareTo(m_plage.getHeureFin())==0)){
            return true;
        }
        else{
            return false;
        }
    }
    public Heure getHeureLivraison() {
        return m_heureLivraison;
    }


    //
    // Other methods
    //

}
