
public class IDAStar extends IterativeDeepeningSearch {
	public IDAStar(State initialState) {
		super(initialState);
	}

	@Override
    protected boolean boundsCheck(Node node, int bound) {	
		System.out.println("ida-star");	
		return node.getF() < bound;
    }
}
