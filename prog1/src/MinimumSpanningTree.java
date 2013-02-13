import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Find the minimum spanning tree that connects all remaining dirty cells (and
 * optionally a charging cell). This is for use with heuristic functions h2 and
 * h3. Since generating a minimum spanning tree is quite expensive, it is only
 * done when that tree has not been created before. All created trees are stored
 * here statically in a hash map, hashed by the dirty cells and charging cell
 * that they represent.
 * 
 * Implemented using Kruskal's algorithm.
 * 
 * @author Carmen St. Jean
 * 
 */
public class MinimumSpanningTree {
	public static HashMap<Pair, MinimumSpanningTree> msts = new HashMap<Pair, MinimumSpanningTree>();

	private double cost = 0;

	private MinimumSpanningTree(BitSet cellsLeftover, Cell chargingCell) {
		List<Cell> newList = new ArrayList<Cell>();

		// Only include the dirty cells that remain.
		for (int i = 0; i < cellsLeftover.size(); i++) {
			if (cellsLeftover.get(i)) {
				newList.add(VacuumWorld.dirtyCells.get(i));
			}
		}

		// Only include the charging cell if was actually specified.
		if (chargingCell != null) {
			newList.add(chargingCell);
		}

		buildMst(newList);
	}

	/**
	 * Gets the cost of the minimum spanning tree.
	 * 
	 * @return The cost of the tree as a double.
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Retrieves the minimum spanning tree for the specified state.
	 * 
	 * @param state
	 *            The state for which a minimum spanning tree is required.
	 * @return The minimum spanning tree connecting those dirty cells (and the
	 *         single charging cell, if specified).
	 */
	public static MinimumSpanningTree getMst(State state) {
		return getMst(state, null);
	}

	/**
	 * Retrieves the minimum spanning tree for the specified state and charging
	 * cell.
	 * 
	 * @param state
	 *            The state for which a minimum spanning tree is required.
	 * @param chargingCell
	 *            A charging station to be included in the minimum spanning tree
	 *            (null if no charging station is desired).
	 * @return The minimum spanning tree connecting those dirty cells (and the
	 *         single charging cell, if specified).
	 */
	public static MinimumSpanningTree getMst(State state, Cell chargingCell) {
		BitSet bitsToClean = state.getBitsToClean();

		MinimumSpanningTree mst = msts.get(new Pair(bitsToClean, chargingCell));

		if (mst == null) {
			mst = new MinimumSpanningTree(bitsToClean, chargingCell);
			msts.put(new Pair(bitsToClean, null), mst);
		}

		return mst;
	}

	/**
	 * Builds a minimum spanning tree to connect the specified cells.
	 * 
	 * @param cells
	 *            The cells for which a minimum spanning tree is required.
	 */
	private void buildMst(List<Cell> cells) {
		PriorityQueue<Edge> allEdges = new PriorityQueue<Edge>();
		List<Tree> forest = new ArrayList<Tree>();

		for (int i = 0; i < cells.size(); i++) {
			Cell cell = cells.get(i);
			Tree tree = new Tree();
			tree.add(cell);
			forest.add(tree);

			for (int j = i + 1; j < cells.size(); j++) {
				allEdges.add(new Edge(cell, cells.get(j)));
			}
		}

		buildMstFromForest(allEdges, forest);
	}

	/**
	 * Builds the minimum spanning tree from the forest of disjoint trees and
	 * determines the cost of that minimum spanning tree.
	 * 
	 * @param allEdges
	 *            All of the possible edges in the graph.
	 * @param forest
	 *            The forest of disjoint trees where each tree contains a single
	 *            cell.
	 * @return The list of edges that form the minimum spanning tree.
	 */
	private List<Edge> buildMstFromForest(PriorityQueue<Edge> allEdges,
			List<Tree> forest) {
		List<Edge> mstEdges = new ArrayList<Edge>();

		while (!allEdges.isEmpty() && forest.size() > 1) {
			// Find the minimum edge.
			Edge minimumEdge = allEdges.poll();

			Cell cellA = minimumEdge.getCellA();
			Cell cellB = minimumEdge.getCellB();
			int indexTreeA = -1;
			int indexTreeB = -1;

			// Look through the forest for the trees that contain the two cells.
			for (int i = 0; i < forest.size(); i++) {
				Tree tree = forest.get(i);

				if (tree.contains(cellA)) {
					indexTreeA = i;
				}

				if (tree.contains(cellB)) {
					indexTreeB = i;
				}
			}

			// If the two cells are in two different trees, those trees should
			// be joined; otherwise ignore this edge completely.
			if (indexTreeA != indexTreeB) {
				mergeTrees(forest, indexTreeA, indexTreeB);
				mstEdges.add(minimumEdge);
				cost = cost + minimumEdge.getCost();
			}
		}

		return mstEdges;
	}

	/**
	 * Combines two trees of the forest together.
	 * 
	 * @param forest
	 *            The collection of disjoint trees.
	 * @param indexTreeA
	 *            The index of the first tree to be combined with the second
	 *            tree.
	 * @param indexTreeB
	 *            The index of the second tree to be combined with the first
	 *            tree.
	 */
	private void mergeTrees(List<Tree> forest, int indexTreeA,
			int indexTreeB) {
		forest.get(indexTreeA).mergeIntoTree(forest.get(indexTreeB));
		forest.remove(indexTreeB);
	}
	
	/**
	 * Defines an edge connecting two cells.
	 * 
	 * @author Carmen St. Jean
	 * 
	 */
	private class Edge implements Comparable<Edge> {
		private final int BEFORE = -1;
		private final int EQUAL = 0;
		private final int AFTER = 1;
		
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
	}
	
	/**
	 * Defines a tree where the nodes are unique cells.
	 * 
	 * @author Carmen St. Jean
	 *
	 */
	private class Tree extends HashSet<Cell> {
		private static final long serialVersionUID = 7194448419581626719L;
		
		/**
		 * Add all cells of another tree into this tree.
		 * 
		 * @param otherTree
		 *            The other tree to be added to this tree.
		 */
		public void mergeIntoTree(Tree otherTree) {
			for (Cell cell : otherTree) {
				this.add(cell);
			}
		}
	}
}
