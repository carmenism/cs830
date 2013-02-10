public class IDDepthFirstSearch extends IterativeDeepeningSearch {
	public IDDepthFirstSearch(State initialState) {
        super(initialState);
    }
        
	protected boolean boundsCheck(Node node, int bound) {
		System.out.println("depth-first-id");
    	return node.getDepth() < bound;
    }
}
