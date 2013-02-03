import java.util.HashMap;

public abstract class SearchAlgorithm {
	protected Node initialNode;
	
	//protected List<Node> openList;
	protected HashMap<String, Node> closedList;
	
	protected HashMap<String, Node> seenList = new HashMap<String, Node>();

	protected int nodesGenerated = 0;
	protected int nodesExpanded = 0;
	
	public SearchAlgorithm(State initialState) {
		this.initialNode = new Node(initialState, null, 0);
	}
	
	public abstract void search();
}
