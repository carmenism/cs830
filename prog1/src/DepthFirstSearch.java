import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Defines a class representing depth first search. Depth first search means the
 * open list is a stack.
 * 
 * @author Carmen St. Jean
 * 
 */
public class DepthFirstSearch extends SearchAlgorithm {
	private int bound;
	private IDSearch callingAlgorithm = null;

	/**
	 * Creates the depth-first search algorithm and the initial node from the
	 * specified initial state.
	 * 
	 * @param initialState
	 *            The initial state of the world.
	 */
	public DepthFirstSearch(State initialState) {
		super(initialState);
		this.bound = Integer.MAX_VALUE;
	}

	/**
	 * Creates a depth first search with a bound and calling algorithm. This
	 * constructor is only intended to be used by an IDSearch class.
	 * 
	 * @param initialState
	 *            The initial state of the vacuum world.
	 * @param bound
	 *            A bound used to prune the children of an expanded node.
	 * @param callingAlgorithm
	 *            An iterative deepening search class.
	 */
	protected DepthFirstSearch(State initialState, int bound,
			IDSearch callingAlgorithm) {
		super(initialState);

		this.bound = bound;
		this.callingAlgorithm = callingAlgorithm;
	}

	@Override
	public Solution search() {
		Stack<Node> openList = new Stack<Node>();

		// Place the node with the starting state on the open list.
		openList.push(initialNode);
		nodesGenerated++;

		while (true) {
			if (openList.isEmpty()) {
				// Failure.
				return null;
			}

			Node node = openList.pop();

			if (node.isGoal()) {
				// The goal was found.
				return new Solution(node.getState().getActionsTaken(),
						nodesGenerated, nodesExpanded);
			} else if (callingAlgorithm == null
					|| callingAlgorithm.boundsCheck(node, bound)) {
				// Expand the node.
				List<Node> children = expand(node);
				nodesExpanded++;
				nodesGenerated += children.size();

				for (Node child : children) {
					// Make sure the generated node is not a duplicate.
					if (!nodeMakesACycle(child)) {
						openList.push(child);
					}
				}
			}
		}
	}

	private boolean nodeMakesACycle(Node node) {
		Node parent = node.getParent();

		while (parent != null) {
			if (parent.getState().equals(node.getState())) {
				return true;
			}

			parent = parent.getParent();
		}

		return false;
	}

	@Override
	protected List<Node> expand(Node node) {
		List<Node> children = node.expand();

		Collections.sort(children, this);

		return children;
	}

	@Override
	public int compare(Node n1, Node n2) {
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
