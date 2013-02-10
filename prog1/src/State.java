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
    public static final char RECHARGE = 'R';
    
    private final Cell cell;    
    private final BitSet bitsToClean;    
    private String actionsTaken = "";
    private MinimumSpanningTree mst = null;
    private final int batteryLevel;
    
    public State(Cell cell, BitSet bitsToClean, int batteryLevel) {
        this.cell = cell;
        this.bitsToClean = bitsToClean;
        this.batteryLevel = batteryLevel;
        
        mst = minimumSpanningTrees.get(bitsToClean);
        
        if (mst == null) {
        	mst = new MinimumSpanningTree(dirtyCells, bitsToClean);
        	minimumSpanningTrees.put(bitsToClean, mst);
        }
    }
    
    public State(Cell cell, BitSet bitsToClean, String actionsTaken, char lastAction, int batteryLevel) {
        this(cell, bitsToClean, batteryLevel);
        
        this.actionsTaken = actionsTaken + lastAction;
    }
            
    public int getBatteryLevel() {
		return batteryLevel;
	}

	public Cell getCell() {
        return cell;
    }

    public BitSet getBitsToClean() {
        return bitsToClean;
    }

	public String toString() {
        return "State[ " + cell.toString() + ", " + bitsToClean.toString() + " ]";
    }
    

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batteryLevel;
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
		if (batteryLevel != other.batteryLevel)
			return false;
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
    
    public String getActionsTaken() {
    	return actionsTaken;
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
     * @return The number of remaining dirty cells for this state.
     */
    public int getNumberDirtyCells() {
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
        
        int nextBatteryLevel;
        
        if (batteryLevel == Integer.MAX_VALUE) {
        	nextBatteryLevel = Integer.MAX_VALUE;
        } else {
        	nextBatteryLevel = batteryLevel - 1;
        }
        
        if (nextBatteryLevel < 0) {
        	return possibleFutures;
        }
        
        if (!cell.isClean() && bitsToClean.get(cell.dirtyCellIndex)) {
            // Add a state to vacuum.
            BitSet newStateBits = (BitSet) bitsToClean.clone();
            newStateBits.flip(cell.dirtyCellIndex);                    
            possibleFutures.add(new State(cell, newStateBits, actionsTaken, VACUUM, nextBatteryLevel));
        } else {
            if (cell.east != null) { 
                // Add a state to move east.
                possibleFutures.add(new State(cell.east, bitsToClean, actionsTaken, EAST, nextBatteryLevel));    
            }
            
            if (cell.west != null) { 
                // Add a state to move west.
                possibleFutures.add(new State(cell.west, bitsToClean, actionsTaken, WEST, nextBatteryLevel));
            }
            
            if (cell.north != null) { 
                // Add a state to move north.
                possibleFutures.add(new State(cell.north, bitsToClean, actionsTaken, NORTH, nextBatteryLevel));
            }
            
            if (cell.south != null) {
                // Add a state to move south.                
                possibleFutures.add(new State(cell.south, bitsToClean, actionsTaken, SOUTH, nextBatteryLevel));
            }

            if (cell.isChargeStation()) {
            	possibleFutures.add(new State(cell, bitsToClean, actionsTaken, RECHARGE, VacuumWorld.maxBattery));
            }
        }
        
        return possibleFutures;
    }
}
