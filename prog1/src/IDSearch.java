/**
 * Defines an abstract class representing iterative deepening search. Iterative
 * deepening consists of using depth-first search with a bound. To implement an
 * iterative deepening search, extend this class and define the bounds check
 * method.
 * 
 * @author Carmen St. Jean
 * 
 */
public abstract class IDSearch extends DepthFirstSearch {
    private final State initialState;

    /**
     * Creates the iterative deepening search algorithm and the initial node
     * from the specified initial state.
     * 
     * @param initialState
     *            The initial state of the world.
     */
    public IDSearch(State initialState) {
        super(initialState);

        this.initialState = initialState;
    }

    @Override
    public Solution search() {
        int depth = 1;

        while (true) {
            DepthFirstSearch dfs = new DepthFirstSearch(initialState, depth,
                    this);
            Solution solution = dfs.search();

            if (solution != null) {
                int totalNodesGenerated = solution.getNodesGenerated()
                        + this.nodesGenerated;
                int totalNodesExpanded = solution.getNodesExpanded()
                        + this.nodesExpanded;

                return new Solution(solution.getPath(), totalNodesGenerated,
                        totalNodesExpanded);
            }

            this.nodesGenerated += dfs.nodesGenerated;
            this.nodesExpanded += dfs.nodesExpanded;

            depth++;
        }
    }

    /**
     * Checks a node with the current bound.
     * 
     * @param node
     *            The node to be compared with the bound.
     * @param bound
     *            The current bound.
     * @return True if the node is to be added to the open list, false if the
     *         node exceeds the bounds and should not be included in the open
     *         list.
     */
    protected abstract boolean boundsCheck(Node node, int bound);
}
