import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;


public class VacuumWorld {
	public Cell [][] cells;
	public ArrayList<Cell> dirtyCells = new ArrayList<Cell>();
	
	private int robotInitialRow;
	private int robotInitialCol;
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
				String line = br.readLine().trim();
				
				for (int c = 0; c < numCols; c++) {
					char t = line.charAt(c);
					
					cells[r][c] = new Cell(r, c, t);
					
					if (!cells[r][c].isClean()) {
						dirtyCells.add(cells[r][c]);
					}
					
					if (cells[r][c].isOccupied()) {
						robotInitialRow = r;
						robotInitialCol = c;
					}
				}
			}
			
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
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// all bits of this bitset are set to false by default
		bitsToClean = new BitSet(dirtyCells.size());
		bitsToClean.flip(0, dirtyCells.size());
		
		initialState = new State(robotInitialRow, robotInitialCol, bitsToClean);

		System.out.println(dirtyCells.size());
		System.out.println(initialState);
	}
	
	
}
