import java.util.Stack;


public class DepthFirstSearch extends SearchAlgorithm {
	Stack<Node> openList = new Stack<Node>();
	
	public DepthFirstSearch(State initialState) {
		super(initialState);
	}
}
