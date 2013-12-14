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

    public Heure m_heureLivraison;

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



    public int getM_id() {
        return m_id;
    }

    public int getM_noClient() {
        return m_noClient;
    }

    public PlageHoraire getM_plage() {
        return m_plage;
    }

    public Noeud getM_adresse() {
        return m_adresse;
    }

    //
    // Other methods
    //

}
