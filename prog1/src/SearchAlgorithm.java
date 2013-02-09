import java.util.List;

/**
 * Defines an abstract class for shared behavior of search algorithms.
 * 
 * @author Carmen St. Jean
 *
 */
public abstract class SearchAlgorithm {
    public static SearchAlgorithm algorithm;
    
    protected final int BEFORE = -1;
    protected final int EQUAL = 0;
    protected final int AFTER = 1;
    
    protected Node initialNode;

    protected int nodesGenerated = 0;
    protected int nodesExpanded = 0;
    
    public SearchAlgorithm(State initialState) {
        this.initialNode = new Node(initialState, null, 0);
    }
    
    public abstract void search();
    protected abstract List<Node> expand(Node node);    
    protected abstract int compareTo(Node n1, Node n2);
}
