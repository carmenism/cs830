import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node {
	private State state;
	private final int g;
	private int h;
	
	private Node parent;
	
	public Node(State state, Node parent, int g) {
		this.state = state;
		this.parent = parent;
		this.g = g;
		this.h = state.manhattanDistToNearestDirtyCell() + state.remainingDirtyCells();
		
		//System.out.println("New state generated ("+this.toString()+")");
	}
		
	public Collection<Node> expand() {
		//System.out.println("Entering node expand: " + this.toString());
		
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
		

		//System.out.println("Exiting node expand: " + this.toString());
		
		return children;
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
	
	public boolean isGoal() {
		return state.isGoal();
	}
	
	public void printPath() {
		state.printPath();
	}
}
