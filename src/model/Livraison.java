package model;

/**
 * Class Livraison
 */
public class Livraison {

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


    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_retard(int newVar) {
        m_retard = newVar;
    }

    public int getM_retard() {
        return m_retard;
    }

    public void setM_noClient(int newVar) {
        m_noClient = newVar;
    }

    public int getM_noClient() {
        return m_noClient;
    }

    public void setM_effectuee(boolean newVar) {
        m_effectuee = newVar;
    }

    public boolean getM_effectuee() {
        return m_effectuee;
    }

    public void setM_plage(PlageHoraire newVar) {
        m_plage = newVar;
    }

    public PlageHoraire getM_plage() {
        return m_plage;
    }

    public void setM_adresse(Noeud newVar) {
        m_adresse = newVar;
    }

    public Noeud getM_adresse() {
        return m_adresse;
    }

    public void setM_heureLivraison(Heure newVar) {
        m_heureLivraison = newVar;
    }

    public Heure getM_heureLivraison() {
        return m_heureLivraison;
    }

    //
    // Other methods
    //

}
