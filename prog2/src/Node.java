import java.util.ArrayList;
import java.util.List;

public class Node {
    List<Node> children = new ArrayList<Node>();
    
    public Node() {
        
    }
    
    public void addChild(Node n) {
        children.add(n);
    }
}
