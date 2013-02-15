/**
 * Defines a class to represent greedy search. Greedy is a best-first search
 * that sorts the open list based on h(n), the heuristic function.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Greedy extends BestFirstSearch {
	/**
	 * Creates the greedy search algorithm and the initial node from the
	 * specified initial state.
	 * 
	 * @param initialState
	 *            The initial state of the world.
	 */
	public Greedy(State initialState) {
		super(initialState);
	}

	@Override
	public int compare(Node n1, Node n2) {
		if (n1.getH() < n2.getH()) {
			return BEFORE;
		}

		if (n1.getH() > n2.getH()) {
			return AFTER;
		}

		return EQUAL;
	}
}
