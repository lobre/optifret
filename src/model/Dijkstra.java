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

    public Chemin dijkstra_c(Noeud n_depart, Noeud n_arrivee, Plan plan){
        LinkedList<NoeudPondere> noeudsVisites = new LinkedList<NoeudPondere>();
        LinkedList<NoeudPondere> noeudsNonVisites = new LinkedList<NoeudPondere>();
        //On remplit la liste des noeuds non visités
        for (Noeud noeud : plan.getM_noeuds().values()) {
            noeudsNonVisites.addLast(new NoeudPondere(noeud));
        }
        //On initialise le noeud de départ
        NoeudPondere depart = new NoeudPondere(n_depart);
        depart.setM_poids(0.);
        NoeudPondere dernierNoeudVisite = depart;

        //On visite les noeuds non visités jusqu'à visiter le noeud cible
        while (dernierNoeudVisite.get_id() != n_arrivee.getM_id()){
            //On met à jour la distance des noeuds accessibles depuis le dernier noeud accédé
            for (Troncon troncon : dernierNoeudVisite.getM_noeud().getM_troncons()) {
                //Cas des noeuds pas encore visités
                for (NoeudPondere noeudsNonVisite : noeudsNonVisites) {
                    if (noeudsNonVisite.get_id() == troncon.getArrivee().getM_id()){
                        NoeudPondere tampon = noeudsNonVisite;
                        noeudsNonVisites.remove(noeudsNonVisite);
                        tampon.setM_poids(dernierNoeudVisite.getM_poids()+troncon.getM_longueur());
                        noeudsVisites.addLast(tampon);
                    }
                }
                //Cas des noeuds déjà visités
                for (NoeudPondere noeudsVisite : noeudsVisites) {
                    if (noeudsVisite.get_id() == troncon.getArrivee().getM_id()){
                        //Dans le cas où la nouvelle facon d'acceder au noeud est plus efficace
                        if (dernierNoeudVisite.getM_poids()+troncon.getM_longueur() < noeudsVisite.getM_poids()){
                            noeudsVisite.setM_precedent(dernierNoeudVisite.getM_noeud());
                            noeudsVisite.setM_poids(dernierNoeudVisite.getM_poids()+troncon.getM_longueur());
                        }
                    }
                }
            }

            //On cherche le noeud accessible le plus proche
            NoeudPondere plusProche = noeudsNonVisites.getFirst();
            for (NoeudPondere noeudsNonVisite : noeudsNonVisites) {
                if (noeudsNonVisite.getM_poids() < plusProche.getM_poids()){
                    plusProche = noeudsNonVisite;
                }
            }
            //On l'ajoute à la liste des noeuds visités
            noeudsNonVisites.remove(plusProche);
            noeudsVisites.addLast(plusProche);
            //On met à jour le dernier noeud visité
            dernierNoeudVisite = plusProche;
        }
        //TODO
        //On reconstruit le chemin
        Chemin pcchemin = new Chemin();
        NoeudPondere noeudParcouru = dernierNoeudVisite;
        while (noeudParcouru.get_id() != depart.get_id()){
            pcchemin.ajouterTronconDebut();
        }
        return pcchemin;
    }

}
