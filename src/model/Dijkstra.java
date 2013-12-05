package model;

import java.util.LinkedList;

public class Dijkstra {
    //Entrée : noeud départ, noeud arrivée, plan
    //Sortie : chemin (objet chemin)

    public Chemin dijkstra_c(Noeud depart, Noeud arrivee, Plan plan){
        LinkedList<NoeudPondere> noeudsVisites = new LinkedList<NoeudPondere>();
        LinkedList<NoeudPondere> noeudsNonVisites = new LinkedList<NoeudPondere>();
        //On remplit la liste des noeuds non visités
        for (Noeud noeud : plan.getM_noeuds()) {
            noeudsNonVisites.addLast(new NoeudPondere(noeud));
        }
        //On visite les noeuds non visités jusqu'à visiter le noeud cible
        Integer dernierNoeudVisite = -1;
        while (dernierNoeudVisite != arrivee.getM_id()){
            //On met à jour la distance des noeuds accessibles
            // for();
            //On cherche le noeud accessible le plus proche

            //On choisit le noeud le plus proche

        }

        // TODO : Change this. Added only to compile the rest of the app.
        return null;
    }

}
