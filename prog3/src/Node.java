import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node of the search tree for the STRIPS planning problem.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Node {
    private final State state;
    private final Node parent;
    private final double g;
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
     */
    public Node(State state, Node parent, double g) {
        this.state = state;
        this.parent = parent;
        this.g = g;
        this.h = Heuristic.compute(state);
    }

    /**
     * Expands this node for searching, with parent pruning.
     * 
     * @return A list of Nodes representing all possible future states.
     */
    public List<Node> expand() {
        List<Node> children = new ArrayList<Node>();

        if (!Program3.isParallel) {
            for (State possibleFuture : state.expand()) {
                // Prune away the parent state.
                if (parent == null || !possibleFuture.equals(parent.state)) {
                    Node n = new Node(possibleFuture, this, g + 1);

                    children.add(n);
                }
            }
        } else {
            for (State possibleFuture : state.expand()) {
                if (state.getTime() < possibleFuture.getTime()) {
                    // Advance time node.
                    children.add(new Node(possibleFuture, this, g + 1));
                } else {
                    Node n = new Node(possibleFuture, this, g);
                    children.add(n);
                }
            }
        }

        return children;
    }

    /**
     * Determines whether or not this Node contains the goal State.
     * 
     * @return True if this Node contains the goal.
     */
    public boolean isGoal() {
        return state.isGoal();
    }

    /**
     * Gets the f value for this node, which is calculated as g + w * h.
     * 
     * @return The f value.
     */
    public double getF() {
        if (h == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        }

        return g + Program3.w * h;
    }

    /**
     * Gets the g value for this node, which is the cost it has taken to reach
     * the corresponding state.
     * 
     * @return The g value.
     */
    public double getG() {
        return g;
    }

    /**
     * Gets the h (heuristic) value for this node, which is the lower bound on
     * the cost to go until the goal.
     * 
     * @return The h value.
     */
    public double getH() {
        return h;
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }
}
