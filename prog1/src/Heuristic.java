/**
 * Represents all possible heuristic functions for the vacuum world planning
 * problem.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Heuristic {
	public enum Type {
		H0, H1, H2, H3
	}

	public static final String CMD_H0 = "h0";
	public static final String CMD_H1 = "h1";
	public static final String CMD_H2 = "h2";
	public static final String CMD_H3 = "h3";

	public static Type type = Type.H1;

	/**
	 * Sets the heuristic to be used according the command specified by the
	 * user.
	 * 
	 * @param cmdHeuristic
	 *            The heuristic command.
	 */
	public static void setHeuristic(String cmdHeuristic) {
		if (cmdHeuristic.equals(CMD_H0)) {
			type = Type.H0;
		} else if (cmdHeuristic.equals(CMD_H1)) {
			type = Type.H1;
		} else if (cmdHeuristic.equals(CMD_H2)) {
			type = Type.H2;
		} else if (cmdHeuristic.equals(CMD_H3)) {
			type = Type.H3;
		} else {
			type = Type.H1;
		}
	}

	/**
	 * Calculates the heuristic value for the specified state according to the
	 * heuristic chosen by the user.
	 * 
	 * @param state
	 *            The state whose heuristic value will be calculated.
	 * @return The heuristic value of the state.
	 */
	public static double calculate(State state) {
		switch (type) {
		case H0:
			return calculateH0(state);
		case H1:
			return calculateH1(state);
		case H2:
			return calculateH2(state);
		case H3:
			return calculateH3(state);
		}

		throw new RuntimeException(
				"Unexpectedly reached end of heuristic function calculation.");
	}

	/**
	 * Calculates the heuristic according to h0 (h(n) = 0).
	 * 
	 * @param state
	 *            The state whose heuristic value will be calculated.
	 * @return The heuristic value of the state.
	 */
	private static double calculateH0(State state) {
		return 0;
	}

	/**
	 * Calculates the heuristic according to h1, a simple admissible heuristic
	 * that sums the distance to the nearest dirty cell with the number of
	 * remaining dirty cells.
	 * 
	 * @param state
	 *            The state whose heuristic value will be calculated.
	 * @return The heuristic value of the state.
	 */
	private static double calculateH1(State state) {
		return state.distanceToNearestDirtyCell() + state.getNumberDirtyCells();
	}

	/**
	 * Calculates the heuristic according to h2, a smarter admissible heuristic
	 * that sums the distance to the nearest dirty cell, the number of remaining
	 * dirty cells, and the length of the minimum spanning tree between all of
	 * the remaining dirty cells.
	 * 
	 * @param state
	 *            The state whose heuristic value will be calculated.
	 * @return The heuristic value of the state.
	 */
	private static double calculateH2(State state) {
		return calculateH1(state) + state.getMinimumSpanningTreeLength();
	}

	/**
	 * Calculates the heuristic according to h3, a smarter admissible heuristic
	 * that also takes batteries and charging cells into account. If the charge
	 * remaining on the vacuum robot is sufficient, then h3 is exactly the same
	 * as h2. Otherwise, the heuristic determines which charger makes for the
	 * best addition to the minimum spanning tree and recalculates h according
	 * to that cell.
	 * 
	 * @param state
	 *            The state whose heuristic value will be calculated.
	 * @return The heuristic value of the state.
	 */
	private static double calculateH3(State state) {
		double h = calculateH2(state);

		// Find out how many charges are needed by the robot.
		int numberChargesNeeded = (int) (h / VacuumWorld.maxBattery);

		// If it's more than one charge needed, we need to recalculate the
		// minimum spanning tree.
		if (numberChargesNeeded >= 1) {
			MinimumSpanningTree minMst = null;
			Cell minChargingCell = null;

			if (VacuumWorld.chargingCells.size() == 1) {
				// Since there is only one charger, recalculate the minimum
				// spanning tree with this one charging cell.
				minChargingCell = VacuumWorld.chargingCells.get(0);
				minMst = MinimumSpanningTree.getMst(state, minChargingCell);
			} else {
				// Figure out which charging cell added to the minimum spanning
				// tree of dirty cells makes for the smallest minimum spanning
				// tree possible.
				for (Cell chargingCell : VacuumWorld.chargingCells) {
					MinimumSpanningTree mst = MinimumSpanningTree.getMst(state,
							chargingCell);

					if (minMst == null || mst.getCost() < minMst.getCost()) {
						minMst = mst;
						minChargingCell = chargingCell;
					}
				}
			}

			// Figure out which is closer to the robot - the charging station or
			// the nearest dirty cell.
			double distNearestDirt = state.distanceToNearestDirtyCell();
			double distChargingCell = minChargingCell
					.getManhattanDistance(state.getCell());
			double distToMst;

			if (distChargingCell < distNearestDirt) {
				distToMst = distChargingCell;
			} else {
				distToMst = distNearestDirt;
			}

			return state.getNumberDirtyCells() + minMst.getCost() + distToMst
					+ numberChargesNeeded;
		}

		return h;
	}
}
