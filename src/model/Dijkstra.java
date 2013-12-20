package model;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe permettant de trouver le plus court chemin entre deux noeuds en utilisant l'algorithme de Dijkstra.
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
        if (n_depart.getId() == n_arrivee.getId())
            return new Chemin();
        LinkedList<NoeudPondere> noeudsVisites = new LinkedList<NoeudPondere>();
        LinkedList<NoeudPondere> noeudsNonVisites = new LinkedList<NoeudPondere>();
        //On remplit la liste des noeuds non visités
        for (Noeud noeud : plan.getNoeuds().values()) {
            noeudsNonVisites.addLast(new NoeudPondere(noeud));
        }
        //On initialise le noeud de départ
        NoeudPondere depart = new NoeudPondere(n_depart);
        depart.setPoids(0.);
        NoeudPondere dernierNoeudVisite = depart;
        noeudsVisites.add(depart);

        //On visite les noeuds non visités jusqu'à visiter le noeud cible
        while (dernierNoeudVisite.get_id() != n_arrivee.getId()) {
            if (dernierNoeudVisite.getPoids() == Double.POSITIVE_INFINITY) {
                throw new SecurityException("Echec de Dijkstra : noeud d'arrivee inaccessible.");
            }
            //On met à jour la distance des noeuds accessibles depuis le dernier noeud accédé
            for (Troncon troncon : dernierNoeudVisite.getNoeud().getTroncons()) {
                for (NoeudPondere noeudNonVisite : noeudsNonVisites) {
                    if (noeudNonVisite.get_id() == troncon.getArrivee().getId()) {
                        //On met à jour le poids du noeud s'il est meilleur que l'ancien
                        float poidsTroncon = troncon.getLongueur() / troncon.getVitesse();
                        if (dernierNoeudVisite.getPoids() + poidsTroncon < noeudNonVisite.getPoids()) {
                            noeudNonVisite.setPoids(dernierNoeudVisite.getPoids() + poidsTroncon);
                            noeudNonVisite.setRejointDepuis(troncon);
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
            pcchemin.ajouterTronconDebut(noeudParcouru.getRejointDepuis());
            int idOrigine = noeudParcouru.getRejointDepuis().getDepart().getId();
            noeudParcouru = noeudOrigine(noeudsVisites, idOrigine);
        }
        return pcchemin;
    }

    private static NoeudPondere noeudLePlusProche(LinkedList<NoeudPondere> noeudsNonVisites) {
        NoeudPondere plusProche = noeudsNonVisites.getFirst();
        for (NoeudPondere noeudsNonVisite : noeudsNonVisites) {
            if (noeudsNonVisite.getPoids() < plusProche.getPoids()) {
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
