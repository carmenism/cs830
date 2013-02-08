import java.util.List;


public class Greedy extends SearchAlgorithm {
    public Greedy(State initialState) {
        super(initialState);
        
        algorithm = this;
    }

    @Override
    public void search() {
        // TODO Auto-generated method stub

    }

    @Override
    protected List<Node> expand(Node node) {
        // TODO Auto-generated method stub
        return null;
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
