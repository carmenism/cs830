import java.util.List;

/**
 * Represents a solution to the vacuum robot planning problem.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Solution {
    private final List<SolutionStep> stepsTaken;
    private final int nodesGenerated;
    private final int nodesExpanded;

    /**
     * Creates a solution.
     * 
     * @param path
     *            The path for the solution.
     * @param nodesGenerated
     *            The number of nodes generated to reach the solution.
     * @param nodesExpanded
     *            The number of nodes expanded to reach the solution.
     */
    public Solution(List<SolutionStep> stepsTaken, int nodesGenerated, int nodesExpanded) {
        this.stepsTaken = stepsTaken;
        this.nodesGenerated = nodesGenerated;
        this.nodesExpanded = nodesExpanded;
    }

    /**
     * Prints the solution's path to standard out with one action per line.
     */
    private void printActionsTaken() {
        for (SolutionStep step : stepsTaken) {
            System.out.println(step);
        }
    }

    /**
     * Prints the solution to standard out.
     */
    public void print() {
        if (stepsTaken != null) {
            printActionsTaken();
        } else {
            System.out.println("No plan found.");
        }

        System.out.println(nodesGenerated + " nodes generated");
        System.out.println(nodesExpanded + " nodes expanded");
    }

    /**
     * Gets the length of the solution - i.e., the number of actions taken.
     * 
     * @return The length of the solution.
     */
    public int getLength() {
        return stepsTaken.size();
    }
    
    public List<SolutionStep> getStepsTaken() {
        return stepsTaken;
    }

    /**
     * Gets the number of nodes generated to reach this solution.
     * 
     * @return The number of nodes generated.
     */
    public int getNodesGenerated() {
        return nodesGenerated;
    }

    /**
     * Gets the number of nodes expanded to reach this solution.
     * 
     * @return The number of nodes expanded.
     */
    public int getNodesExpanded() {
        return nodesExpanded;
    }
}
