package model;


import java.util.Collection;
import java.util.LinkedList;

/**
 * Class Chemin
 */
public class Chemin {


    //
    // Fields
    //
    private final Collection<Troncon> listeTroncons = new LinkedList<Troncon>() ;

    //
    // Constructors
    //

    ;

    //
    // Methods
    //

    public void ajouterTroncon(Troncon troncon){
        //Vérifier que le troncon est bien ajoute a la fin (le type de liste peut etre a changer)
        listeTroncons.add(troncon);
    }

    //
    // Accessor methods
    //

    //
    // Other methods
    //

}
