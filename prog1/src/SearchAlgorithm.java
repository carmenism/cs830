import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class SearchAlgorithm {
	private State initialState;
	
	private VacuumWorld vacuumWorld;
	
	//protected List<Node> openList;
	protected HashMap<String, Node> closedList;
	
	protected HashMap<String, Node> seenList;

	public SearchAlgorithm(State initialState) {
		this.initialState = initialState;
	}
	

	public void expand(Node node) {
		List<Node> children = new ArrayList<Node>();
		
		
	}/*
	
	public boolean validCell(int r, int c) {
		return validIndices(r, c) && !vacuumWorld.cells[r][c].isBlocked();
	}
	
	public boolean validIndices(int r, int c) {
		return validRowIndex(r) && validColIndex(c);
	}
	
	public boolean validRowIndex(int r) {
		return r >= 0 && r < vacuumWorld.numRows;
	}
	
	public boolean validColIndex(int c) {
		return c >= 0 && c < vacuumWorld.numCols;
	}*/
}
