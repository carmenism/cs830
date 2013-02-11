/**
 * Defines a class to represent A* search. A* is a best-first search that sorts
 * the open list based on f(n) (where f(n) = g(n) + h(n)) with ties broken on
 * g(n).
 * 
 * @author Carmen St. Jean
 * 
 */
public class AStar extends BestFirstSearch {
	/**
	 * Creates the A* search algorithm and the initial node from the specified
	 * initial state.
	 * 
	 * @param initialState
	 *            The initial state of the world.
	 */
    public AStar(State initialState) {
        super(initialState);
    }
       
    @Override
    public int compare(Node n1, Node n2) {
        if (n1.getF() < n2.getF()) {
            return BEFORE;
        } else if (n1.getF() > n2.getF()) {
            return AFTER;
        }
        
        if (n1.getG() > n2.getG()) {
            return BEFORE;
        } else if (n1.getG() < n2.getF()) {
            return AFTER;
        }
        
        return EQUAL;
    }
}
