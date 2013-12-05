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

    private PlageHoraire m_laPlage;

    private Noeud m_adresse;

    public Heure m_heureLivraison;

    //
    // Constructors
    //
    public Livraison() {
    }

    public Livraison(int id,int client, Noeud adresse) {
        this.m_id = id;
        this.m_noClient = client;
        this.m_adresse = adresse;
    }

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Set the value of m_retard
     *
     * @param newVar the new value of m_retard
     */
    private void setM_retard(int newVar) {
        m_retard = newVar;
    }

    /**
     * Get the value of m_retard
     *
     * @return the value of m_retard
     */
    private int getM_retard() {
        return m_retard;
    }

    /**
     * Set the value of m_noClient
     *
     * @param newVar the new value of m_noClient
     */
    private void setM_noClient(int newVar) {
        m_noClient = newVar;
    }

    /**
     * Get the value of m_noClient
     *
     * @return the value of m_noClient
     */
    private int getM_noClient() {
        return m_noClient;
    }

    /**
     * Set the value of m_effectuee
     *
     * @param newVar the new value of m_effectuee
     */
    private void setM_effectuee(boolean newVar) {
        m_effectuee = newVar;
    }

    /**
     * Get the value of m_effectuee
     *
     * @return the value of m_effectuee
     */
    private boolean getM_effectuee() {
        return m_effectuee;
    }

    /**
     * Set the value of m_laPlage
     *
     * @param newVar the new value of m_laPlage
     */
    public void setLaPlage(PlageHoraire newVar) {
        m_laPlage = newVar;
    }

    /**
     * Get the value of m_laPlage
     *
     * @return the value of m_laPlage
     */
    private PlageHoraire getLaPlage() {
        return m_laPlage;
    }

    /**
     * Set the value of m_adresse
     *
     * @param newVar the new value of m_adresse
     */
    private void setAdresse(Noeud newVar) {
        m_adresse = newVar;
    }

    /**
     * Get the value of m_adresse
     *
     * @return the value of m_adresse
     */
    private Noeud getAdresse() {
        return m_adresse;
    }

    /**
     * Set the value of m_heureLivraison
     *
     * @param newVar the new value of m_heureLivraison
     */
    public void setHeureLivraison(Heure newVar) {
        m_heureLivraison = newVar;
    }

    /**
     * Get the value of m_heureLivraison
     *
     * @return the value of m_heureLivraison
     */
    public Heure getHeureLivraison() {
        return m_heureLivraison;
    }

    //
    // Other methods
    //

}
