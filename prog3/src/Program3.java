import java.util.HashMap;
import java.util.HashSet;


public class Program3 {
    public static HashMap<String, Predicate> predicates = new HashMap<String, Predicate>();
    
    public static HashMap<String, Constant> constants = new HashMap<String, Constant>();
    public static HashSet<PredicateSpec> predSpecs = new HashSet<PredicateSpec>();
    public static HashSet<Action> actions = new HashSet<Action>();
        
    public static HashSet<PredicateUsage> initial = new HashSet<PredicateUsage>();
    public static HashSet<PredicateUsage> goal = new HashSet<PredicateUsage>();
    public static HashSet<PredicateUsage> goalNeg = new HashSet<PredicateUsage>();
    
    public Program3() {
        Parser.parseInput();
    }
    
    public static void main(String [] args) {
        
    }
}
