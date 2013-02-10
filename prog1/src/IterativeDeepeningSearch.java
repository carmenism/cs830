public abstract class IterativeDeepeningSearch extends DepthFirstSearch {
    private State initialState;
	
	public IterativeDeepeningSearch(State initialState) {
        super(initialState);
        
        this.initialState = initialState;
    }

    public Solution search() {
    	int depth = 1;
    	
    	while (true) {
    		DepthFirstSearch dfs = new DepthFirstSearch(initialState, depth, this);
    		Solution solution = dfs.search();
    		
    		if (solution != null) {
    			int totalNodesGenerated = solution.getNodesGenerated() + this.nodesGenerated;
    			int totalNodesExpanded = solution.getNodesExpanded() + this.nodesExpanded;
    			
    			return new Solution(solution.getPath(), totalNodesGenerated, totalNodesExpanded);
    		}
    		
    		this.nodesGenerated += dfs.nodesGenerated;
    		this.nodesExpanded += dfs.nodesExpanded;
    		
    		depth++;
    	}
    }
    
	protected abstract boolean boundsCheck(Node node, int bound);
}
