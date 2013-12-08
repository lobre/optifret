package model;

import java.util.ArrayList;
import java.util.List;


/**
 * Class PlageHoraire
 */
public class PlageHoraire {

    //
    // Fields
    //
    private ArrayList<Livraison> m_livraisons;

    private Heure m_heureDebut;
    private Heure m_heureFin;

    //
    // Constructors
    //
    public PlageHoraire() {
        m_livraisons = new ArrayList<Livraison>();
        m_heureDebut = new Heure();
        m_heureFin = new Heure();
    }

    public PlageHoraire(Heure hDebut, Heure hFin) {
        m_livraisons = new ArrayList<Livraison>();
        this.m_heureDebut = hDebut;
        this.m_heureFin = hFin;
    }

    //
    // Methods
    //


    //
    // Accessor methods
    //
    public void addLivraison(Livraison livraison) {
        m_livraisons.add(livraison);
    }
    private void removeLivraison(Livraison livraison) {
        m_livraisons.remove(livraison);
    }
    public List getM_livraison() {
        return m_livraisons;
    }


    private void setHeureDebut(Heure newVar) {
        m_heureDebut = newVar;
    }
    private Heure getHeureDebut() {
        return m_heureDebut;
    }


    private void setHeureFin(Heure newVar) {
        m_heureFin = newVar;
    }
    private Heure getHeureFin() {
        return m_heureFin;
    }

    //
    // Other methods
    //
    @Override
    public String toString() {
        return  "[" + m_heureDebut.toString() + "] -> [" + m_heureFin.toString() + "]";
    }
}
