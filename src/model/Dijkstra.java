package model;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: karimalaoui
 * Date: 05/12/2013
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class Dijkstra {
    //Entrée : noeud départ, noeud arrivée, plan
    //Sortie : chemin (objet chemin)

    public Chemin dijkstra_c(Noeud depart, Noeud arrivee, Plan plan){
        LinkedList<NoeudPondere> noeudsVisites = new LinkedList<NoeudPondere>();
        LinkedList<NoeudPondere> noeudsNonVisites;
        //On remplit la liste des noeuds non visités
        for (Noeud noeud : plan.getM_noeuds()) {
            noeudsNonVisites.addLast(new NoeudPondere(noeud));
        }
        //On visite les noeuds non visités jusqu'à visiter le noeud cible
        Integer dernierNoeudVisite = -1;
        while (dernierNoeudVisite != arrivee.getM_id()){
            //On met à jour la distance des noeuds accessibles
            for();
            //On cherche le noeud accessible le plus proche

            //On choisit le noeud le plus proche

        }
    }

}
