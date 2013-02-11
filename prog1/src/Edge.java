/**
 * Defines an edge connecting two cells. This class is designed for use with the
 * MinimumSpanningTree class.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Edge implements Comparable<Edge> {
    protected final int BEFORE = -1;
    protected final int EQUAL = 0;
    protected final int AFTER = 1;
	
	private final Cell cellA;
	private final Cell cellB;
	private final double cost;

	/**
	 * Creates an edge from two cells and calculates the cost of the edge.
	 * 
	 * @param cellA
	 *            The first cell.
	 * @param cellB
	 *            The second cell.
	 */
	public Edge(Cell cellA, Cell cellB) {
		this.cellA = cellA;
		this.cellB = cellB;
		this.cost = cellA.getManhattanDistance(cellB);
	}

	/**
	 * Determines whether or not the specified cell is included in this edge.
	 * 
	 * @param cell
	 *            A cell to check for in this edge.
	 * @return True if this cell is one of the two cells of this edge.
	 */
	public boolean containsCell(Cell cell) {
		return cellA.equals(cell) || cellB.equals(cell);
	}
	
	/**
	 * Determines if this edge is equivalent to another cell edge.
	 * 
	 * @param edge
	 *            The other edge for the comparison.
	 * @return True if these edges connect the same two cells.
	 */
	public boolean equivalentCells(Edge edge) {
		if (cellA.equals(edge.getCellA()) && cellB.equals(edge.getCellB())) {
			return true;
		}
		
		if (cellA.equals(edge.getCellB()) && cellB.equals(edge.getCellA())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the primary cell stored for this edge.
	 * 
	 * @return The A cell.
	 */
	public Cell getCellA() {
		return cellA;
	}

	/**
	 * Gets the secondary cell stored for this edge.
	 * 
	 * @return The B cell.
	 */
	public Cell getCellB() {
		return cellB;
	}

	/**
	 * Gets the cost for this edge.
	 * 
	 * @return The edge's cost.
	 */
	public double getCost() {
		return cost;
	}
	
	@Override
	public int compareTo(Edge cellPair) {
        // Sort lowest to highest distance.
        if (this.getCost() < cellPair.getCost()) {
            return BEFORE;
        }

        if (this.getCost() > cellPair.getCost()) {
            return AFTER;
        }
        
        return EQUAL;
	}

	@Override
	public String toString() {
		return "Edge [cellA=" + cellA + ", cellB=" + cellB + ", cost=" + cost
				+ "]\n";
	}
}
