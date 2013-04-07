import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Defines an abstract class representing best first search. Best first search
 * means the open list is a priority queue. To implement a best first search,
 * extend this class and define the compare method that is used to sort the
 * priority queue.
 * 
 * @author Carmen St. Jean
 * 
 */
public abstract class BestFirstSearch extends SearchAlgorithm {
    private static final int INITITAL_SIZE = 100;

    /**
     * Creates the best-first search algorithm and the initial node from the
     * specified initial state.
     * 
     * @param initialState
     *            The initial state of the world.
     */
    public BestFirstSearch(State initialState) {
        super(initialState);
    }

    @Override
    public Solution search() {
        PriorityQueue<Node> openList = new PriorityQueue<Node>(INITITAL_SIZE,
                this);
        HashMap<State, Node> closedList = new HashMap<State, Node>();

        // Place the node with the starting state on the open list.
        openList.add(initialNode);
        nodesGenerated++;

        while (true) {
            if (openList.isEmpty()) {
                // Failure.
                return new Solution(null, nodesGenerated, nodesExpanded);
            }

            Node node = openList.poll();

            if (node.isGoal()) {
                // The goal was found.
                return new Solution(node.getState().getStepsTaken(),
                        nodesGenerated, nodesExpanded);
            } else {
                // Expand the node.
                closedList.put(node.getState(), node);
                nodesExpanded++;

                System.out.println(node.getState().getStepsTaken());
                
                List<Node> children = expand(node);
                nodesGenerated += children.size();

                for (Node child : children) {
                    System.out.println("checking child");
                    // Make sure the generated node is not a duplicate.
                    Node incumbent = closedList.get(child.getState());

                    if (incumbent == null) {
                        // There is no state like this already.
                        System.out.println("adding node");
                        openList.add(child);
                        closedList.put(child.getState(), child);
                    } else if (child.getF() < incumbent.getF()
                            || (child.getF() == incumbent.getF() && child.getG() > incumbent.getG())
                            || Program3.isParallel) {
                        // This state is a duplicate, but the child is
                        // preferrable, so add it.
                        System.out.println("replacing child");
                        closedList.remove(incumbent.getState());
                        openList.add(child);
                        closedList.put(child.getState(), child);
                    } else {

                        System.out.println("not adding child");
                    }
                }
            }
        }
    }

    @Override
    protected List<Node> expand(Node node) {
        return node.expand();
    }
}
