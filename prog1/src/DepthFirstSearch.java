import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;


public class DepthFirstSearch extends SearchAlgorithm {
	
	
	public DepthFirstSearch(State initialState) {
		super(initialState);
		
		Stack<Node> openList = new Stack<Node>();
		
		openList.push(initialNode);
		seenList.put(initialNode.toString(), initialNode);
		
		HashMap<String, State> cycleChecker = new HashMap<String, State>();
				
		while (true) {
			if (openList.isEmpty()) {
				// failure
				System.err.println("Failure - open list is empty.");
				System.exit(1);
			}
			
			Node node = openList.pop();
			
			if (node.isGoal()) {
				node.printPath();
				return;
			} else {
				cycleChecker.put(node.getState().toString(), node.getState());
				
				System.out.println("Expanding node: " + node.toString());
				
				for (Node child : node.expand()) {					
					if (!cycleChecker.containsKey(child.getState().toString())) {
						openList.push(child);
						System.out.println(" Adding child to open list: " + child.toString());
					} else {
						System.out.println(" Found duplicate child: " + child.toString());
					}
				}
			}
			
			System.out.println(" *** ");
		}
	}
}
