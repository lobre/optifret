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

    /**
     * @param n_depart  le noeud de départ
     * @param n_arrivee le noeud d'arrivée
     * @param plan      le plan sur lequel le chemin est calculé
     * @return le plus court chemin entre les deux noeuds calculé selon l'algorithme de Dijkstra
     *         en prenant en compte la vitesse.
     */
    public static Chemin dijkstra_c(Noeud n_depart, Noeud n_arrivee, Plan plan) {
        if (plan == null || n_depart == null || n_arrivee == null)
            throw new IllegalArgumentException();
        if (n_depart.getM_id() == n_arrivee.getM_id())
            return new Chemin();
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
        noeudsVisites.add(depart);

        //On visite les noeuds non visités jusqu'à visiter le noeud cible
        while (dernierNoeudVisite.get_id() != n_arrivee.getM_id()) {
            if (dernierNoeudVisite.getM_poids() == Double.POSITIVE_INFINITY) {
                throw new SecurityException("Echec de Dijkstra : noeud d'arrivee inaccessible.");
            }
            //On met à jour la distance des noeuds accessibles depuis le dernier noeud accédé
            for (Troncon troncon : dernierNoeudVisite.getM_noeud().getM_troncons()) {
                for (NoeudPondere noeudNonVisite : noeudsNonVisites) {
                    if (noeudNonVisite.get_id() == troncon.getArrivee().getM_id()) {
                        //On met à jour le poids du noeud s'il est meilleur que l'ancien
                        float poidsTroncon = troncon.getM_longueur() / troncon.getM_vitesse();
                        if (dernierNoeudVisite.getM_poids() + poidsTroncon < noeudNonVisite.getM_poids()) {
                            noeudNonVisite.setM_poids(dernierNoeudVisite.getM_poids() + poidsTroncon);
                            noeudNonVisite.setM_rejointDepuis(troncon);
                        }
                    }
                }
            }
            //On visite le noeud le plus proche
            NoeudPondere plusProche = noeudLePlusProche(noeudsNonVisites);
            noeudsNonVisites.remove(plusProche);
            noeudsVisites.addLast(plusProche);
            dernierNoeudVisite = plusProche;
        }

        //On reconstruit le chemin
        Chemin pcchemin = new Chemin();
        NoeudPondere noeudParcouru = dernierNoeudVisite;
        while (noeudParcouru.get_id() != depart.get_id()) {
            pcchemin.ajouterTronconDebut(noeudParcouru.getM_rejointDepuis());
            int idOrigine = noeudParcouru.getM_rejointDepuis().getDepart().getM_id();
            noeudParcouru = noeudOrigine(noeudsVisites, idOrigine);
        }
        /*System.out.println("Chemin entre "+n_depart.getId() +" et "+n_arrivee.getId());
        for (Troncon troncon : pcchemin.getListeTroncons()) {
            System.out.println("Départ : "+troncon.getDepart().getId() + ",arrivée : "+troncon.getArrivee().getId());
        }
        System.out.println("Poids du noeud : "+ dernierNoeudVisite.getM_poids());*/
        return pcchemin;
    }

    private static NoeudPondere noeudLePlusProche(LinkedList<NoeudPondere> noeudsNonVisites) {
        NoeudPondere plusProche = noeudsNonVisites.getFirst();
        for (NoeudPondere noeudsNonVisite : noeudsNonVisites) {
            if (noeudsNonVisite.getM_poids() < plusProche.getM_poids()) {
                plusProche = noeudsNonVisite;
            }
        }
        return plusProche;
    }

    private static NoeudPondere noeudOrigine(LinkedList<NoeudPondere> noeudsVisites, int idOrigine) {
        Iterator<NoeudPondere> it = noeudsVisites.iterator();
        NoeudPondere retour = null;
        while (it.hasNext()) {
            NoeudPondere x = it.next();
            if (x.get_id() == idOrigine) {
                retour = x;
                break;
            }
        }
        return retour;
    }
}
