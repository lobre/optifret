package model;


import java.util.LinkedList;

/**
 * Class Chemin
 */
public class Chemin {


    //
    // Fields
    //
    private final LinkedList<Troncon> listeTroncons = new LinkedList<Troncon>();

    //
    // Constructors
    //
    /**
     * Constructeur par défaut d'un chemin.
     */
    public Chemin() {

    }


    //
    // Methods
    //
    /**
     * Donne la longueur totale du chemin.
     * @return la longueur totale du chemin.
     */
    public int getLongueur() {
        int longueur = 0;
        for (Troncon troncon : listeTroncons) {
            longueur += troncon.getM_longueur()/troncon.getM_vitesse();
        }
        return longueur;
    }

    /**
     * Ajoute un tronçons au début du chemin.
     * @param troncon le troncon à ajouter
     */
    public void ajouterTronconDebut(Troncon troncon) {
        listeTroncons.addFirst(troncon);
    }

    //
    // Accessor methods
    //
    /**
     * Donne la liste de tronçons constituant le chemin.
     * @return la liste de tronçons constituant le chemin.
     */
    public LinkedList<Troncon> getListeTroncons() {
        return listeTroncons;
    }


    //
    // Other methods
    //

    /**
     * Donne le noeud de départ du chemin, soit le premier noeud du premier tronçon du chemin.
     * @return le noeud de départ du chemin.
     */
    public Noeud getDepart() {
        return listeTroncons.getFirst().getDepart();
    }

    /**
     * Donne le noeud d'arrivée du chemin, soit le dernier noeud du dernier tronçon du chemin.
     * @return le noeud de d'arrivée du chemin.
     */
    public Noeud getArrivee() {
        return listeTroncons.getLast().getArrivee();
    }

}
