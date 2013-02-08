import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Defines a possible state of a grid of cells for planning in the vacuum world.
 * 
 * @author Carmen St. Jean
 * 
 */
public class State {
    public static List<Cell> dirtyCells = new ArrayList<Cell>();
        
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
    
    /**
     * Finds the distance to the nearest dirty cell.
     * 
     * @return
     */
    public int getManhattanDistToNearestDirtyCell() {    
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
     * Gets the number of remaining dirty cells left in the world.
     * 
     * @return The number of remaning dirty cells for this state.
     */
    public int getNumRemainingDirtyCells() {
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
}
