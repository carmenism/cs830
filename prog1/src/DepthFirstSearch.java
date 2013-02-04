import java.util.HashMap;
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
	}
	
	public void search() {
		Stack<Node> openList = new Stack<Node>();
		
		// Place the node with the starting state on the open list.
		openList.push(initialNode);
		seenList.put(initialNode.toString(), initialNode);
		
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
				
				for (Node child : node.expand()) {					
					// Make sure the generated node is not a duplicate.
					if (!cycleChecker.containsKey(child.getState().toString())) {
						openList.push(child);
						nodesGenerated++;
					}
				}
			}
		}
	}
}
