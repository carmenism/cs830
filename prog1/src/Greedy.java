public class Greedy extends BestFirstSearch {
    public Greedy(State initialState) {
        super(initialState);
    }
    
    @Override
    protected int compareTo(Node n1, Node n2) {
        if (n1.getH() < n2.getH()) {
            return BEFORE;
        }
        
        if (n1.getH() > n2.getH()) {
            return AFTER;
        }
        
        return EQUAL;
    }
}
