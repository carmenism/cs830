import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Program3 {
    public static double w = 1.0;
      
    public static HashMap<String, Constant> constants = new HashMap<String, Constant>();
    public static HashMap<Integer, ConstantSet> constantSets = new HashMap<Integer, ConstantSet>();
    
    public static HashMap<String, UngroundedPredicate> ungroundedPreds = new HashMap<String, UngroundedPredicate>();
    public static HashSet<UngroundedAction> ungroundedActions = new HashSet<UngroundedAction>();

    public static HashSet<Action> groundedActions = new HashSet<Action>();
    public static HashMap<Predicate, Predicate> allGroundedPredicates = new HashMap<Predicate, Predicate>();
        
    public static HashSet<Predicate> initial = new HashSet<Predicate>();
    public static HashSet<Predicate> goal = new HashSet<Predicate>();
    public static HashSet<Predicate> goalNeg = new HashSet<Predicate>();
    
    public Program3() {
        milestone();
    }
    
    private static void milestone() {
        Parser.parseInput();
        
        addAllGroundedPredicates();
        
        groundAllActions();
        
        State initialState = getInitialState();
        
        HashSet<Action> actions = initialState.getPossibleActions();
        
        for (Action action : actions) {
            System.out.println(action.toStringDetails() + "\n");
        }
    }
    
    private static State getInitialState() {
        HashSet<Predicate> initialNeg = new HashSet<Predicate>();
        
        for (Predicate predicate : allGroundedPredicates.values()) {
            if (!initial.contains(predicate)) {
                initialNeg.add(predicate);
            }
        }
        
        return new State(initial, initialNeg);
    }
    
    private static void groundAllActions() {        
        List<Constant> allConstants = new ArrayList<Constant>(constants.values());
        
        for (UngroundedAction ugAction : ungroundedActions) {          
            int length = ugAction.length();
                        
            ConstantSet cs = constantSets.get(length);
            
            if (cs == null) {
                cs = new ConstantSet(length, allConstants);
                
                constantSets.put(length, cs);
            }
            
            groundedActions.addAll(ugAction.ground(cs));
        }
    }
    
    private static void addAllGroundedPredicates() {
        for (Predicate p : initial) {
            allGroundedPredicates.put(p, p);
        }
        
        for (Predicate p : goal) {
            allGroundedPredicates.put(p, p);
        }
        
        for (Predicate p : goalNeg) {
            allGroundedPredicates.put(p, p);
        }
    }
    
    public static void main(String [] args) {
        new Program3();
    }
}
