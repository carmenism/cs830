public class DepthFirstIDSearch extends DepthFirstSearch {
    private State initialState;
	
	public DepthFirstIDSearch(State initialState) {
        super(initialState);
        
        this.initialState = initialState;
    }
    
    public boolean search() {
    	int depth = 1;
    	
    	while (true) {
    		if (new DepthFirstSearch(initialState, depth).search()) {
    			return true;
    		}
    		
    		depth++;
    	}
    }
}
