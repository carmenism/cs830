import java.util.BitSet;


public class State {
	private int robotRow;
	private int robotCol;
	
	public BitSet bitsToClean;
	
	// actions done so far?
	
	public State(int r, int c, BitSet b) {
		robotRow = r;
		robotCol = c;
		bitsToClean = b;
	}

	public int getRobotRow() {
		return robotRow;
	}

	public int getRobotCol() {
		return robotCol;
	}

	public BitSet getBitsToClean() {
		return bitsToClean;
	}

	public void setBitsToClean(BitSet bitsToClean) {
		this.bitsToClean = bitsToClean;
	}
	
	public String toString() {
		return "r:" + robotRow + ", c:" + robotCol + ", " + bitsToClean.toString();
	}
}
