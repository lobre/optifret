package model.tsp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GraphTest {

    /**
     * Checks that each vertex of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code>
     * has <code>degree</code> successors
     */
    @Test
    public void testDegree() {
        int nbVertices = 50;
        int degree = 8;
        int minArcCost = 1;
        int maxArcCost = 10;
        Graph g = new RegularGraph(nbVertices, degree, minArcCost, maxArcCost);
        for (int i = 0; i < g.getNbVertices(); i++)
            assertEquals(g.getNbSucc(i), degree);
    }

    /**
     * Checks that each arc of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code>
     * has a cost ranging between<code>minArcCost</code> and <code>maxArcCost</code>
     */
    @Test
    public void testArcCost() {
        int nbVertices = 50;
        int degree = 8;
        int minArcCost = 5;
        int maxArcCost = 10;
        Graph g = new RegularGraph(nbVertices, degree, minArcCost, maxArcCost);
        for (int i = 0; i < g.getNbVertices(); i++) {
            int[] succ = g.getSucc(i);
            for (int j = 0; j < g.getNbSucc(i); j++) {
                assertTrue(g.getCost()[i][succ[j]] >= minArcCost);
                assertTrue(g.getCost()[i][succ[j]] <= maxArcCost);
            }
        }
    }

    /**
     * Checks that for each couple of vertices <code>(i,j)</code> of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code>
     * such that <code>(i,j)</code> is not an arc, <code>getCost()[i][j]</code> returns <code>getMaxArcCost()+1</code>
     */
    @Test
    public void testNonArcCost() {
        int nbVertices = 50;
        int degree = 8;
        int minArcCost = 1;
        int maxArcCost = 10;
        Graph g = new RegularGraph(nbVertices, degree, minArcCost, maxArcCost);
        for (int i = 0; i < g.getNbVertices(); i++) {
            for (int j = 0; j < g.getNbVertices(); j++) {
                int[] succ = g.getSucc(i);
                int k = 0;
                while (k < g.getNbSucc(i) && succ[k] != j)
                    k++;
                if (k == g.getNbSucc(i))
                    assertEquals(g.getCost()[i][j], g.getMaxArcCost() + 1);
            }
        }
    }

}
