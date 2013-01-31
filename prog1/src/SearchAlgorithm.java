import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.HashMap;

public class SearchAlgorithm {
	private State initialState;
	
	//protected List<Node> openList;
	protected HashMap<String, Node> closedList;
	
	protected HashMap<String, Node> seenList;

	public SearchAlgorithm(State initialState) {
		this.initialState = initialState;
	}
	

	public List<Node> expand(Node node) {
		List<Node> children = new ArrayList<Node>();
		
		State currentState = node.getState();
		Cell currentCell = currentState.getCell();
		
		if (!currentCell.isClean()) {
			// Add a node to vacuum
			BitSet newStateBits = (BitSet) currentState.bitsToClean.clone();
			newStateBits.flip(currentCell.dirtyCellIndex);
			
			State stateV = new State(currentCell, newStateBits);
			node.v = new Node(stateV, node, node.getG());
			
			children.add(node.v);
		} else {
			if (currentState.getLastAction() != State.Action.WEST && currentCell.east != null) {
				// Add a node to move east.
				State stateE = new State(currentCell.east, currentState.bitsToClean, currentState.actionsTaken, State.Action.EAST);
				node.e = new Node(stateE, node, node.getG() + 1);
				
				children.add(node.e);
			}
			
			if (currentState.getLastAction() != State.Action.EAST && currentCell.west != null) {
				// Add a node to move west.
				State stateW = new State(currentCell.west, currentState.bitsToClean, currentState.actionsTaken, State.Action.WEST);
				node.w = new Node(stateW, node, node.getG() + 1);
				
				children.add(node.w);
			}
			
			if (currentState.getLastAction() != State.Action.NORTH && currentCell.north != null) {
				// Add a node to move north.
				State stateN = new State(currentCell.north, currentState.bitsToClean, currentState.actionsTaken, State.Action.NORTH);
				node.n = new Node(stateN, node, node.getG() + 1);
				
				children.add(node.n);
			}
			
			if (currentState.getLastAction() != State.Action.SOUTH && currentCell.south != null) {
				// Add a node to move south.
				State stateS = new State(currentCell.south, currentState.bitsToClean, currentState.actionsTaken, State.Action.SOUTH);
				node.s = new Node(stateS, node, node.getG() + 1);
				
				children.add(node.s);
			}
		}
		
		return children;
	}
}
