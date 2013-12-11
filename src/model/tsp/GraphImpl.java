package model.tsp;

import java.util.HashMap;
import java.util.Map;

public class GraphImpl implements Graph {
    private final Map<Integer, Map<Integer, Integer>> m_costs;
    private int[] m_matches;
    private int m_maxArcCost = -1;
    private int m_minArcCost = -1;
    private int[][] m_cost = null;

    public GraphImpl() {
        m_costs = new HashMap<>();
    }

    @Override
    public int getMaxArcCost() {
        if (m_maxArcCost == -1) {
            int vRet = Integer.MIN_VALUE;
            for (Map<Integer, Integer> trajets : m_costs.values()) {
                for (Integer cost : trajets.values()) {
                    if (cost.intValue() > vRet)
                        vRet = cost.intValue();
                }
            }
            m_maxArcCost = vRet;
        }
        return m_maxArcCost;
    }

    @Override
    public int getMinArcCost() {
        if (m_minArcCost == -1) {
            int vRet = Integer.MAX_VALUE;
            for (Map<Integer, Integer> trajets : m_costs.values()) {
                for (Integer cost : trajets.values()) {
                    if (cost.intValue() < vRet)
                        vRet = cost.intValue();
                }
            }
            m_minArcCost = vRet;
        }
        return m_minArcCost;
    }

    @Override
    public int getNbVertices() {
        return m_costs.size();
    }

    @Override
    public int[][] getCost() {
        if (m_cost == null) {
            m_cost = new int[m_costs.size()][m_costs.size()];
            m_matches = new int[m_costs.size()];
            // On admet qu'aucune ville n'est dans la liste des destinations si elle n'est pas dans celle des départs.
            // On pourrait penser que c'est faux, mais si ça l'était, notre pauvre livreur pourrait se retrouver bloqué,
            // donc remerciez-moi de parer à cette éventualité. Pas d'impasses chez nous !
            int i = 0, j;
            for (Integer depart : m_costs.keySet()) {
                m_matches[i] = depart.intValue();
                j = 0;
                for (Integer destination : m_costs.keySet()) {
                    Integer longueur = m_costs.get(depart).get(destination);
                    m_cost[i][j++] = longueur == null ? getMaxArcCost() + 1 : longueur.intValue();
                }
                i++;
            }
        }
        return m_cost;
    }

    @Override
    public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException {
        Map<Integer, Integer> successorsMap = m_costs.get(m_matches[i]);
        if (successorsMap == null) return null;
        int[] vRet = new int[successorsMap.size()];
        int j = 0;
        for (Integer successor : successorsMap.keySet()) {
            vRet[j++] = getReverseMatch(successor.intValue());
        }
        return vRet;
    }

    @Override
    public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
        return m_costs.get(m_matches[i]).size();
    }

    public Map<Integer, Map<Integer, Integer>> getCosts() {
        return m_costs;
    }

    /**
     * Gets the id of the vertex pointed by idPoint in Graph internal structure.
     *
     * @param idPoint the Graph structure internal vertex id.
     * @return the Noeud.m_id of the vertex
     */
    private int getReverseMatch(int idPoint) {
        for (int i = 0; i < m_matches.length; i++) {
            if (m_matches[i] == idPoint)
                return i;
        }
        return -1;
    }

    public int[] getMatches() {
        return m_matches;
    }
}
