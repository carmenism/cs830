/**
 * Defines a class to represent uniform cost search. Uniform cost is a
 * best-first search that sorts the open list based on g(n), the cost function.
 * 
 * @author Carmen St. Jean
 * 
 */
public class UniformCost extends BestFirstSearch {
    public UniformCost(State initialState) {
        super(initialState);
    }

    @Override
    public int compare(Node n1, Node n2) {
        // Sort lowest to highest cost.
        if (n1.getG() < n2.getG()) {
            return BEFORE;
        }

        if (n1.getG() > n2.getG()) {
            return AFTER;
        }
        
        return EQUAL;
    }
}
