import java.util.List;


public class AStar extends SearchAlgorithm {
    public AStar(State initialState) {
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
