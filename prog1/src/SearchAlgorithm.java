import java.util.HashMap;

public class SearchAlgorithm {
	private State initialState;
	protected Node initialNode;
	
	//protected List<Node> openList;
	protected HashMap<String, Node> closedList;
	
	protected HashMap<String, Node> seenList = new HashMap<String, Node>();

	public SearchAlgorithm(State initialState) {
		this.initialState = initialState;
		this.initialNode = new Node(initialState, null, 0);
	}
}
