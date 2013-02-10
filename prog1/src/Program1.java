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
    
    public static final String CMD_H0 = "h0";
    public static final String CMD_H1 = "h1";
    public static final String CMD_H2 = "h2";
    public static final String CMD_H3 = "h3";
    
    public Program1(String [] args) {
        SearchAlgorithm algorithm = null;
                
        if (args.length == 0) {
            System.err.println("Must specify at least one argument.");
            System.exit(1);
        }
        
        boolean useBattery = false;
        
        for (String arg : args) {
        	if (arg.equals("-battery")) {
        		useBattery = true;
        	}
        }
        
        String cmdAlgorithm = args[0];
        String cmdHeuristic = null;
        VacuumWorld vw = new VacuumWorld(useBattery);
        
        if (cmdAlgorithm.equals(CMD_DEPTH_FIRST)) {
            algorithm = new DepthFirstSearch(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_DEPTH_FIRST_ID)) {
        	algorithm = new IDDepthFirstSearch(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_UNIFORM_COST)) {
            algorithm = new UniformCost(vw.getInitialState());
        } else if (cmdAlgorithm.equals(CMD_A_STAR)) {
           	algorithm = new AStar(vw.getInitialState());
           	cmdHeuristic = args[1];
        } else if (cmdAlgorithm.equals(CMD_GREEDY)) {
        	algorithm = new Greedy(vw.getInitialState());
        	cmdHeuristic = args[1];
        } else if (cmdAlgorithm.equals(CMD_IDA_STAR)) {
        	algorithm = new IDAStar(vw.getInitialState());
        	cmdHeuristic = args[1];
        }
        
        if (cmdHeuristic != null) {
        	if (cmdHeuristic.equals(CMD_H0)) {
        		SearchAlgorithm.heuristic = SearchAlgorithm.Heuristic.H0;
        	} else if (cmdHeuristic.equals(CMD_H1)) {
        		SearchAlgorithm.heuristic = SearchAlgorithm.Heuristic.H1;
        	} else if (cmdHeuristic.equals(CMD_H2)) {
        		SearchAlgorithm.heuristic = SearchAlgorithm.Heuristic.H2;
        	} else if (cmdHeuristic.equals(CMD_H3)) {
        		SearchAlgorithm.heuristic = SearchAlgorithm.Heuristic.H3;
        	}
        }
        
        Solution solution = algorithm.search();
        
        if (solution != null) {
        	solution.print();
        	System.out.println(solution.getLength());
        } else {
        	System.err.println("Solution could not be found.");
        }
    }
        
    public static void main(String [] args) {
        new Program1(args);
    }
}
