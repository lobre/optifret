package tsp;

import solver.ResolutionPolicy;
import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.search.strategy.IntStrategyFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

/**
 * @author Christine Solnon, Jean-Guillaume Fages
 */

public class TSP {

	private int[] next;
	private int totalCost;
	private SolutionState state;
	private Graph graph;

	public TSP(Graph graph) {
		state = SolutionState.NO_SOLUTION_FOUND;
		this.graph = graph;
	}

	/**
	 * Searches for a tour in <code>graph</code> lower than <code>bound</code> and tries to prove its optimality in less than <code>timeLimit</code> milliseconds.
	 * @param timeLimit a limit of CPU time in milliseconds
	 * @param bound an upper bound on the total cost of the tour
	 * @return NO_SOLUTION_FOUND, if no tour lower than <code>bound</code> has been found within <code>timeLimit</code> milliseconds,<br>
	 * SOLUTION_FOUND, if a tour lower than <code>bound</code> has been found, but its optimality has not been proven within <code>timeLimit</code> milliseconds,<br> 
	 * OPTIMAL_SOLUTION_FOUND, if a tour lower than <code>bound</code> has been found, and its optimality has been proven,<br>
	 * or INCONSISTENT, if there does not exist a tour lower than <code>bound</code>.
	 */
	public SolutionState solve(int timeLimit, int bound) {
		int n = graph.getNbVertices();
		int minCost = graph.getMinArcCost();
		int maxCost = graph.getMaxArcCost();
		int[][] cost = graph.getCost();
		next = new int[n];
		Solver solver = new Solver();

		// Create variables
		// xNext[i] = vertex visited after i
		IntVar[] xNext = new IntVar[n];
		for (int i = 0; i < n; i++)
			xNext[i] = VariableFactory.enumerated("Next " + i, graph.getSucc(i), solver);
		// xCost[i] = cost of arc (i,xNext[i])
		IntVar[] xCost = VariableFactory.boundedArray("Cost ", n, minCost, maxCost, solver);
		// xTotalCost = total cost of the solution
		IntVar xTotalCost = VariableFactory.bounded("Total cost ", n*minCost, bound - 1, solver);

		// Add constraints
		for (int i = 0; i < n; i++) 
			solver.post(IntConstraintFactory.element(xCost[i], cost[i], xNext[i], 0, "none"));
		solver.post(IntConstraintFactory.circuit(xNext,0));
		solver.post(IntConstraintFactory.sum(xCost, xTotalCost));

		// limit CPU time
		SearchMonitorFactory.limitTime(solver,timeLimit);
		// set the branching heuristic (branch on xNext only by selecting smallest domains first)
		solver.set(IntStrategyFactory.firstFail_InDomainMin(xNext));
		// try to find and prove the optimal solution
		solver.findOptimalSolution(ResolutionPolicy.MINIMIZE,xTotalCost);
		// record solution and state
		if(solver.getMeasures().getSolutionCount()>0){
			if (solver.getSearchLoop().hasReachedLimit()) 
				state = SolutionState.SOLUTION_FOUND;
			else 
				state = SolutionState.OPTIMAL_SOLUTION_FOUND;
			for(int i=0;i<n;i++) next[i] = xNext[i].getValue();
			totalCost = xTotalCost.getValue();
		}
		else {
			if(solver.getSearchLoop().hasReachedLimit()) 
				state = SolutionState.NO_SOLUTION_FOUND;
			else 
				state = SolutionState.INCONSISTENT;
		}
		return state;
	}

	/**
	 * @return an array <code>next</code> such that <code>next[i]</code> gives the vertex visited just after <code>i</code> in the last computed solution
	 * @throws NullPointerException If <code>this.getSolutionState()</code> not in <code>{SOLUTION_FOUND, OPTIMAL_SOLUTION_FOUND}</code>
	 */
	public int[] getNext() throws NullPointerException {
		if ((state == SolutionState.NO_SOLUTION_FOUND) || (state == SolutionState.INCONSISTENT))
			throw new NullPointerException();
		return next;
	}

	/**
	 * @return the total cost of the last computed solution 
	 * @throws NullPointerException If <code>this.getSolutionState()</code> not in <code>{SOLUTION_FOUND, OPTIMAL_SOLUTION_FOUND}</code>
	 */
	public int getTotalCost() throws NullPointerException {
		if ((state == SolutionState.NO_SOLUTION_FOUND) || (state == SolutionState.INCONSISTENT))
			throw new NullPointerException();
		return totalCost;
	}

	/**
	 * @return NO_SOLUTION_FOUND if no solution has been found during the last call to solve,<br>
	 * SOLUTION_FOUND if a solution has been found during the last call to solve but its optimality has not been proven,<br>
	 * OPTIMAL_SOLUTION_FOUND if a solution has been found during the last call to solve, and its optimality proven,<br>
	 * INCONSISTENT if the last call to solve has proven that the problem does not have a solution. 
	 */
	public SolutionState getSolutionState() {
		return state;
	}

}
