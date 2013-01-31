import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


public class State {
	public enum Action {
		NORTH,
		SOUTH,
		EAST,
		WEST,
		VACUUM
	}
	
	private Cell cell;
	
	public BitSet bitsToClean;
	
	public List<Action> actionsTaken = null;
	
	public State(Cell cell, BitSet bitsToClean) {
		this.cell = cell;
		this.bitsToClean = bitsToClean;
		
		actionsTaken = new ArrayList<Action>();
	}
	
	public State(Cell cell, BitSet bitsToClean, List<Action> actionsTaken, Action lastAction) {
		this(cell, bitsToClean);
		
		for (Action action : actionsTaken) {
			this.actionsTaken.add(action);
		}
		
		this.actionsTaken.add(lastAction);
	}

	public Cell getCell() {
		return cell;
	}

	public BitSet getBitsToClean() {
		return bitsToClean;
	}

	public void setBitsToClean(BitSet bitsToClean) {
		this.bitsToClean = bitsToClean;
	}
	
	public String toString() {
		return cell.toString() + ", " + bitsToClean.toString();
	}
	
	public Action getLastAction() {
		if (actionsTaken.size() == 0) {
			return null;
		}
		
		return actionsTaken.get(actionsTaken.size() - 1);
	}
}
