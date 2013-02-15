import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Defines a possible state of the robot and the grid of cell of the vacuum
 * world.
 * 
 * @author Carmen St. Jean
 * 
 */
public class State {
	public static final char NORTH = 'N';
	public static final char SOUTH = 'S';
	public static final char EAST = 'E';
	public static final char WEST = 'W';
	public static final char VACUUM = 'V';
	public static final char RECHARGE = 'R';

	private final Cell cell;
	private final BitSet bitsToClean;
	private String actionsTaken = "";
	private final MinimumSpanningTree mst;
	private final int batteryLevel;

	/**
	 * Creates a state where no actions have been taken. To be used only for the
	 * creation of the initial state.
	 * 
	 * @param cell
	 *            The cell where the robot is located.
	 * @param bitsToClean
	 *            A BitSet indicating which of the dirty cells are left to be
	 *            cleaned.
	 * @param batteryLevel
	 *            The remaining charge in the robot's battery.
	 */
	public State(Cell cell, BitSet bitsToClean, int batteryLevel) {
		this.cell = cell;
		this.bitsToClean = bitsToClean;
		this.batteryLevel = batteryLevel;
		this.mst = MinimumSpanningTree.getMst(this);
	}

	/**
	 * Creates a state where actions have been taken. To be used by all states
	 * but the initial state.
	 * 
	 * @param cell
	 *            The cells where the robot is located.
	 * @param bitsToClean
	 *            A BitSet indicating which of the dirty cells are left to be
	 *            cleaned.
	 * @param actionsTaken
	 *            A string representing the actions taken previously by the
	 *            robot, NOT including the most recent action.
	 * @param lastAction
	 *            A character representing the most recent action taken by the
	 *            robot.
	 * @param batteryLevel
	 *            The remaining charge in the robot's battery.
	 */
	public State(Cell cell, BitSet bitsToClean, String actionsTaken,
			char lastAction, int batteryLevel) {
		this(cell, bitsToClean, batteryLevel);

		this.actionsTaken = actionsTaken + lastAction;
	}

	/**
	 * Determines whether or not this state is the goal state.
	 * 
	 * @return True if this state is the goal state.
	 */
	public boolean isGoal() {
		return bitsToClean.isEmpty();
	}

	/**
	 * Finds the distance to the nearest dirty cell.
	 * 
	 * @return The Manhattan distance to the closest dirty cell.
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
		List<State> possibleFutures = new ArrayList<State>(5);

		int nextBatteryLevel;

		// Determine the next battery level.
		if (batteryLevel == Integer.MAX_VALUE) {
			// The battery option of the program is not being used, so simply
			// set the battery to max level again.
			nextBatteryLevel = Integer.MAX_VALUE;
		} else {
			// Subtract one battery level.
			nextBatteryLevel = batteryLevel - 1;
		}

		// If the next battery level is zero, then we have no charge to make any
		// further moves. Return an empty list.
		if (nextBatteryLevel < 0) {
			return possibleFutures;
		}

		if (!cell.isClean() && bitsToClean.get(cell.dirtyCellIndex)) {
			// Add a state to vacuum.
			BitSet newStateBits = (BitSet) bitsToClean.clone();
			newStateBits.clear(cell.dirtyCellIndex);
			possibleFutures.add(new State(cell, newStateBits, actionsTaken,
					VACUUM, nextBatteryLevel));
		} else {
			if (cell.east != null) {
				// Add a state to move east.
				possibleFutures.add(new State(cell.east, bitsToClean,
						actionsTaken, EAST, nextBatteryLevel));
			}

			if (cell.west != null) {
				// Add a state to move west.
				possibleFutures.add(new State(cell.west, bitsToClean,
						actionsTaken, WEST, nextBatteryLevel));
			}

			if (cell.north != null) {
				// Add a state to move north.
				possibleFutures.add(new State(cell.north, bitsToClean,
						actionsTaken, NORTH, nextBatteryLevel));
			}

			if (cell.south != null) {
				// Add a state to move south.
				possibleFutures.add(new State(cell.south, bitsToClean,
						actionsTaken, SOUTH, nextBatteryLevel));
			}

			// Only add the charge action if the cell is a charging station.
			if (cell.isChargeStation()) {
				possibleFutures.add(new State(cell, bitsToClean, actionsTaken,
						RECHARGE, VacuumWorld.maxBattery));
			}
		}

		return possibleFutures;
	}

	/**
	 * Return the string that represents all actions taken for this state.
	 * 
	 * @return The actions taken by the robot so far, as a string.
	 */
	public String getActionsTaken() {
		return actionsTaken;
	}

	/**
	 * Get the length of the minimum spanning tree for the dirty cells of this
	 * state.
	 * 
	 * @return The length of the minimum spanning tree.
	 */
	public double getMinimumSpanningTreeLength() {
		return mst.getCost();
	}

	/**
	 * Get the amount of charge remaining for the battery.
	 * 
	 * @return The current battery level.
	 */
	public int getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * Get the cell where the vacuum robot is positioned during this state.
	 * 
	 * @return The cell where the robot is positioned.
	 */
	public Cell getCell() {
		return cell;
	}

	/**
	 * Gets the BitSet representing the state of the dirty cells. (0 means the
	 * cell was cleaned, 1 means the cell is still dirty in this state.)
	 * 
	 * @return The BitSet representing the state of the dirty cells.
	 */
	public BitSet getBitsToClean() {
		return bitsToClean;
	}

	@Override
	public String toString() {
		return "State[ " + cell.toString() + ", " + bitsToClean.toString()
				+ " ]";
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
}
