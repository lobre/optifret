package model;

/**
 * Class Troncon
 */
public class Troncon {

    //
    // Fields
    //

    private String nom;
    private float longueur;
    private float vitesse;

    private Noeud m_arrivee;

    private Noeud m_depart;

    //
    // Constructors
    //
    public Troncon() {
    }

    ;

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Set the value of nom
     *
     * @param newVar the new value of nom
     */
    private void setNom(String newVar) {
        nom = newVar;
    }

    /**
     * Get the value of nom
     *
     * @return the value of nom
     */
    private String getNom() {
        return nom;
    }

    /**
     * Set the value of longueur
     *
     * @param newVar the new value of longueur
     */
    private void setLongueur(float newVar) {
        longueur = newVar;
    }

    /**
     * Get the value of longueur
     *
     * @return the value of longueur
     */
    private float getLongueur() {
        return longueur;
    }

    /**
     * Set the value of vitesse
     *
     * @param newVar the new value of vitesse
     */
    private void setVitesse(float newVar) {
        vitesse = newVar;
    }

    /**
     * Get the value of vitesse
     *
     * @return the value of vitesse
     */
    private float getVitesse() {
        return vitesse;
    }

    /**
     * Set the value of m_arrivee
     *
     * @param newVar the new value of m_arrivee
     */
    private void setArrivee(Noeud newVar) {
        m_arrivee = newVar;
    }

    /**
     * Get the value of m_arrivee
     *
     * @return the value of m_arrivee
     */
    private Noeud getArrivee() {
        return m_arrivee;
    }

    /**
     * Set the value of m_depart
     *
     * @param newVar the new value of m_depart
     */
    private void setDepart(Noeud newVar) {
        m_depart = newVar;
    }

    /**
     * Get the value of m_depart
     *
     * @return the value of m_depart
     */
    private Noeud getDepart() {
        return m_depart;
    }

    //
    // Other methods
    //

}
