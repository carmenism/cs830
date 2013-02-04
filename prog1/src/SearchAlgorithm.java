import java.util.HashMap;

/**
 * Defines an abstract class for shared behavior of search algorithms.
 * 
 * @author Carmen St. Jean
 *
 */
public abstract class SearchAlgorithm {
	protected Node initialNode;
	protected HashMap<String, Node> closedList;
	protected HashMap<String, Node> seenList = new HashMap<String, Node>();

	protected int nodesGenerated = 0;
	protected int nodesExpanded = 0;
	
	public SearchAlgorithm(State initialState) {
		this.initialNode = new Node(initialState, null, 0);
	}
	
	public abstract void search();
}
