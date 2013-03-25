import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Program3 {
    public static boolean isParallel = false;
    
    public static double w = 1.0;

    public static HashMap<String, Constant> constants = new HashMap<String, Constant>();
    public static HashMap<Integer, ConstantSet> constantSets = new HashMap<Integer, ConstantSet>();

    public static HashMap<String, UngroundedPredicate> ungroundedPreds = new HashMap<String, UngroundedPredicate>();
    public static HashSet<UngroundedAction> ungroundedActions = new HashSet<UngroundedAction>();

    public static HashSet<Action> groundedActions = new HashSet<Action>();
    public static HashMap<Predicate, Predicate> groundedPredicates = new HashMap<Predicate, Predicate>();

    public static HashSet<Predicate> initial = new HashSet<Predicate>();
    public static HashSet<Predicate> goal = new HashSet<Predicate>();
    public static HashSet<Predicate> goalNeg = new HashSet<Predicate>();

    public Program3(String[] args) {
        parseArgs(args);
        Parser.parseInput();

        addAllGroundedPredicates();
        groundAllActions();

        State initialState = getInitialState();
        Solution solution = new AStar(initialState).search();

        if (solution != null) {
            solution.print();
        } else {
            System.err.println("No plan found.");
        }
    }

    private static void usage() {
        System.err.println("Usage: ./run.sh [-parallel] <weight> <heuristic>");
        System.err.println();
        System.err.println("where <weight> is the weight for weighted A* and");
        System.err
                .println("and <heuristic> is either h0, h-goal-lits, h1, or h1sum.");
        System.err
                .println("Expects a problem domain and instance on standard input.");
    }

    private static void parseArgs(String[] args) {        
        String weight = args[0];
        String heuristic = args[1];
        
        if (args[0].equals("-parallel")) {
            weight = args[1];
            heuristic = args[2];
            
            isParallel = true;
        }
        
        try {
            w = Double.parseDouble(weight);
        } catch (NumberFormatException nfe) {
            usage();
            System.exit(1);
        }
        
        if (w < 1) {
            System.err.println("Weight should be greater than or equal to 1.");
            System.exit(1);
        }

        if (heuristic.equals("h0")) {
            Heuristic.type = Heuristic.Type.H0;
        } else if (heuristic.equals("h1")) {
            Heuristic.type = Heuristic.Type.H1_MAX;
        } else if (heuristic.equals("h1sum")) {
            Heuristic.type = Heuristic.Type.H1_SUM;
        } else if (heuristic.equals("h-goal-lits")) {
            Heuristic.type = Heuristic.Type.H_GOAL_LITS;
        } else {
            usage();
            System.exit(1);
        }
    }

    private static State getInitialState() {
        HashSet<Predicate> initialNeg = new HashSet<Predicate>();

        for (Predicate predicate : groundedPredicates.values()) {
            if (!initial.contains(predicate)) {
                initialNeg.add(predicate);
            }
        }

        return new State(new ArrayList<SolutionStep>(), initial, initialNeg, 0);
    }

    private static void groundAllActions() {
        List<Constant> allConstants = new ArrayList<Constant>(
                constants.values());

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
            groundedPredicates.put(p, p);
        }

        for (Predicate p : goal) {
            groundedPredicates.put(p, p);
        }

        for (Predicate p : goalNeg) {
            groundedPredicates.put(p, p);
        }
    }

    public static void main(String[] args) {
        new Program3(args);
    }
}
