
public class Cell {
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
	
	public Cell(int r, int c, char t) {
		row = r;
		col = c;
		
		if (t == '_') {
			cellType = type.FREE;
		} else if (t == '*') {
			cellType = type.FREE;
			clean = false;
		} else if (t == '#') {
			cellType = type.BLOCKED;
		} else if (t == ':') {
			cellType = type.CHARGE_STATION;
		} else if (t == '@') {
			cellType = type.FREE;
			occupied = true;
		}
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
}
