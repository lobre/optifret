package model;

import java.util.Iterator;
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
    //Cette version ne prend pas en compte la vitesse, mais seulement la distance

    public static Chemin dijkstra_c(Noeud n_depart, Noeud n_arrivee, Plan plan){
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
                for (NoeudPondere noeudNonVisite : noeudsNonVisites) {
                    if (noeudNonVisite.get_id() == troncon.getArrivee().getM_id()){
                        //On met à jour la distance si elle est meilleure que l'ancienne
                        if (dernierNoeudVisite.getM_poids()+troncon.getM_longueur() < noeudNonVisite.getM_poids()){
                            noeudNonVisite.setM_poids(dernierNoeudVisite.getM_poids()+troncon.getM_longueur());
                            noeudNonVisite.setM_rejointDepuis(troncon);
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

        //On reconstruit le chemin
        Chemin pcchemin = new Chemin();
        NoeudPondere noeudParcouru = dernierNoeudVisite;
        while (noeudParcouru.get_id() != depart.get_id()){
            pcchemin.ajouterTronconDebut(noeudParcouru.getM_rejointDepuis());
            int idOrigine = noeudParcouru.getM_rejointDepuis().getDepart().getM_id();

            //On recherche le noeud qui a mené au noeudParcouru
            Iterator<NoeudPondere> it = noeudsVisites.iterator();
            while (it.hasNext()) {
                NoeudPondere x = it.next();
                if (x.get_id() == idOrigine){
                    noeudParcouru = x;
                }
            }
        }
        return pcchemin;
    }

}
