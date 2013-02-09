import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

/**
 * Defines a possible state of a grid of cells for planning in the vacuum world.
 * 
 * @author Carmen St. Jean
 * 
 */
public class State {
    public static List<Cell> dirtyCells = new ArrayList<Cell>();
    public static HashMap<BitSet, MinimumSpanningTree> minimumSpanningTrees = new HashMap<BitSet, MinimumSpanningTree>(); 
    
    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';
    public static final char VACUUM = 'V';
    
    private Cell cell;    
    private BitSet bitsToClean;    
    private String actionsTaken = "";
    private MinimumSpanningTree mst = null;
    
    public State(Cell cell, BitSet bitsToClean) {
        this.cell = cell;
        this.bitsToClean = bitsToClean;
        
        mst = minimumSpanningTrees.get(bitsToClean);
        
        if (mst == null) {
        	mst = new MinimumSpanningTree(dirtyCells, bitsToClean);
        	minimumSpanningTrees.put(bitsToClean, mst);
        }
    }
    
    public State(Cell cell, BitSet bitsToClean, String actionsTaken, char lastAction) {
        this(cell, bitsToClean);
        
        this.actionsTaken = actionsTaken + lastAction;
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
    /*
    public MinimumSpanningTree getMst() {
		return mst;
	}

	public void setMst(MinimumSpanningTree mst) {
		this.mst = mst;
	}*/

	public String toString() {
        return "State[ " + cell.toString() + ", " + bitsToClean.toString() + " ]";
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bitsToClean == null) ? 0 : bitsToClean.hashCode());
		result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (bitsToClean == null) {
			if (other.bitsToClean != null)
				return false;
		} else if (!bitsToClean.equals(other.bitsToClean))
			return false;
		if (cell == null) {
			if (other.cell != null)
				return false;
		} else if (!cell.equals(other.cell))
			return false;
		return true;
	}

	/**
     * Determines whether or not this state is the goal state.
     * 
     * @return True if this state is the goal state.
     */
    public boolean isGoal() {
        return bitsToClean.isEmpty();
    }
    
    public void printPath() {
        for (int i = 0; i < actionsTaken.length(); i++) {
            System.out.println(actionsTaken.charAt(i));
        }
    }
    
    public int getPathLength() {
    	return actionsTaken.length();
    }
    
    public double getMinimumSpanningTreeLength() {
    	return mst.getCost();
    }
    
    /**
     * Finds the distance to the nearest dirty cell.
     * 
     * @return
     */
    public double distanceToNearestDirtyCell() {
    	if (bitsToClean.isEmpty()) {
    		return 0;
    	}
    	
        int minManhattan = Integer.MAX_VALUE;
        
        for (int i = 0; i < bitsToClean.size(); i++) {
            if (bitsToClean.get(i)) {                
                int manhattanDist = cell.getDistanceToDirt(i);
                
                if (minManhattan > manhattanDist) {
                    minManhattan = manhattanDist;
                }
            }
        }
        
        return minManhattan;
    }

    /**
     * Finds the distance to the farthest dirty cell.
     * 
     * @return
     */
    public double distanceToFarthestDirtyCell() {
    	int maxManhattan = Integer.MIN_VALUE;
        
        for (int i = 0; i < bitsToClean.size(); i++) {
            if (bitsToClean.get(i)) {                
                int manhattanDist = cell.getDistanceToDirt(i);
                
                if (maxManhattan < manhattanDist) {
                	maxManhattan = manhattanDist;
                }
            }
        }
        
        return maxManhattan;
    }
    
    /**
     * Gets the number of remaining dirty cells left in the world.
     * 
     * @return The number of remaning dirty cells for this state.
     */
    public int numberDirtyCells() {
        return bitsToClean.cardinality();
    }

    /**
     * Expands the state into all possible future states based on possible
     * movements for the robot.
     * 
     * @return A list of possible future states.
     */
    public List<State> expand() {
        List<State> possibleFutures = new ArrayList<State>(4);
                
        if (!cell.isClean() && bitsToClean.get(cell.dirtyCellIndex)) {
            // Add a state to vacuum.
            BitSet newStateBits = (BitSet) bitsToClean.clone();
            newStateBits.flip(cell.dirtyCellIndex);                    
            possibleFutures.add(new State(cell, newStateBits, actionsTaken, VACUUM));
        } else {
            if (cell.east != null) { 
                // Add a state to move east.
                possibleFutures.add(new State(cell.east, bitsToClean, actionsTaken, EAST));    
            }
            
            if (cell.west != null) { 
                // Add a state to move west.
                possibleFutures.add(new State(cell.west, bitsToClean, actionsTaken, WEST));
            }
            
            if (cell.north != null) { 
                // Add a state to move north.
                possibleFutures.add(new State(cell.north, bitsToClean, actionsTaken, NORTH));
            }
            
            if (cell.south != null) {
                // Add a state to move south.                
                possibleFutures.add(new State(cell.south, bitsToClean, actionsTaken, SOUTH));
            }
        }
        
        return possibleFutures;
    }
    
    /*public double awesomeHeuristic(){
    	double mst = calculateMST();
    	double dirts = this.bitsToClean.cardinality();
    	double nearestDirt = 
    }*/
}
