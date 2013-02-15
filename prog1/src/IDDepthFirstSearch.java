/**
 * Defines a class to represent depth-first iterative deepening search. This
 * means using iterative deepening on depth first search where nodes whose depth
 * exceeds the bounds are pruned (not added to the open list).
 * 
 * @author Carmen St. Jean
 * 
 */
public class IDDepthFirstSearch extends IDSearch {
	/**
	 * Creates the iterative deepening depth-first search algorithm and the
	 * initial node from the specified initial state.
	 * 
	 * @param initialState
	 *            The initial state of the world.
	 */
	public IDDepthFirstSearch(State initialState) {
		super(initialState);
	}

	@Override
	protected boolean boundsCheck(Node node, int bound) {
		return node.getDepth() < bound;
	}
}
