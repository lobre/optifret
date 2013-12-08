package model;


import java.util.LinkedList;

/**
 * Class Chemin
 */
public class Chemin {


    //
    // Fields
    //
    private final LinkedList<Troncon> listeTroncons = new LinkedList<Troncon>() ;

    //
    // Constructors
    //

    ;

    //
    // Methods
    //

    public void ajouterTronconFin(Troncon troncon){
        listeTroncons.addLast(troncon);
    }

    public void ajouterTronconDebut(Troncon troncon){
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
