import java.io.FileNotFoundException;


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
	
	public Program1(String [] args) throws FileNotFoundException {
		VacuumWorld vw = new VacuumWorld();
		SearchAlgorithm algorithm = null;
				
		if (args.length == 0) {
			System.err.println("Must specify at least one argument.");
			System.exit(1);
		} else if (args.length > 2) {
			System.err.println("Must specify no more than two arguments.");
			System.exit(1);
		}
		
		String cmdAlgorithm = args[0];
		
		if (args.length == 1) {
			if (cmdAlgorithm.equals(CMD_DEPTH_FIRST)) {
				algorithm = new DepthFirstSearch(vw.getInitialState());
			} else if (cmdAlgorithm.equals(CMD_DEPTH_FIRST_ID)) {
			
			} else if (cmdAlgorithm.equals(CMD_UNIFORM_COST)) {
					
			} else if (cmdAlgorithm.equals(CMD_A_STAR)) {
				System.err.println("Must also specify a heuristic for a-star.");
				System.exit(1);
			}
		} else if (cmdAlgorithm.equals(CMD_A_STAR)) {
			String heuristic = args[1];
			
			if (heuristic.equals(CMD_H0)) {
				
			} else if (heuristic.equals(CMD_H1)) {
				
			} else if (heuristic.equals(CMD_H2)) {
				
			}
		}
		
		algorithm.search();
	}
		
	public static void main(String [] args) {
		try {
			new Program1(args);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
