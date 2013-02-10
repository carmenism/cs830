public class Solution {
	private String path;
	private int nodesGenerated;
	private int nodesExpanded;

	public Solution(String path, int nodesGenerated, int nodesExpanded) {
		this.path = path;
		this.nodesGenerated = nodesGenerated;
		this.nodesExpanded = nodesExpanded;
	}

	private void printPath() {
		for (int i = 0; i < path.length(); i++) {
			System.out.println(path.charAt(i));
		}
	}

	public void print() {
		printPath();

		System.out.println(nodesGenerated + " nodes generated");
		System.out.println(nodesExpanded + " nodes expanded");
	}
	
	public int getLength() {
		return path.length();
	}

	public String getPath() {
		return path;
	}

	public int getNodesGenerated() {
		return nodesGenerated;
	}

	public int getNodesExpanded() {
		return nodesExpanded;
	}
}
