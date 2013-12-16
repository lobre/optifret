package model;

/**
 * Class Livraison
 */
public class Livraison {

    // TODO : ajouter le temps de livraison prévisionnel (mis à jour par les feuilles de route)

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
    public Livraison() {
    }

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


    //
    // Accessor methods
    //


    public Heure getHeureLivraison() {
        return m_heureLivraison;
    }

    public int getId() {
        return m_id;
    }

    public int getNoClient() {
        return m_noClient;
    }

    public PlageHoraire getPlage() {
        return m_plage;
    }

    public Noeud getAdresse() {
        return m_adresse;
    }

    public void setHeureLivraison(Heure m_heureLivraison) {
        this.m_heureLivraison = m_heureLivraison;
    }

    //
    // Other methods
    //

}
