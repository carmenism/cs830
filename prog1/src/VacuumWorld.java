import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.BitSet;


public class VacuumWorld {
	public Cell [][] cells;
	
	private Cell robotCell;
	private BitSet bitsToClean;
	
	private State initialState;
	
	public int numRows;
	public int numCols;
	
	public VacuumWorld() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				
		try {
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			
			cells = new Cell[numRows][numCols];
			
			for (int r = 0; r < numRows; r++) {
				stringToCells(br.readLine().trim(), r);
			}
			
			determineNeighbors();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// all bits of this bitset are set to false by default
		bitsToClean = new BitSet(Cell.numberDirtyCells);
		bitsToClean.flip(0, Cell.numberDirtyCells);
		
		initialState = new State(robotCell, bitsToClean);

		System.out.println(Cell.numberDirtyCells);
		System.out.println(initialState);
		System.out.println(cells[0][0].getNeighbors().size());
	}
	
	private void stringToCells(String line, int row) {
		for (int col = 0; col < numCols; col++) {
			char t = line.charAt(col);
			
			cells[row][col] = new Cell(row, col, t);
			
			if (cells[row][col].isOccupied()) {
				robotCell = cells[row][col];
			}					
		}
	}
	
	private void determineNeighbors() {
		for (int r = 0; r < numRows; r++) {				
			for (int c = 0; c < numCols; c++) {
				if (r == 0) {
					cells[r][c].north = null;
				} else {
					cells[r][c].north = cells[r - 1][c];
				}
				
				if (r == numRows - 1) {
					cells[r][c].south = null;
				} else {
					cells[r][c].south = cells[r + 1][c];
				}
				
				if (c == 0) {
					cells[r][c].west = null;
				} else {
					cells[r][c].west = cells[r][c - 1];
				}
				
				if (c == numCols - 1) {
					cells[r][c].east = null;
				} else {
					cells[r][c].east = cells[r][c + 1];
				}
			}
		}
	}
}
