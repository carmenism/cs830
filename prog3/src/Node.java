import java.util.ArrayList;
import java.util.List;

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

    public Action getLastAction() {
        return state.getLastAction();
    }
    
    public boolean isGoal() {
        return state.isGoal();
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public double getF() {
        if (h == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        }

        return g + Program3.w * h;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }
}
