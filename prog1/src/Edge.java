
public class Edge implements Comparable<Edge> {
    protected final int BEFORE = -1;
    protected final int EQUAL = 0;
    protected final int AFTER = 1;
	
	private Cell cellA;
	private Cell cellB;
	private double cost;
	
	public Edge(Cell cellA, Cell cellB) {
		this.cellA = cellA;
		this.cellB = cellB;
		this.cost = cellA.getManhattanDistance(cellB);
	}

	public Cell getCellA() {
		return cellA;
	}

	public Cell getCellB() {
		return cellB;
	}

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
	
	public boolean containsCell(Cell cell) {
		return cellA.equals(cell) || cellB.equals(cell);
	}
	
	public boolean equivalentCells(Edge cellPair) {
		if (this.getCellA().equals(cellPair.getCellA()) && this.getCellB().equals(cellPair.getCellB())) {
			return true;
		}
		
		if (this.getCellA().equals(cellPair.getCellB()) && this.getCellB().equals(cellPair.getCellA())) {
			return true;
		}
		
		return false;
	}
}
