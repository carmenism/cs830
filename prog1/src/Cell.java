import java.util.ArrayList;
import java.util.List;


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
	
	private List<Cell> neighbors;
	
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
	
	public List<Cell> getNeighbors() {
		if (neighbors == null) {
			neighbors = new ArrayList<Cell>();
			
			if (north != null && !north.isBlocked()) {
				neighbors.add(north);
			}
			if (south != null && !south.isBlocked()) {
				neighbors.add(south);
			}
			if (east != null && !east.isBlocked()) {
				neighbors.add(east);
			}
			if (west != null && !west.isBlocked()) {
				neighbors.add(west);
			}
		}
		
		return neighbors;
	}
	
	public boolean isBlocked() {
		return cellType == type.BLOCKED;
	}
	
	public boolean isClean() {
		return clean;
	}
	
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
	
	public String toString() {
		return "Cell[ r:" + row + ", c:" + col + " ]"; 
	}
}
