package model;

import java.util.List;
import java.util.Vector;


/**
 * Class PlageHoraire
 */
public class PlageHoraire {

    //
    // Fields
    //


    private Vector leslivraisonsVector = new Vector();

    private Heure m_heureDebut;

    private Heure m_heureFin;

    //
    // Constructors
    //
    public PlageHoraire() {
    }

    ;

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Add a LesLivraisons object to the leslivraisonsVector List
     */
    private void addLesLivraisons(Livraison new_object) {
        leslivraisonsVector.add(new_object);
    }

    /**
     * Remove a LesLivraisons object from leslivraisonsVector List
     */
    private void removeLesLivraisons(Livraison new_object) {
        leslivraisonsVector.remove(new_object);
    }

    /**
     * Get the List of LesLivraisons objects held by leslivraisonsVector
     *
     * @return List of LesLivraisons objects held by leslivraisonsVector
     */
    private List getLesLivraisonsList() {
        return (List) leslivraisonsVector;
    }


    /**
     * Set the value of m_heureDebut
     *
     * @param newVar the new value of m_heureDebut
     */
    private void setHeureDebut(Heure newVar) {
        m_heureDebut = newVar;
    }

    /**
     * Get the value of m_heureDebut
     *
     * @return the value of m_heureDebut
     */
    private Heure getHeureDebut() {
        return m_heureDebut;
    }

    /**
     * Set the value of m_heureFin
     *
     * @param newVar the new value of m_heureFin
     */
    private void setHeureFin(Heure newVar) {
        m_heureFin = newVar;
    }

    /**
     * Get the value of m_heureFin
     *
     * @return the value of m_heureFin
     */
    private Heure getHeureFin() {
        return m_heureFin;
    }

    //
    // Other methods
    //

}
