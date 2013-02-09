public class AStar extends BestFirstSearch {
    public AStar(State initialState) {
        super(initialState);
    }
       
    @Override
    protected int compareTo(Node n1, Node n2) {
        if (n1.getF() < n2.getF()) {
            return BEFORE;
        } else if (n1.getF() > n2.getF()) {
            return AFTER;
        }
        
        if (n1.getG() > n2.getG()) {
            return BEFORE;
        } else if (n1.getG() < n2.getF()) {
            return AFTER;
        }
        
        return EQUAL;
    }
}
