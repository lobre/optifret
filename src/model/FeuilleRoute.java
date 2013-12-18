package model;

import model.tsp.SolutionState;
import model.tsp.TSP;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class FeuilleRoute
 */
public class FeuilleRoute {
    private static final int TEMPS_ENTRE_DEUX_LIVRAISONS = 600;

    public enum EtatFeuille {RESOLU, SOLUBLE, INSOLUBLE}

    ;
    //
    // Fields
    //
    private final DemandeLivraison m_demandeLivraison;
    private final List<Chemin> m_chemins;
    private LinkedList<Livraison> m_livraisonsOrdonnees;
    private EtatFeuille m_etat = EtatFeuille.RESOLU;
    private final List<Livraison> m_reSchedule;

    //
    // Methods
    //

    /**
     * TODO : javadoc here
     *
     * @param tsp
     * @param chemins
     * @param matches
     * @param cost
     * @param demandeLivraison
     */
    public FeuilleRoute(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, int[] matches, int[][] cost, DemandeLivraison demandeLivraison) {
        m_demandeLivraison = demandeLivraison;
        m_chemins = new LinkedList<>();
        m_reSchedule = new ArrayList<>();
        m_livraisonsOrdonnees = new LinkedList<>();
        switch (tsp.getSolutionState()) {
            case INCONSISTENT:
                m_etat = EtatFeuille.INSOLUBLE;
                break;
            case OPTIMAL_SOLUTION_FOUND:
                fill(tsp, chemins, matches, cost);
                break;
            case SOLUTION_FOUND:
                fill(tsp, chemins, matches, cost);
                break;
            case NO_SOLUTION_FOUND:
                m_etat = EtatFeuille.INSOLUBLE;
                break;
            default:
                throw new EnumConstantNotPresentException(SolutionState.class, tsp.getSolutionState().name());
        }
    }

    private boolean contains(PlageHoraire plageHoraire, Heure heure) {
        return (heure.estAvant(plageHoraire.getHeureFin()) && plageHoraire.getHeureDebut().estAvant(heure));
    }

    /**
     * Fixe de manière non triviale les horaires des livraisons dans les plages voulues.
     * - Si une livraison entre dans la plage voulue, elle entre dans la plage voulue.
     * - Si une livraison n'entre pas dans la plage voulue :
     * - si la plage suivante est directement consécutive à la plage voulue, la livraison empiètera sur la plage suivante.
     * - si la plage suivante n'est pas directement consécutive à la plage voulue :
     * toutes les livraisons dépassant de cette plage (voulue) seront programmées directement après ladite plage.
     * Cas limite : si trop de livraisons sont reprogrammées, elles peuvent empiéter sur une plage existante.
     * - Une livraison ne peut pas êre programmée avant le début de la plage indiquée.
     * Dans ce cas, la livraison sera programmée pour la première heure disponible de la plage.
     *
     * @param tsp     la TSP à la base du calcul du trajet.
     * @param matches tableau des correspondances entre les index des vertices de la tsp et des noeuds du modèle.
     * @param cost    tableau des coûts des chemins optimaux entre deux vertices du graphe fondant la TSP.
     */
    private void fixerLesHeuresDeLivraison(TSP tsp, int[] matches, int[][] cost) {
        int pH = 0;
        Duree tempsEntreDeuxLivraisons = new Duree(TEMPS_ENTRE_DEUX_LIVRAISONS);
        boolean estSoluble = true;
        // Résolution du cas initial
        Chemin chemin = m_chemins.get(1);
        PlageHoraire plageHoraire = m_demandeLivraison.getPlagesHoraires().get(pH);
        Noeud depart = chemin.getDepart();
        Livraison livraison = getLivraisonPourUnNoeudEtUnePlage(depart, plageHoraire);
        Heure heureLivraison = plageHoraire.getHeureDebut();
        livraison.setHeureLivraison(heureLivraison);
        m_livraisonsOrdonnees.add(livraison);
        Heure departDerniereLivraison = tempsEntreDeuxLivraisons.ajouterA(heureLivraison);
        for (int i = 2; i < m_chemins.size(); i++) {
            // i représente le cardinal de la livraison dans la séquence des livraisons de la journée.
            // i==0 <--> départ de l'entrepôt. Ce cas est exclu.
            // i==1 <--> Première livraison. On postule que l'heure de cette livraison est l'heure de début de sa plage.
            chemin = m_chemins.get(i);
            depart = chemin.getDepart();
            plageHoraire = m_demandeLivraison.getPlagesHoraires().get(pH);
            livraison = getLivraisonPourUnNoeudEtUnePlage(depart, plageHoraire);
            if (livraison == null) {
                // On cherche cette livraison dans une autre plage.
                if (++pH < m_demandeLivraison.getPlagesHoraires().size()) {
                    for (; pH < m_demandeLivraison.getPlagesHoraires().size() && m_demandeLivraison.getPlagesHoraires().get(pH) != null && livraison == null; pH++) {
                        plageHoraire = m_demandeLivraison.getPlagesHoraires().get(pH);
                        livraison = getLivraisonPourUnNoeudEtUnePlage(depart, plageHoraire);
                    }
                    pH--;
                } else
                    livraison = null;
                if (livraison == null) {
                    m_etat = EtatFeuille.INSOLUBLE;
                    break;
                }
            }
            heureLivraison = new Duree(cost[getReverseMatch(chemin.getDepart().getId(), matches)]
                    [getReverseMatch(chemin.getArrivee().getId(), matches)]).ajouterA(departDerniereLivraison);
            if (plageHoraire.getHeureFin().estAvant(heureLivraison)) {
                // Livraison en retard : on prévient la secrétaire du retard et du fait qu'il faut reschedule cette livraison.
                // On passe la feuille de route en état SOLUBLE car au moins une livraison dépasse de sa plage.
                m_etat = EtatFeuille.SOLUBLE;
                // Insertion dans la liste des livraisons reprogrammées.
                m_reSchedule.add(livraison);
                // Indicateur de cas final.
                if (i == m_chemins.size() - 1) {
                    estSoluble = false;
                }
            } else if (heureLivraison.estAvant(plageHoraire.getHeureDebut())) {
                // Livraison en avance : on fixe l'heure de livraison à l'heure de début de la plage voulue.
                heureLivraison = plageHoraire.getHeureDebut();
            }
            livraison.setHeureLivraison(heureLivraison);
            departDerniereLivraison = tempsEntreDeuxLivraisons.ajouterA(heureLivraison);
            // Traitement de la solubilité de la feuille de route.
            if (i == m_chemins.size() - 1) {
                if (estSoluble && m_reSchedule.size() == 0) m_etat = EtatFeuille.RESOLU;
                else if (!estSoluble) m_etat = EtatFeuille.INSOLUBLE;
            }
            m_livraisonsOrdonnees.add(livraison);
        }
    }

    /**
     * Renvoie la première livraison d'une plage horaire située dans un noeud déterminé.
     *
     * @param noeud        le noeud dont on veut savoir s'il abrite une livraison dans la plage définie.
     * @param plageHoraire la plage horaire dont on veut savoir si elle abrite une livraison au noeud indiqué.
     * @return la livraison correspondant au critère, null si aucune livraison ne correspondait.
     */
    private Livraison getLivraisonPourUnNoeudEtUnePlage(Noeud noeud, PlageHoraire plageHoraire) {
        for (Livraison livraison : plageHoraire.getLivraisons()) {
            if (livraison.getAdresse().getId() == noeud.getId()) {
                return livraison;
            }
        }
        return null;
    }

    private void fill(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, int[] matches, int[][] cost) {
        int[] tspNext = tsp.getNext();
        int idEntrepot = getReverseMatch(m_demandeLivraison.getEntrepot().getId(), matches);
        int from = idEntrepot;
        int to = tspNext[from];
        do {
            m_chemins.add(chemins.get(matches[from]).get(matches[to]));
            from = to;
            to = tspNext[from];
        } while (from != idEntrepot && to != idEntrepot);
        if (to == idEntrepot) {
            m_chemins.add(chemins.get(matches[from]).get(matches[to]));
        }
        fixerLesHeuresDeLivraison(tsp, matches, cost);
    }

    //
    // Accessor methods
    //
    public List<Chemin> getChemins() {
        return m_chemins;
    }

    public LinkedList<Livraison> getLivraisonsOrdonnees() {
        return m_livraisonsOrdonnees;
    }

    public List<Livraison> getReSchedule() {
        return m_reSchedule;
    }

    public EtatFeuille getEtatFeuille() {
        return m_etat;
    }

    //
    // Other methods
    //

    private int getReverseMatch(int idPoint, int[] matches) {
        for (int i = 0; i < matches.length; i++) {
            if (matches[i] == idPoint)
                return i;
        }
        return -1;
    }


    public ArrayList<Noeud> getNodes() {
        ArrayList<Noeud> noeuds = new ArrayList<>();
        for (Chemin c : m_chemins) {
            noeuds.add(c.getArrivee());
        }
        return noeuds;
    }

    public ArrayList<Livraison> getLivraisons() {
        ArrayList<Livraison> livraisons = new ArrayList<>();
        for (Noeud noeud : getNodes()) {
            if (noeud.hasLivraison()) {
                livraisons.add(noeud.getLivraison());
            }
        }
        return livraisons;
    }
    public ArrayList<Troncon> getTroncons() {
        ArrayList<Troncon> troncons = new ArrayList<>();
        for (Chemin chemin : m_chemins) {
            troncons.addAll(chemin.getListeTroncons());
        }
        return troncons;
    }


    private class Duree {
        private int m_secondes;

        private Duree(int secondes) {
            m_secondes = secondes;
        }

        /**
         * Ajoute la durée this à l'heure passée en paramètre. Ne gère pas les chevauchements des jours.
         *
         * @param heure l'heure à laquelle on veut ajouter la durée this.
         * @return l'heure heure à laquelle on a ajouté la durée this.
         */
        public Heure ajouterA(Heure heure) {
            int secondes = m_secondes + heure.getTotalSeconds();
            int minutes = secondes / 60;
            int heures = minutes / 60;
            secondes %= 60;
            minutes %= 60;
            return new Heure(heures, minutes, secondes);
        }
    }
}
