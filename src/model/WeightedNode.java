package model;

/**
 * Created with IntelliJ IDEA.
 * User: karimalaoui
 * Date: 05/12/2013
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */

public class WeightedNode {
    private static Noeud m_noeud;
    private static Double m_weight;
    private static Integer m_previous;

    //Constructor
    WeightedNode(Noeud noeud){
        this.m_noeud = noeud;
        this.m_previous = -1;
        this.m_weight = Double.POSITIVE_INFINITY;
    }

    //Getter-setter

    public static void setM_weight(Double m_weight) {
        WeightedNode.m_weight = m_weight;
    }
}
