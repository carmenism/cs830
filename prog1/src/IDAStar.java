/**
 * Defines a class to represent IDA* (iterative deepening A*) search. IDA* means
 * using iterative deepening on depth first search where nodes whose f value
 * exceeds the bounds are pruned (not added to the open list).
 * 
 * @author Carmen St. Jean
 * 
 */
public class IDAStar extends IDSearch {
	/**
	 * Creates the iterative deepening A* search algorithm and the initial node
	 * from the specified initial state.
	 * 
	 * @param initialState
	 *            The initial state of the world.
	 */
	public IDAStar(State initialState) {
		super(initialState);
	}

	@Override
	protected boolean boundsCheck(Node node, int bound) {
		return node.getF() < bound;
	}
}
