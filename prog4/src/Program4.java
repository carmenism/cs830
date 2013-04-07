import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Program4 {
    public static final String ALG_RANDOM = "random";
    public static final String ALG_GREEDY = "greedy";
    public static final String ALG_Q = "q";
    public static final String ALG_VI = "vi";
    
    public static final String INITIALIZE = "initialize:";
    public static final String INITIALIZE_STATES = "states";
    public static final String INITIALIZE_ACTIONS = "actions";
    public static final String INITIALIZE_MAX_REWARD = "max reward";
    
    public static final String STATE = "state:";
    public static final String ACTIONS = "actions:";    
    public static final String REWARD = "reward:";
    
    public static final String NEW_TRIAL = "-------- new trial ----------";
    public static final String TERMINATION = "goodbye!";

    private HashMap<Integer, Step> stepTable = new HashMap<Integer, Step>();
    public static HashMap<Integer, Double> R = new HashMap<Integer, Double>();
    
    enum Algorithm {
        RANDOM,
        GREEDY,
        Q,
        VI
    }
    
    public static Algorithm algorithm;
    
    private double discount;
    private int numberStates;
    private int numberActions;
    private double maxReward;
    
    private double totalReward;
    private int currentState;
   
    public Program4(String [] args) {
        parseArgs(args);
        parseStandardIn();
    }
    
    private void parseStandardIn() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line; 
        boolean firstLine = true;       
        List<Step> steps = new ArrayList<Step>();

        try {
            while ((line = br.readLine()) != null) {     
                //System.out.println(line);                  
                if (firstLine) {
                    firstLine = false;
                    
                    parseFirstLine(line);
                } else {
                    if (line.equals(TERMINATION)) {
                        System.exit(0);
                    } else if (line.equals(NEW_TRIAL)) {
                        // ????
                        line = br.readLine();
                        currentState = parseState(line);
                    } else if (line.contains(ACTIONS)) {
                        List<Integer> actions = parseActions(line);
                        
                        Step step = lookupStep(currentState);
                        steps.add(step);
                        
                        int action = step.chooseAction(actions);
                        System.out.println(action);                        
                        
                        line = br.readLine();
                        double reward = parseReward(line);
                        
                        line = br.readLine();
                        currentState = parseState(line);
                        
                        step.addToHistory(action, currentState);
                        addReward(reward);
                        
                        if (!R.containsKey(currentState)) {
                            R.put(currentState, reward);
                        }
                    }
                 }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Step lookupStep(int state) {
        Step step = stepTable.get(state);
        
        if (step == null) {
            step = new Step(state);
            
            stepTable.put(state, step);
        }
        
        return step;
    }
        
    public static double lookupReward(int state) {
        Double reward = R.get(state);
        
        if (reward != null) {
            return reward;
        }
        
        return 0;
    }
    
    private void addReward(double reward) {
        totalReward = totalReward + discount * reward;
    }
    
    private int parseState(String line) {       
        String s = line.replace(STATE, "").trim();
        
        return Integer.parseInt(s);
    }

    private double parseReward(String line) {
        String r = line.replace(REWARD, "").trim();
        
        return Double.parseDouble(r);    
    }
    
    private List<Integer> parseActions(String line) {
        int firstSpace = line.indexOf(" ");
        String a = line.substring(firstSpace).replace(ACTIONS, "").trim();
        
        String [] tokens = a.split(" ");
        List<Integer> actions = new ArrayList<Integer>();
        
        for (String token : tokens) {
            actions.add(Integer.parseInt(token.trim()));
        }
        
        return actions;
    }
        
    private void parseFirstLine(String line) {
        String newLine = line.replace(INITIALIZE, "").trim();
        
        String [] tokens = newLine.split(", ");
        
        for (String token : tokens) {
            if (token.contains(INITIALIZE_STATES)) {
                String num = token.replaceAll(INITIALIZE_STATES, "");
                
                numberStates = Integer.parseInt(num.trim());
            } else if (token.contains(INITIALIZE_ACTIONS)) {
                String num = token.replaceAll(INITIALIZE_ACTIONS, "");
                
                numberActions = Integer.parseInt(num.trim());
            } else if (token.contains(INITIALIZE_MAX_REWARD)) {
                String num = token.replaceAll(INITIALIZE_MAX_REWARD, "");
                
                maxReward = Double.parseDouble(num.trim());
            }
        }
    }
    
    private void parseArgs(String [] args) {
        if (args.length != 2) {
            System.exit(1);
        }
        
        String alg = args[0];
        String dis = args[1];
        
        if (alg.equals(ALG_RANDOM)) {
            algorithm = Algorithm.RANDOM;
        } else if (alg.equals(ALG_GREEDY)) {
            algorithm = Algorithm.GREEDY;
        } else if (alg.equals(ALG_Q)) {
            algorithm = Algorithm.Q;            
        } else if (alg.equals(ALG_VI)) {
            algorithm = Algorithm.VI;
        }
        
        discount = Double.parseDouble(dis.trim());
    }
    
    public static void main(String [] args) {        
        new Program4(args);        
    }
}
