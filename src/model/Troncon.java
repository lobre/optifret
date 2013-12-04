package model;

/**
 * Class Troncon
 */
public class Troncon {

    //
    // Fields
    //

    private String m_nom;
    private float m_longueur;
    private float m_vitesse;

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
     * Set the value of m_nom
     *
     * @param newVar the new value of m_nom
     */
    private void setM_nom(String newVar) {
        m_nom = newVar;
    }

    /**
     * Get the value of m_nom
     *
     * @return the value of m_nom
     */
    private String getM_nom() {
        return m_nom;
    }

    /**
     * Set the value of m_longueur
     *
     * @param newVar the new value of m_longueur
     */
    private void setM_longueur(float newVar) {
        m_longueur = newVar;
    }

    /**
     * Get the value of m_longueur
     *
     * @return the value of m_longueur
     */
    private float getM_longueur() {
        return m_longueur;
    }

    /**
     * Set the value of m_vitesse
     *
     * @param newVar the new value of m_vitesse
     */
    private void setM_vitesse(float newVar) {
        m_vitesse = newVar;
    }

    /**
     * Get the value of m_vitesse
     *
     * @return the value of m_vitesse
     */
    private float getM_vitesse() {
        return m_vitesse;
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
