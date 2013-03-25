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

        for (State possibleFuture : state.expand()) {
            // Prune away the parent state.
            if (parent == null || !possibleFuture.equals(parent.state)) {
                Node n = null;

                if (!Program3.isParallel) {
                    n = new Node(possibleFuture, this, getG() + 1);
                    
                    children.add(n);
                } else {
                    n = new Node(possibleFuture, this, getG());
                    
                    // check for conflicting actions
                    if (!interferes(n)) {
                        children.add(n);
                    }
                }

            }
        }

        if (Program3.isParallel) {
            // / add dummy advance time node
            State futureState = new State(state.getStepsTaken(), state.getPositive(), state.getNegative(), state.getTime() + 1);            
            children.add(new Node(futureState, this, getG() + 1));
        }

        return children;
    }
    
    public Action getLastAction() {
        return state.getLastAction();
    }

    public boolean interferes(Node other) {
        Action otherLastAction = other.getLastAction();
        Node n = this;

        while (n != null && n.state.getTime() == other.state.getTime()) {
            Action nLastAction = n.getLastAction();
                        
            if (nLastAction != null) {
                if (otherLastAction.interferes(nLastAction)
                        || nLastAction.interferes(otherLastAction)) {
                    return true;
                }
            }
            
            n = n.parent;
        }
        
        return false;
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
