package model;


/**
 * Class Heure
 */
public class Heure {

    //
    // Fields
    //

    private int m_heures;
    private int m_minutes;
    private int m_secondes;

    //
    // Constructors
    //
    public Heure() {
    }

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Set the value of m_heures
     *
     * @param newVar the new value of m_heures
     */
    public void setM_heures(int newVar) {
        m_heures = newVar;
    }

    /**
     * Get the value of m_heures
     *
     * @return the value of m_heures
     */
    public int getM_heures() {
        return m_heures;
    }

    /**
     * Set the value of m_minutes
     *
     * @param newVar the new value of m_minutes
     */
    public void setM_minutes(int newVar) {
        m_minutes = newVar;
    }

    /**
     * Get the value of m_minutes
     *
     * @return the value of m_minutes
     */
    public int getM_minutes() {
        return m_minutes;
    }

    /**
     * Set the value of m_secondes
     *
     * @param newVar the new value of m_secondes
     */
    public void setM_secondes(int newVar) {
        m_secondes = newVar;
    }

    /**
     * Get the value of m_secondes
     *
     * @return the value of m_secondes
     */
    public int getM_secondes() {
        return m_secondes;
    }
    //
    // Other methods
    //

    public boolean estAvant(Heure h){
        if (this.getM_heures() < h.getM_heures()) {
            return true;
        }
        if (this.getM_heures() == h.getM_heures()) {

            if (this.getM_minutes() < h.getM_minutes()) {
                return  true;
            }
        }
        return false;
    }
}
