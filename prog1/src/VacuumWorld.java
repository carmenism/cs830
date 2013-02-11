import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Defines a class to read from standard input and create a vacuum world.
 * 
 * @author Carmen St. Jean
 *
 */
public class VacuumWorld {
    public static List<Cell> dirtyCells = new ArrayList<Cell>();
    public static List<Cell> chargingCells = new ArrayList<Cell>();
	
	public static int maxBattery;
	
    public Cell [][] cells;    
    private Cell robotCell;
    private State initialState;
    
    private int numRows;
    private int numCols;
    
	/**
	 * Creates a vacuum world.
	 * 
	 * @param useBattery
	 *            True if the user specified in the arguments that a battery is
	 *            to be considered in this problem.
	 */
    public VacuumWorld(boolean useBattery) {
        buildWorldFromInput();
        determineNeighbors();

        if (useBattery) {
        	maxBattery = 2 * (numRows + numCols - 2) + 1;       	
        } else {
        	maxBattery = Integer.MAX_VALUE;
        }
        
        initialState = new State(robotCell, buildInitialBitSet(), maxBattery);
    }

	/**
	 * Gets the initial state of the vacuum world.
	 * 
	 * @return The initial state.
	 */
    public State getInitialState() {
        return initialState;
    }

	/**
	 * Builds the BitSet for the initial state of the world.
	 * 
	 * @return A BitSet with one bit for each dirty cell.
	 */
    private BitSet buildInitialBitSet() {
        // All bits of this bitset are set to false by default.
        BitSet bitsToClean = new BitSet(dirtyCells.size());
        
        // Set them all to true.
        bitsToClean.flip(0, dirtyCells.size());
        
        return bitsToClean;
    }
    
    /**
     * Builds the vacuum world from standard input.
     */
    private void buildWorldFromInput() {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // For testing purposes on my PC.
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("C:/spring2013/cs830/prog1/worlds/giant3.vw")));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.exit(1);
        }
        
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
    
	/**
	 * Taken a line from standard input and creates one cell per character in
	 * the line.
	 * 
	 * @param line
	 *            The line from standard input.
	 * @param row
	 *            The index of the row.
	 */
    private void stringToCells(String line, int row) {
        for (int col = 0; col < numCols; col++) {
            char t = line.charAt(col);
            
            cells[row][col] = new Cell(row, col, t);
            
            if (cells[row][col].isOccupied()) {
                robotCell = cells[row][col];
            }
            
            if (!cells[row][col].isClean()) {
            	VacuumWorld.dirtyCells.add(cells[row][col]);
            }
            
            if (cells[row][col].isChargeStation()) {
            	VacuumWorld.chargingCells.add(cells[row][col]);
            }
        }
    }
    
	/**
	 * Determines neighbor relationships for each cell of the vacuum world grid.
	 * The distance to each dirt is also calculated for each cell in advance.
	 */
    private void determineNeighbors() {
        for (int r = 0; r < numRows; r++) {                
            for (int c = 0; c < numCols; c++) {
                if (!cells[r][c].isBlocked()) {
                    cells[r][c].calculateDistanceToDirts();
                    
                    // Determine northern neighbor.
                    if (r != 0 && !cells[r - 1][c].isBlocked()) {
                        cells[r][c].north = cells[r - 1][c];
                    } else {
                        cells[r][c].north = null;
                    }
                    
                    // Determine southern neighbor.
                    if (r != numRows - 1 && !cells[r + 1][c].isBlocked()) {
                        cells[r][c].south = cells[r + 1][c];
                    } else {
                        cells[r][c].south = null;
                    }
                    
                    // Determine western neighbor.
                    if (c != 0 && !cells[r][c - 1].isBlocked()) {
                        cells[r][c].west = cells[r][c - 1];
                    } else {
                        cells[r][c].west = null;
                    }
                    
                    // Determine eastern neighbor.
                    if (c != numCols - 1 && !cells[r][c + 1].isBlocked()) {
                        cells[r][c].east = cells[r][c + 1];
                    } else {
                        cells[r][c].east = null;
                    }
                }
            }
        }
    }
}
