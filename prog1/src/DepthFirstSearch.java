import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * Defines behavior for a depth first search.
 * 
 * @author Carmen St. Jean 
 *
 */
public class DepthFirstSearch extends SearchAlgorithm {
	private int depthBounds = Integer.MAX_VALUE;
	
    public DepthFirstSearch(State initialState) {
        super(initialState);
    }
    
    public DepthFirstSearch(State initialState, int depthBounds) {
        super(initialState);
        
        this.depthBounds = depthBounds;
    }
        
    @Override
    public boolean search() {
        Stack<Node> openList = new Stack<Node>();
        
        // Place the node with the starting state on the open list.
        openList.push(initialNode);
        nodesGenerated++;
        
        HashSet<State> cycleChecker = new HashSet<State>();
                        
        while (true) {
            if (openList.isEmpty()) {
                // Failure.
                return false;
            }
            
            Node node = openList.pop();
            
            if (node.isGoal()) {
                // The goal was found, so print the path and return.
                node.printPath();
                
                System.out.println(nodesGenerated + " nodes generated");
                System.out.println(nodesExpanded + " nodes expanded");
                
                return true;
            } else {
                // Expand the node.
                cycleChecker.add(node.getState());
                nodesExpanded++;
                                
                List<Node> children = expand(node);
                nodesGenerated += children.size();
                
                for (Node child : children) {                    
                    // Make sure the generated node is not a duplicate.
                    if (child.getDepth() <= depthBounds && !cycleChecker.contains(child.getState())) {
                        openList.push(child);
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
