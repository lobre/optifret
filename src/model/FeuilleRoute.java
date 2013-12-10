package model;

import model.tsp.SolutionState;
import model.tsp.TSP;

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


    public FeuilleRoute(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins, DemandeLivraison demandeLivraison) {
        m_demandeLivraison = demandeLivraison;
        m_chemins = new LinkedList<>();
        switch (tsp.getSolutionState()) {
            case INCONSISTENT:
                fillWithBlank(tsp);
                break;
            case OPTIMAL_SOLUTION_FOUND:
                fill(tsp, chemins);
                break;
            case SOLUTION_FOUND:
                fill(tsp, chemins);
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
    private void fill(TSP tsp, Map<Integer, Map<Integer, Chemin>> chemins) {
        int[] tspNext = tsp.getNext();
        int idEntrepot = m_demandeLivraison.getEntrepot().getM_id();
        int from = idEntrepot;
        int to = tspNext[from];
        do {
            m_chemins.add(chemins.get(from).get(to));
            from = to;
            to = tspNext[from];
        } while (from != idEntrepot);
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

}
