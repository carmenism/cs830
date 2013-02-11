/**
 * CS 830 - Artificial Intelligence
 * Program 1 - Vacuum World
 * 
 * @author Carmen St. Jean
 *
 */
public class Program1 {
    public static final String CMD_DEPTH_FIRST = "depth-first";
    public static final String CMD_DEPTH_FIRST_ID = "depth-first-id";
    public static final String CMD_UNIFORM_COST = "uniform-cost";
    public static final String CMD_A_STAR = "a-star";
    public static final String CMD_IDA_STAR = "ida-star";
    public static final String CMD_GREEDY = "greedy";
    
	/**
	 * Creates the program from the command line arguments and standard input.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
    public Program1(String [] args) {
        SearchAlgorithm algorithm = getAlgorithmFromArgs(args);
        
        Solution solution = algorithm.search();
        
        if (solution != null) {
        	solution.print();
        } else {
        	System.err.println("Solution could not be found.");
        }
    }
    
	/**
	 * Determines the algorithm from command line arguments and builds the
	 * vacuum world from standard in put.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @return The search algorithm to be used.
	 */
    private static SearchAlgorithm getAlgorithmFromArgs(String [] args) {
        SearchAlgorithm algorithm = null;
        
        if (args.length == 0) {
            System.err.println("Must specify at least one argument.");
            System.exit(1);
        }
        
        boolean useBattery = determineBatteryUsage(args);
        
        if (args.length > 1) {
        	Heuristic.setHeuristic(args[1]);
        }
        
        String cmdAlgorithm = args[0];
        VacuumWorld vw = new VacuumWorld(useBattery);
        
        if (cmdAlgorithm.equals(CMD_DEPTH_FIRST)) {
            algorithm = new DepthFirstSearch(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_DEPTH_FIRST_ID)) {
        	algorithm = new IDDepthFirstSearch(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_UNIFORM_COST)) {
            algorithm = new UniformCost(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_A_STAR)) {
           	algorithm = new AStar(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_GREEDY)) {
        	algorithm = new Greedy(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_IDA_STAR)) {
        	algorithm = new IDAStar(vw.getInitialState());
        }
               
        return algorithm;
    }

	/**
	 * Determines if the user specified that a battery should be considered in
	 * the problem.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @return True if the battery should be used in the program.
	 */
    private static boolean determineBatteryUsage(String [] args) {
    	for (String arg : args) {
        	if (arg.equals("-battery")) {
        		return true;
        	}
        }
    	
    	return false;
    }
    
    public static void main(String [] args) {
        new Program1(args);
    }
}
