import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Node {
	private State state;
	private int g;
	private int h;
	
	private Node parent;
	
	public Node n = null;
	public Node s = null;
	public Node e = null;
	public Node w = null;
	public Node v = null;
	
	public Node(State state, Node parent) {
		this.state = state;
		this.parent = parent;
	}

	public Node(State state, Node parent, int g) {
		this(state, parent);
		this.setG(g);
	}
	
	public Node(State state, Node parent, int g, int h) {
		this(state, parent);
		this.setH(h);
	}
	
	public List<Node> expand() {
		List<Node> children = new ArrayList<Node>();
		
		Cell cell = state.getCell();
		
		if (!cell.isClean()) {
			// Add a node to vacuum.
			BitSet newStateBits = (BitSet) state.bitsToClean.clone();
			newStateBits.flip(cell.dirtyCellIndex);
			
			State stateV = new State(cell, newStateBits);
			v = new Node(stateV, this, getG() + 1);
			
			children.add(v);
		} else {
			if (state.getLastAction() != State.Action.WEST && cell.east != null) {
				// Add a node to move east.
				State stateE = new State(cell.east, state.bitsToClean, state.actionsTaken, State.Action.EAST);
				e = new Node(stateE, this, getG() + 1);
				
				children.add(e);
			}
			
			if (state.getLastAction() != State.Action.EAST && cell.west != null) {
				// Add a node to move west.
				State stateW = new State(cell.west, state.bitsToClean, state.actionsTaken, State.Action.WEST);
				w = new Node(stateW, this, getG() + 1);
				
				children.add(w);
			}
			
			if (state.getLastAction() != State.Action.NORTH && cell.north != null) {
				// Add a node to move north.
				State stateN = new State(cell.north, state.bitsToClean, state.actionsTaken, State.Action.NORTH);
				n = new Node(stateN, this, getG() + 1);
				
				children.add(n);
			}
			
			if (state.getLastAction() != State.Action.SOUTH && cell.south != null) {
				// Add a node to move south.
				State stateS = new State(cell.south, state.bitsToClean, state.actionsTaken, State.Action.SOUTH);
				s = new Node(stateS, this, getG() + 1);
				
				children.add(s);
			}
		}
		
		return children;
	}	
		
	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public State getState() {
		return state;
	}

	public int getF() {
		return getG() + getH();
	}

	public Node getParent() {
		return parent;
	}
	
	public String toString() {
		return state.toString();// + ", g:" + g + ", h:" + h;
	}
}
