import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Defines behavior for a depth first search.
 * 
 * @author Carmen St. Jean 
 *
 */
public class DepthFirstSearch extends SearchAlgorithm {
    public DepthFirstSearch(State initialState) {
        super(initialState);
        
        algorithm = this;
    }
    
    @Override
    public void search() {
/*        search(null);
    }
        
    public void search(Integer maxDepth) {*/
        Stack<Node> openList = new Stack<Node>();
        
        // Place the node with the starting state on the open list.
        openList.push(initialNode);
        nodesGenerated++;
        
        HashMap<String, State> cycleChecker = new HashMap<String, State>();
                
        while (true) {
            if (openList.isEmpty()) {
                // Failure - abort.
                System.err.println("Failure - open list is empty.");
                System.exit(1);
            }
            
            Node node = openList.pop();
            
            if (node.isGoal()) {
                // The goal was found, so print the path and return.
                node.printPath();
                
                System.out.println(nodesGenerated + " nodes generated");
                System.out.println(nodesExpanded + " nodes expanded");
                
                return;
            } else {
                // Expand the node.
                cycleChecker.put(node.getState().toString(), node.getState());
                nodesExpanded++;
                
                for (Node child : expand(node)) {                    
                    // Make sure the generated node is not a duplicate.
                    if (!cycleChecker.containsKey(child.getState().toString())) {
                        openList.push(child);
                        nodesGenerated++;
                    }
                }
            }
        }
    }
    
    @Override
    protected List<Node> expand(Node node) {
        List<Node> children = node.expand();
        
        Collections.sort(children);
        
        return children;
    }

    @Override
    protected int compareTo(Node n1, Node n2) {
        // Sort by largest to smallest heuristic.
        if (n1.getH() > n2.getH()) {
            return BEFORE;
        }
        
        if (n1.getH() < n2.getH()) {
            return AFTER;
        }
        
        return EQUAL;
    }
}
