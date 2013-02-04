import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Defines a node for a search tree.
 * 
 * @author Carmen St. Jean
 *
 */
public class Node {
	private State state;
	private final int g;
	private int h;
	
	private Node parent;
	
	public Node(State state, Node parent, int g) {
		this.state = state;
		this.parent = parent;
		this.g = g;
		this.h = state.getManhattanDistToNearestDirtyCell() + state.getNumRemainingDirtyCells();
	}

	/**
	 * Expands this node for searching, with parent pruning.
	 * 
	 * @return A list of Nodes representing all possible future states.
	 */
	public Collection<Node> expand() {
		List<Node> children = new ArrayList<Node>();

		for (State possibleFuture : state.expand()) {
			// Prune away the parent state.
			if (parent == null || !possibleFuture.equals(parent.state)) {
				Node n = new Node(possibleFuture, this, getG() + 1);
				children.add(n);
			}
		}
		
		// Sort by largest to smallest heuristic.
		Collections.sort(children, new Comparator<Node>() {
	        @Override public int compare(Node n1, Node n2) {
	            return - n1.h + n2.h;
	        }
	    });
		
		return children;
	}

	/**
	 * Determines whether or not this state is the goal state.
	 * 
	 * @return True if this state is a goal state.
	 */
	public boolean isGoal() {
		return state.isGoal();
	}
	
	/**
	 * Prints the path.
	 */
	public void printPath() {
		state.printPath();
	}
	
	public int getG() {
		return g;
	}

	public int getH() {
		return h;
	}

	public State getState() {
		return state;
	}

	public int getF() {
		return getG() + getH();
	}

	public Node getParent() {
		return parent;
	}
	
	public String toString() {
		return "Node[ " + state.toString() + ", g:" + g + ", h:" + h + " ]";
	}
}