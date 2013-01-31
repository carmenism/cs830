
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
