import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	public VacuumWorld() throws FileNotFoundException {

		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/csg/crr8/spring2013/cs830/prog1/worlds/hard-1.vw")));
				
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
		bitsToClean.flip(0, Cell.numberDirtyCells); // set them all to true
		
		initialState = new State(robotCell, bitsToClean);

		System.out.println(Cell.numberDirtyCells);
		System.out.println(initialState);
		System.out.println(cells[0][0].getNeighbors().size());
		
		new DepthFirstSearch(initialState);
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
		
		
		/*for (int r = 0; r < numRows; r++) {				
			for (int c = 0; c < numCols; c++) {
				System.err.println(cells[r][c]);

				System.err.println("\tN "+cells[r][c].north);
				System.err.println("\tS "+cells[r][c].south);
				System.err.println("\tE "+cells[r][c].east);
				System.err.println("\tW "+cells[r][c].west);
			}

			System.err.println();
		}*/
	}
}
