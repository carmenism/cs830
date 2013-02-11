import java.util.BitSet;

/**
 * Defines a pairing of BitSet and Cell for use with the MinimumSpanningTree
 * hashing. Minimum spanning trees take a lot of time to generate, so they are
 * created only once and stored based on the dirty cells and charging cell (if
 * applicable) that they connect.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Pair {
	private final BitSet bitsToClean;
	private final Cell chargingStation;

	/**
	 * Creates a pair for hashing a minimum spanning tree. Charging station is
	 * optional; specify null if no charging station is to be associated with
	 * this pair.
	 * 
	 * @param bitsToClean
	 *            A BitSet indicating which of the dirty cells are left to be
	 *            cleaned.
	 * @param chargingStation
	 *            A charging station cell to be included in the minimum spanning
	 *            tree or null.
	 */
	public Pair(BitSet bitsToClean, Cell chargingStation) {
		this.bitsToClean = bitsToClean;
		this.chargingStation = chargingStation;
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

	/**
	 * Gets the charging station if one was included in the minimum spanning
	 * tree that this pair corresponds to. Null if no charging station is
	 * associated.
	 * 
	 * @return The associated charging station.
	 */
	public Cell getChargingStation() {
		return chargingStation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bitsToClean == null) ? 0 : bitsToClean.hashCode());
		result = prime * result
				+ ((chargingStation == null) ? 0 : chargingStation.hashCode());
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
		Pair other = (Pair) obj;
		if (bitsToClean == null) {
			if (other.bitsToClean != null)
				return false;
		} else if (!bitsToClean.equals(other.bitsToClean))
			return false;
		if (chargingStation == null) {
			if (other.chargingStation != null)
				return false;
		} else if (!chargingStation.equals(other.chargingStation))
			return false;
		return true;
	}
}
