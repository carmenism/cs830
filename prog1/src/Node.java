import java.util.ArrayList;
import java.util.List;

/**
 * Defines a node for a search tree.
 * 
 * @author Carmen St. Jean
 *
 */
public class Node {	
    private final State state;
    private final Node parent;
    private final double g;
    private final int depth;
    private final double h;

	/**
	 * Creates a node from a state, a parent node, a cost, and a depth.
	 * 
	 * @param state
	 *            The state corresponding to this node.
	 * @param parent
	 *            The parent for this node in the search tree.
	 * @param g
	 *            The cost of this node.
	 * @param depth
	 *            The depth of this node.
	 */
    public Node(State state, Node parent, double g, int depth) {
        this.state = state;
        this.parent = parent;
        this.g = g;
        this.depth = depth;
        this.h = Heuristic.calculate(state);        
    }

    /**
     * Expands this node for searching, with parent pruning.
     * 
     * @return A list of Nodes representing all possible future states.
     */
    public List<Node> expand() {
        List<Node> children = new ArrayList<Node>();

        for (State possibleFuture : state.expand()) {
            // Prune away the parent state.
            if (parent == null || !possibleFuture.equals(parent.state)) {
                Node n = new Node(possibleFuture, this, getG() + 1, getDepth() + 1);
                assert(this.getF() <= n.getF());
                children.add(n);
            }
        }
        
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
	 * The depth of this node in the search tree.
	 * 
	 * @return The node's depth.
	 */
    public int getDepth() {
        return depth;
    }

	/**
	 * The cost for this node.
	 * 
	 * @return The node's cost.
	 */
    public double getG() {
        return g;
    }

	/**
	 * The heuristic function value for this node.
	 * 
	 * @return The node's heuristic value.
	 */
    public double getH() {
        return h;
    }

	/**
	 * The state of the vacuum world for this node.
	 * 
	 * @return The state corresponding to this node.
	 */
    public State getState() {
        return state;
    }

	/**
	 * The f value (where f = g + h) for this node.
	 * 
	 * @return The f value.
	 */
    public double getF() {
        return getG() + getH();
    }

	/**
	 * The parent node for this node.
	 * 
	 * @return The parent.
	 */
    public Node getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        return "Node[ " + state.toString() + ", g:" + g + ", h:" + h + " ]";
    }
}