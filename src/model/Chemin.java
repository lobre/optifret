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

    ;

    //
    // Methods
    //
    public int getLongueur() {
        int longueur = 0;
        for (Troncon troncon : listeTroncons) {
            longueur += troncon.getM_longueur();
        }
        return longueur;
    }

    public void ajouterTronconFin(Troncon troncon) {
        listeTroncons.addLast(troncon);
    }

    public void ajouterTronconDebut(Troncon troncon) {
        listeTroncons.addFirst(troncon);
    }

    //
    // Accessor methods
    //

    public LinkedList<Troncon> getListeTroncons() {
        return listeTroncons;
    }


    //
    // Other methods
    //

}
