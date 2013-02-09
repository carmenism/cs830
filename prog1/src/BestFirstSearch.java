import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


public abstract class BestFirstSearch extends SearchAlgorithm {
	public BestFirstSearch(State initialState) {
		super(initialState);
	}

	@Override
	public void search() {
    	PriorityQueue<Node> openList = new PriorityQueue<Node>();
        HashMap<State, Node> closedList = new HashMap<State, Node>();
        
    	// Place the node with the starting state on the open list.
        openList.add(initialNode);
        nodesGenerated++;
    	
        while (true) {
            if (openList.isEmpty()) {
                // Failure - abort.
                System.err.println("Failure - open list is empty.");
                System.exit(1);
            }
            
            Node node = openList.poll();
            
            if (node.isGoal()) {
                // The goal was found, so print the path and return.
                node.printPath();
                
                System.out.println(nodesGenerated + " nodes generated");
                System.out.println(nodesExpanded + " nodes expanded");
                                
                return;
            } else {
                // Expand the node.
                closedList.put(node.getState(), node);
                nodesExpanded++;
                
                List<Node> children = expand(node);
                nodesGenerated += children.size();
                
                for (Node child : children) {                    
                    // Make sure the generated node is not a duplicate.
                    if (!closedList.containsKey(child.getState())) {
                        openList.add(child);
                        closedList.put(child.getState(), child);        
                    }
                }
            }
        }
	}

	@Override
	protected List<Node> expand(Node node) {
		return node.expand();
	}
}
