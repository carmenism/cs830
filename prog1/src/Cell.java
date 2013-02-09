import java.util.ArrayList;
import java.util.List;

/**
 * Defines a grid cell of the vacuum world.
 * 
 * @author Carmen St. Jean
 *
 */
public class Cell {
    public static int numberDirtyCells = 0;
    
    public enum type {
        FREE,
        BLOCKED,
        CHARGE_STATION
    }
        
    private int row;
    private int col;
        
    private type cellType;
    private boolean clean = true;
    private boolean occupied = false;
    
    public Cell north;
    public Cell south;
    public Cell east;
    public Cell west;
    
    private List<Integer> distanceToDirts;
    
    public int dirtyCellIndex = -1;
    
    public Cell(int r, int c, char t) {
        row = r;
        col = c;
        
        if (t == '_') {
            cellType = type.FREE;
        } else if (t == '*') {
            cellType = type.FREE;
            clean = false;
            dirtyCellIndex = numberDirtyCells;
            numberDirtyCells++;
        } else if (t == '#') {
            cellType = type.BLOCKED;
        } else if (t == ':') {
            cellType = type.CHARGE_STATION;
        } else if (t == '@') {
            cellType = type.FREE;
            occupied = true;
        }
    }

    /**
     * Gets the Manhattan distance from this cell to another cell.
     * 
     * @param otherCell
     *            The other cell to calculate the Manhattan distance to.
     * @return The Manhattan distance between the cells.
     */
    public int getManhattanDistance(Cell otherCell) {
        int rowDiff = Math.abs(otherCell.getRow() - getRow());
        int colDiff = Math.abs(otherCell.getCol() - getCol());
        
        return rowDiff + colDiff;
    }
    
    /**
     * Calculates the Manhattan distance from this cell to all dirty cells.
     */
    public void calculateDistanceToDirts() {
        distanceToDirts = new ArrayList<Integer>();
        
        for (Cell dirtyCell : State.dirtyCells) {
            distanceToDirts.add(getManhattanDistance(dirtyCell));
        }
    }

    /**
     * Gets the (previously calculated) Manhattan distance from this cell to the
     * dirty cell of the specified index.
     * 
     * @param index
     *            The index of the dirty cell whose distance will be measured
     *            from this cell.
     * @return The Manhattan distance between this cell and the specified dirty
     *         cell.
     */
    public int getDistanceToDirt(int index) {
        return distanceToDirts.get(index);
    }

    /**
     * Specifies whether or not this cell is a blocked cell.
     * 
     * @return True if this cell is blocked.
     */
    public boolean isBlocked() {
        return cellType == type.BLOCKED;
    }

    /**
     * Specifies whether or not this cell was originally clean.
     * 
     * @return True if this cell was originally clean.
     */
    public boolean isClean() {
        return clean;
    }

    /**
     * Specifies whether or not this cell was originally occupied by the robot.
     * 
     * @return True if this cell was originally occupied by the robot.
     */
    public boolean isOccupied() {
        return occupied;
    }
    
    public type returnCellType() {
        return cellType;
    }
    
    public void setClean(boolean c) {
        clean = c;
    }
    
    public void setOccupied(boolean o) {
        occupied = o;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
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
		Cell other = (Cell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public String toString() {
        return "Cell[ r:" + row + ", c:" + col + " ]"; 
    }
    
    /**
     * Prints the neighbors of this cell.
     */
    public void printNeighbors() {
        System.out.println("Neighbors of " + this.toString() + ":");
        System.out.println("\tN " + north);
        System.out.println("\tS " + south);
        System.out.println("\tE " + east);
        System.out.println("\tW " + west);
    }
}
