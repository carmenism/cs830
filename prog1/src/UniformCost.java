public class UniformCost extends BestFirstSearch {
    public UniformCost(State initialState) {
        super(initialState);
    }

    @Override
    protected int compareTo(Node n1, Node n2) {
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
