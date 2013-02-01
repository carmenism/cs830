import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


public class State {
	public static List<Cell> dirtyCells = new ArrayList<Cell>();
	
	/*public enum Action {
		NORTH,
		SOUTH,
		EAST,
		WEST,
		VACUUM
	}*/
	
	public static final char NORTH = 'N';
	public static final char SOUTH = 'S';
	public static final char EAST = 'E';
	public static final char WEST = 'W';
	public static final char VACUUM = 'V';
	
	private Cell cell;
	
	public BitSet bitsToClean;
	
	public String actionsTaken = "";
	
	public State(Cell cell, BitSet bitsToClean) {
		this.cell = cell;
		this.bitsToClean = bitsToClean;
	}
	
	public State(Cell cell, BitSet bitsToClean, String actionsTaken, char lastAction) {
		this(cell, bitsToClean);
		
		this.actionsTaken = actionsTaken + lastAction;
		System.out.println("New node generated ("+this.toString()+"), actions taken: [" + this.actionsTaken + "]");
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
		return "State[ " + cell.toString() + ", " + bitsToClean.toString() + " ]";
	}
	
	/*public char getLastAction() {
		if (actionsTaken.length() == 0) {
			return ' ';
		}
		
		return actionsTaken.charAt(actionsTaken.length() - 1);
	}*/
	
	public boolean isGoal() {
		System.out.println("Goal check (for "+this.toString()+"): " + bitsToClean.cardinality());
		
		return bitsToClean.isEmpty();
	}
	
	public void printPath() {
		System.out.println("PRINTING PATH!");
		for (int i = 0; i < actionsTaken.length(); i++) {
			System.out.println(actionsTaken.charAt(i));
		}
	}
	
	public int manhattanDistToNearestDirtyCell() {			
		if (!cell.isClean()) {
			System.out.println("Calculating manhattan dist = " + 0 + " " + cell.toString());
			return 0;
		}
		
		int minManhattan = Integer.MAX_VALUE;
		
		for (int i = 0; i < bitsToClean.size(); i++) {
			if (bitsToClean.get(i)) {
				Cell dirtyCell = dirtyCells.get(i);
				
				int rowDiff = Math.abs(cell.getRow() - dirtyCell.getRow());
				int colDiff = Math.abs(cell.getCol() - dirtyCell.getCol());
				int manhattanDist = rowDiff + colDiff;
				
				if (minManhattan > manhattanDist) {
					minManhattan = manhattanDist;
				}
			}
		}
		System.out.println("Calculating manhattan dist  = " + minManhattan + " " + cell.toString());
		return minManhattan;
	}
	
	public int remainingDirtyCells() {
		System.out.println("Calculating remaining dirty cells " + bitsToClean.cardinality() + " " + cell.toString());
		
		return bitsToClean.cardinality();
	}
	
	public List<State> expand() {
		System.out.println("Entering state expand: " + this.toString());

		List<State> possibleFutures = new ArrayList<State>(4);
				
		if (!cell.isClean() && bitsToClean.get(cell.dirtyCellIndex)) {
			// Add a state to vacuum.
			BitSet newStateBits = (BitSet) bitsToClean.clone();
			newStateBits.flip(cell.dirtyCellIndex);					
			possibleFutures.add(new State(cell, newStateBits, actionsTaken, VACUUM));
			
			System.out.println(" Found V child");
		} else {
			if (cell.east != null) { //!actionsTaken.endsWith(""+WEST) && 
				// Add a state to move east.
				possibleFutures.add(new State(cell.east, (BitSet) bitsToClean.clone(), actionsTaken, EAST));	
				
				System.out.println(" Found E child");
			}
			
			if (cell.west != null) { //!actionsTaken.endsWith(""+EAST) && 
				// Add a state to move west.
				possibleFutures.add(new State(cell.west, (BitSet) bitsToClean.clone(), actionsTaken, WEST));
				
				System.out.println(" Found W child");
			}
			
			if (cell.north != null) { //!actionsTaken.endsWith(""+SOUTH) && 
				// Add a state to move north.
				possibleFutures.add(new State(cell.north, (BitSet) bitsToClean.clone(), actionsTaken, NORTH));
				
				System.out.println(" Found N child");
			}
			
			if (cell.south != null) { //!actionsTaken.endsWith(""+NORTH) && 
				// Add a state to move south.				
				possibleFutures.add(new State(cell.south, (BitSet) bitsToClean.clone(), actionsTaken, SOUTH));
				
				System.out.println(" Found S child");
			}
		}
		

		System.out.println("Exiting state expand: " + this.toString());
		
		return possibleFutures;
	}
}
