import java.util.Comparator;
import java.util.List;

/**
 * Defines an abstract class for shared behavior of search algorithms.
 * 
 * @author Carmen St. Jean
 *
 */
public abstract class SearchAlgorithm implements Comparator<Node> {    
    protected final int BEFORE = -1;
    protected final int EQUAL = 0;
    protected final int AFTER = 1;
    
    protected final Node initialNode;

    protected int nodesGenerated = 0;
    protected int nodesExpanded = 0;
    
	/**
	 * Creates the search algorithm and the initial node from the specified
	 * initial state.
	 * 
	 * @param initialState
	 *            The initial state of the world.
	 */
    public SearchAlgorithm(State initialState) {
        this.initialNode = new Node(initialState, null, 0, 1);
    }
    
	/**
	 * Activates the search for the search algorithm.
	 * 
	 * @return The solution, if one was found (null otherwise).
	 */
    public abstract Solution search();
    
    /**
     * 
     * @param node
     * @return
     */
    protected abstract List<Node> expand(Node node);    

	/**
	 * Compares two nodes for sorting the open list (or potentially a list of
	 * children to be added to open list).
	 * 
	 * @param n1
	 *            The first node for comparison.
	 * @param n2
	 *            The second node for comparison.
	 * @return A negative number if n1 should be sorted before n2, a positive
	 *         number if n1 should be sorted after n2, and zero if the two nodes
	 *         are equal.
	 */
    public abstract int compare(Node n1, Node n2);
}
