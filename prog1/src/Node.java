import java.util.ArrayList;
import java.util.List;

/**
 * Defines a node for a search tree.
 * 
 * @author Carmen St. Jean
 *
 */
public class Node implements Comparable<Node> {	
    private final State state;
    private final double g;
    private final int depth;
    private double h;
    
    private Node parent;
    
    public Node(State state, Node parent, double g, int depth) {
        this.state = state;
        this.parent = parent;
        this.g = g;
        this.depth = depth;
        
        switch (SearchAlgorithm.heuristic) {
		case H0:
			this.h = 0;
			break;
		case H1:
			this.h = state.distanceToNearestDirtyCell() + state.getNumberDirtyCells();
			break;
		case H2:
			this.h = state.distanceToNearestDirtyCell() + state.getNumberDirtyCells() + state.getMinimumSpanningTreeLength();
			break;
		case H3:
			this.h = state.distanceToNearestDirtyCell() + state.getNumberDirtyCells() + state.getMinimumSpanningTreeLength();
			this.h = this.h + (int) (this.h / VacuumWorld.maxBattery);			
			break;        
        }
        	
        
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
           
    public int getDepth() {
        return depth;
    }
    
    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public State getState() {
        return state;
    }

    public double getF() {
        return getG() + getH();
    }

    public Node getParent() {
        return parent;
    }
    
    public String toString() {
        return "Node[ " + state.toString() + ", g:" + g + ", h:" + h + " ]";
    }

    @Override
    public int compareTo(Node node) {
        return SearchAlgorithm.algorithm.compareTo(this, node);
    }
}