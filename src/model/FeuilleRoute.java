package model;

import model.tsp.SolutionState;
import model.tsp.TSP;

import java.util.*;

/**
 * Class FeuilleRoute
 */
public class FeuilleRoute {
    private enum EtatFeuille {RESOLU, SOLUBLE, INSOLUBLE, INCERTAIN};
    //
    // Fields
    //
    private final DemandeLivraison m_demandeLivraison;
    private final List<Chemin> m_chemins;
    private LinkedList<Livraison> m_livraisonsOrdonnees;
    private EtatFeuille m_etat = EtatFeuille.INCERTAIN;
    private final Map<Livraison, PlageHoraire> m_reSchedule;


    public FeuilleRoute(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, int[] matches, int[][] cost, DemandeLivraison demandeLivraison) {
        m_demandeLivraison = demandeLivraison;
        m_chemins = new LinkedList<>();
        m_reSchedule = new HashMap<>();
        m_livraisonsOrdonnees = new LinkedList<>();
        switch (tsp.getSolutionState()) {
            case INCONSISTENT:
                fillWithBlank(tsp);
                break;
            case OPTIMAL_SOLUTION_FOUND:
                fill(tsp, chemins, matches, cost);
                break;
            case SOLUTION_FOUND:
                fill(tsp, chemins, matches, cost);
                break;
            case NO_SOLUTION_FOUND:
                fillWithBlank(tsp);
                break;
            default:
                throw new EnumConstantNotPresentException(SolutionState.class, tsp.getSolutionState().name());
        }
    }

    //
    // Methods
    //
    private boolean contains(PlageHoraire plageHoraire, Heure heure) {
        return (heure.estAvant(plageHoraire.getHeureFin()) && plageHoraire.getHeureDebut().estAvant(heure));
    }

    private void fixerLesHeuresDeLivraison(TSP tsp, int[] matches, int[][] cost) {
        int pH = 0;
        Duree tempsEntreDeuxLivraisons = new Duree(600); // TODO : passer en constante
        boolean estSoluble = true;
        // Résolution du cas initial
        Chemin chemin = m_chemins.get(1);
        PlageHoraire plageHoraire = m_demandeLivraison.getM_plagesHoraires().get(pH);
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
            plageHoraire = m_demandeLivraison.getM_plagesHoraires().get(pH); // TODO : voir si ça sert encore
            livraison = getLivraisonPourUnNoeudEtUnePlage(depart, plageHoraire);
            if (livraison == null) {
                // On cherche cette livraison dans une autre plage.
                if (++pH < m_demandeLivraison.getM_plagesHoraires().size()) {
                    for (; pH < m_demandeLivraison.getM_plagesHoraires().size() && m_demandeLivraison.getM_plagesHoraires().get(pH) != null && livraison == null; pH++) {
                        plageHoraire = m_demandeLivraison.getM_plagesHoraires().get(pH);
                        livraison = getLivraisonPourUnNoeudEtUnePlage(depart, plageHoraire);
                    }
                    pH--;
                } else
                    livraison = null;
                if (livraison == null) {
                    m_etat = EtatFeuille.INCERTAIN;
                    break;
                }
            }
            heureLivraison = new Duree(cost[getReverseMatch(chemin.getDepart().getM_id(), matches)]
                    [getReverseMatch(chemin.getArrivee().getM_id(), matches)]).ajouterA(departDerniereLivraison);
            if (plageHoraire.getHeureFin().estAvant(heureLivraison)) {
                // Livraison en retard : on prévient la secrétaire du retard et du fait qu'il faut reschedule cette livraison.
                // On passe la feuille de route en état SOLUBLE. Si le décalage est toujours présent en fin de journée, elle
                // passera en état INSOLUBLE. Dans le cas contraire, elle passera en RESOLU.
                // Pour l'heure, on ne tient pas compte de la plage due.
                m_etat = EtatFeuille.SOLUBLE;
                // TODO : prévenir la secrétaire.
                // On détermine la plage idéale de cette livraison.
                int pI = pH;
                PlageHoraire plageIdeale = null;
                if (++pI < m_demandeLivraison.getM_plagesHoraires().size())
                    plageIdeale = m_demandeLivraison.getM_plagesHoraires().get(pI);
                while ((plageIdeale != null) &&
                        !(plageIdeale.getHeureDebut().estAvant(heureLivraison) && heureLivraison.estAvant(plageIdeale.getHeureFin()))) {
                    if (heureLivraison.estAvant(plageIdeale.getHeureDebut())) {
                        heureLivraison = new Duree(1).ajouterA(plageIdeale.getHeureDebut());
                    } else if (pI < m_demandeLivraison.getM_plagesHoraires().size()) {
                        plageIdeale = m_demandeLivraison.getM_plagesHoraires().get(pI);
                        pI++;
                    } else plageIdeale = null;
                }
                if (plageIdeale == null) {
                    m_etat = EtatFeuille.INSOLUBLE;
                    return; // Un peu violent, mais signifie que cette livraison ne peut pas être effectuée dans la journée.
                    // TODO : check semantics
                }
                m_reSchedule.put(livraison, plageIdeale);
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
        for (Livraison livraison : plageHoraire.getM_livraisons()) {
            if (livraison.getAdresse().getM_id() == noeud.getM_id()) {
                return livraison;
            }
        }
        return null;
    }

    private void fill(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, int[] matches, int[][] cost) {
        int[] tspNext = tsp.getNext();
        int idEntrepot = getReverseMatch(m_demandeLivraison.getEntrepot().getM_id(), matches);
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

    private void fillWithBlank(TSP tsp) {
        // TODO : @Ahmed faut décider ce qu'on fait ici
    }

    //
    // Accessor methods
    //

    public List<Chemin> getChemins() {
        return m_chemins;
    }

    public LinkedList<Livraison> getM_livraisonsOrdonnees() {
        return m_livraisonsOrdonnees;
    }

    public Map<Livraison, PlageHoraire> getM_reSchedule() {
        return m_reSchedule;
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


    public ArrayList<Noeud> getNodes(){
        ArrayList<Noeud> noeuds=new ArrayList<>();
        int i=0;
          for( Chemin c : m_chemins){
              if((i>0) && c.getDepart().getM_id()!=noeuds.get(i).getM_id()){
                noeuds.add(c.getDepart());
                  System.out.println(c.getDepart().getM_id());
              }
              noeuds.add(c.getArrivee());
              System.out.println(c.getArrivee().getM_id());

          }
        return noeuds;
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
