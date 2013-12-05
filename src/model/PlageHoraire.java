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


    private Vector m_lesLivraisons = new Vector();

    private Heure m_heureDebut;

    private Heure m_heureFin;

    //
    // Constructors
    //
    public PlageHoraire() {
        m_heureDebut = new Heure();
        m_heureFin = new Heure();
    }

    public PlageHoraire(Heure hDebut, Heure hFin) {
        this.m_heureDebut = hDebut;
        this.m_heureFin = hFin;
    }

    //
    // Methods
    //


    //
    // Accessor methods
    //

    /**
     * Add a LesLivraisons object to the m_lesLivraisons List
     */
    public void addLesLivraisons(Livraison new_object) {
        m_lesLivraisons.add(new_object);
    }

    /**
     * Remove a LesLivraisons object from m_lesLivraisons List
     */
    private void removeLesLivraisons(Livraison new_object) {
        m_lesLivraisons.remove(new_object);
    }

    /**
     * Get the List of LesLivraisons objects held by m_lesLivraisons
     *
     * @return List of LesLivraisons objects held by m_lesLivraisons
     */
    private List getLesLivraisonsList() {
        return (List) m_lesLivraisons;
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
