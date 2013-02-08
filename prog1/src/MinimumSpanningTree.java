import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


public class MinimumSpanningTree {
	private double cost;
	
	public MinimumSpanningTree(List<Cell> cells) {
		buildMst(cells);
	}
	
	public MinimumSpanningTree(List<Cell> cells, BitSet cellsLeftover) {
		if (cellsLeftover.cardinality() == cellsLeftover.size()) {
			buildMst(cells);
		} else {		
			List<Cell> newList = new ArrayList<Cell>();
			
			for (int i = 0; i < cellsLeftover.size(); i++) {
	            if (cellsLeftover.get(i)) {                
	                newList.add(cells.get(i));
	            }
	        }
			
			buildMst(newList);
		}
	}
	
	private void buildMst(List<Cell> cells) {
		PriorityQueue<Edge> allEdges = new PriorityQueue<Edge>();
		List<HashMap<String, Cell>> forest = new ArrayList<HashMap<String, Cell>>();
		
		for (int i = 0; i < cells.size() - 1; i++) {
			Cell cell = cells.get(i);
			HashMap<String, Cell> tree = new HashMap<String, Cell>();
			tree.put(cell.toString(), cell);
			forest.add(tree);
			
			for (int j = i + 1; j < cells.size(); j++) {
				allEdges.add(new Edge(cell, cells.get(j)));
			}			
		}
		
		buildMst(allEdges, forest);
	}
	
	public double getCost() {
		return cost;
	}
	
	private List<Edge> buildMst(PriorityQueue<Edge> allEdges, List<HashMap<String, Cell>> forest) {
		List<Edge> mstEdges = new ArrayList<Edge>();
		
		while (!allEdges.isEmpty() && forest.size() > 1) {
			Edge minimumEdge = allEdges.poll();
			Cell cellA = minimumEdge.getCellA();
			Cell cellB = minimumEdge.getCellB();
			int indexTreeA = -1;
			int indexTreeB = -1;
			
			for (int i = 0; i < forest.size(); i++) {
				HashMap<String, Cell> tree = forest.get(i);
				
				if (tree.containsKey(cellA.toString())) {
					indexTreeA = i;
				}
				
				if (tree.containsKey(cellB.toString())) {
					indexTreeB = i;
				}
			}
			
			if (indexTreeA != indexTreeB) {
				mergeTrees(forest, indexTreeA, indexTreeB);
				mstEdges.add(minimumEdge);
				cost = cost + minimumEdge.getCost();
			}
		}
		
		return mstEdges;
	}
	
	private void mergeTrees(List<HashMap<String, Cell>> forest, int indexTreeA, int indexTreeB) {
		HashMap<String, Cell> treeA = forest.get(indexTreeA);
		
		for (Cell cell : forest.get(indexTreeB).values()) {
			treeA.put(cell.toString(), cell);
		}
		
		forest.remove(indexTreeB);
	}
}
