package model;

/**
 * Class Livraison
 */
public class Livraison {

    //
    // Fields
    //

    private int retard = 0;
    private int noClient;
    private boolean effectuee = false;

    private PlageHoraire m_laPlage;

    private Noeud m_adresse;

    public Heure m_heureLivraison;

    //
    // Constructors
    //
    public Livraison() {
    }

    ;

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Set the value of retard
     *
     * @param newVar the new value of retard
     */
    private void setRetard(int newVar) {
        retard = newVar;
    }

    /**
     * Get the value of retard
     *
     * @return the value of retard
     */
    private int getRetard() {
        return retard;
    }

    /**
     * Set the value of noClient
     *
     * @param newVar the new value of noClient
     */
    private void setNoClient(int newVar) {
        noClient = newVar;
    }

    /**
     * Get the value of noClient
     *
     * @return the value of noClient
     */
    private int getNoClient() {
        return noClient;
    }

    /**
     * Set the value of effectuee
     *
     * @param newVar the new value of effectuee
     */
    private void setEffectuee(boolean newVar) {
        effectuee = newVar;
    }

    /**
     * Get the value of effectuee
     *
     * @return the value of effectuee
     */
    private boolean getEffectuee() {
        return effectuee;
    }

    /**
     * Set the value of m_laPlage
     *
     * @param newVar the new value of m_laPlage
     */
    private void setLaPlage(PlageHoraire newVar) {
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
