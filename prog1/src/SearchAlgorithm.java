import java.util.List;

/**
 * Defines an abstract class for shared behavior of search algorithms.
 * 
 * @author Carmen St. Jean
 *
 */
public abstract class SearchAlgorithm {
	public enum Heuristic {
		H0,
		H1,
		H2,
		H3
	}
		
    public static SearchAlgorithm algorithm;
    public static Heuristic heuristic = Heuristic.H1;
    
    protected final int BEFORE = -1;
    protected final int EQUAL = 0;
    protected final int AFTER = 1;
    
    protected Node initialNode;

    protected int nodesGenerated = 0;
    protected int nodesExpanded = 0;
    
    public SearchAlgorithm(State initialState) {
        this.initialNode = new Node(initialState, null, 0, 1);
        
        algorithm = this;
    }
    
    public abstract Solution search();
    protected abstract List<Node> expand(Node node);    
    protected abstract int compareTo(Node n1, Node n2);
}
