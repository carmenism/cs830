import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.BitSet;


public class VacuumWorld {
	public Cell [][] cells;	
	private Cell robotCell;
	private State initialState;
	
	private int numRows;
	private int numCols;
	
	public VacuumWorld() throws FileNotFoundException {
		buildWorldFromInput();
		determineNeighbors();
				
		initialState = new State(robotCell, buildInitialBitSet());
	}

	public State getInitialState() {
		return initialState;
	}
	
	private BitSet buildInitialBitSet() {
		// All bits of this bitset are set to false by default.
		BitSet bitsToClean = new BitSet(Cell.numberDirtyCells);
		
		// Set them all to true.
		bitsToClean.flip(0, Cell.numberDirtyCells);
		
		return bitsToClean;
	}
	
	private void buildWorldFromInput() throws FileNotFoundException {
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/csg/crr8/spring2013/cs830/prog1/worlds/hard-1.vw")));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:/spring2013/cs830/prog1/worlds/hard-1.vw")));
		
		try {
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			
			cells = new Cell[numRows][numCols];
			
			for (int r = 0; r < numRows; r++) {
				stringToCells(br.readLine().trim(), r);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void stringToCells(String line, int row) {
		for (int col = 0; col < numCols; col++) {
			char t = line.charAt(col);
			
			cells[row][col] = new Cell(row, col, t);
			
			if (cells[row][col].isOccupied()) {
				robotCell = cells[row][col];
			}
			
			if (!cells[row][col].isClean()) {
				State.dirtyCells.add(cells[row][col]);
			}
		}
	}
	
	private void determineNeighbors() {
		for (int r = 0; r < numRows; r++) {				
			for (int c = 0; c < numCols; c++) {
				if (r != 0 && !cells[r - 1][c].isBlocked()) {
					cells[r][c].north = cells[r - 1][c];
				} else {
					cells[r][c].north = null;
				}
				
				if (r != numRows - 1 && !cells[r + 1][c].isBlocked()) {
					cells[r][c].south = cells[r + 1][c];
				} else {
					cells[r][c].south = null;
				}
				
				if (c != 0 && !cells[r][c - 1].isBlocked()) {
					cells[r][c].west = cells[r][c - 1];
				} else {
					cells[r][c].west = null;
				}
				
				if (c != numCols - 1 && !cells[r][c + 1].isBlocked()) {
					cells[r][c].east = cells[r][c + 1];
				} else {
					cells[r][c].east = null;
				}
			}
		}
	}
}
