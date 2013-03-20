import java.util.HashMap;
import java.util.HashSet;


public class Program3 {
    public static double w = 1.0;
    
//    public static HashMap<String, Predicate> predicates = new HashMap<String, Predicate>();
    
    public static HashMap<String, Constant> constants = new HashMap<String, Constant>();
    public static HashMap<String, UngroundedPredicate> ungroundedPreds = new HashMap<String, UngroundedPredicate>();
    public static HashSet<UngroundedAction> ungroundedActions = new HashSet<UngroundedAction>();
        
    public static HashSet<Predicate> initial = new HashSet<Predicate>();
    public static HashSet<Predicate> goal = new HashSet<Predicate>();
    public static HashSet<Predicate> goalNeg = new HashSet<Predicate>();
    
    public Program3() {
        Parser.parseInput();
    }
    
    public static void main(String [] args) {
        
    }
}
