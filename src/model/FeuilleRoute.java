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

    //
    // Fields
    //
    private final DemandeLivraison m_demandeLivraison;
    private final List<Chemin> m_chemins;


    public FeuilleRoute(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, int[] matches, DemandeLivraison demandeLivraison) {
        m_demandeLivraison = demandeLivraison;
        m_chemins = new LinkedList<>();
        switch (tsp.getSolutionState()) {
            case INCONSISTENT:
                fillWithBlank(tsp);
                break;
            case OPTIMAL_SOLUTION_FOUND:
                fill(tsp, chemins, matches);
                break;
            case SOLUTION_FOUND:
                fill(tsp, chemins, matches);
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
    private void fill(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, int[] matches) {
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

    public ArrayList<Troncon> getTroncons() {
        ArrayList<Troncon> troncons = new ArrayList<Troncon>();
        for (Chemin chemin : m_chemins) {
            troncons.addAll(chemin.getListeTroncons());
        }
        return troncons;
    }

}
