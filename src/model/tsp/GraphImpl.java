package model.tsp;

import java.util.HashMap;
import java.util.Map;

public class GraphImpl implements Graph {
    private final Map<Integer, Map<Integer, Integer>> m_costs;

    public GraphImpl() {
        m_costs = new HashMap<>();
    }

    @Override
    public int getMaxArcCost() {
        int vRet = Integer.MIN_VALUE;
        for (Map<Integer, Integer> trajets : m_costs.values()) {
            for (Integer cost : trajets.values()) {
                if (cost.intValue() > vRet)
                    vRet = cost.intValue();
            }
        }
        return vRet;
    }

    @Override
    public int getMinArcCost() {
        int vRet = Integer.MAX_VALUE;
        for (Map<Integer, Integer> trajets : m_costs.values()) {
            for (Integer cost : trajets.values()) {
                if (cost.intValue() < vRet)
                    vRet = cost.intValue();
            }
        }
        return vRet;
    }

    @Override
    public int getNbVertices() {
        int vRet = 0;
        for (Map<Integer, Integer> trajets : m_costs.values()) {
            vRet += trajets.size();
        }
        return vRet;
    }

    @Override
    public int[][] getCost() {
        int[][] vRet = new int[400][400];
        // On admet qu'aucune ville n'est dans la liste des destinations si elle n'est pas dans celle des départs.
        // On pourrait penser que c'est faux, mais si ça l'était, notre pauvre livreur pourrait se retrouver bloqué,
        // donc remerciez-moi de parer à cette éventualité. Pas d'impasses chez nous !
        // En revanche, on fait un truc hideux : on cherche le point de livraison qui a l'identifiant le plus gros, et on fout
        for (Integer depart : m_costs.keySet()) {
            for (Integer destination : m_costs.keySet()) {
                Integer longueur = m_costs.get(depart).get(destination);
                vRet[depart][destination] = longueur == null ? getMaxArcCost() + 1 : longueur.intValue();
            }
        }
        return vRet;
    }

    @Override
    public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException {
        Map<Integer, Integer> successorsMap = m_costs.get(i);
        int[] vRet = new int[successorsMap.size()];
        int j = 0;
        for (Integer successor : successorsMap.keySet()) {
            vRet[j++] = successor;
        }
        return vRet;
    }

    @Override
    public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
        return m_costs.get(i).size();
    }

    public Map<Integer, Map<Integer, Integer>> getCosts() {
        return m_costs;
    }
}
