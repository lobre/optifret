package model;

import java.util.ArrayList;


/**
 * Class PlageHoraire
 */
public class PlageHoraire implements Comparable<PlageHoraire> {

    //
    // Fields
    //
    private ArrayList<Livraison> m_livraisons;

    private Heure m_heureDebut;
    private Heure m_heureFin;

    private int m_indice;

    //
    // Constructors
    //

    public PlageHoraire(Heure hDebut, Heure hFin) {
        m_livraisons = new ArrayList<Livraison>();
        m_heureDebut = hDebut;
        m_heureFin = hFin;
        m_indice = 0;
    }

    //
    // Methods
    //

    @Override
    public int compareTo(PlageHoraire ph) {
        return m_heureDebut.getTotalSeconds() - ph.getHeureDebut().getTotalSeconds();
    }

    //
    // Accessor methods
    //
    public void addLivraison(Livraison livraison) {
        livraison.getM_adresse().setM_livraison(livraison);
        m_livraisons.add(livraison);
    }

    public void removeLivraison(Livraison livraison) {
        livraison.getM_adresse().setM_livraison(null);
        m_livraisons.remove(livraison);
    }

    public ArrayList<Livraison> getM_livraisons() {
        return m_livraisons;
    }


    public void setHeureDebut(Heure newVar) {
        m_heureDebut = newVar;
    }

    public Heure getHeureDebut() {
        return m_heureDebut;
    }


    public void setHeureFin(Heure newVar) {
        m_heureFin = newVar;
    }

    public Heure getHeureFin() {
        return m_heureFin;
    }

    public void setM_indice(int m_indice) {
        this.m_indice = m_indice;
    }

    public int getM_indice() {
        return m_indice;
    }

    //
    // Other methods
    //
    @Override
    public String toString() {
        return m_heureDebut.toString() + " - " + m_heureFin.toString();
    }
}
