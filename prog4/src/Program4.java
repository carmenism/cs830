import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    private StateAction[][] T;
    public static HashMap<Integer, Double> stateIndexToReward = new HashMap<Integer, Double>();
    public static double[] U;

    private static HashMap<String, Integer> stateToIndex = new HashMap<String, Integer>();
    private static HashMap<String, Integer> actionToIndex = new HashMap<String, Integer>();

    private static int numStatesEncountered = 0;
    private static int numActionsEncountered = 0;

    enum Algorithm {
        RANDOM, GREEDY, Q, VI
    }

    public static Algorithm algorithm;

    private double discount;
    private int numberStates;
    private int numberActions;
    private double maxReward;

    private double totalReward;
    private String currentState;

    public Program4(String[] args) {
        parseArgs(args);
        parseStandardIn();
    }

    private void parseStandardIn() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean firstLine = true;

        try {
            while ((line = br.readLine()) != null) {
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
                        List<String> actions = parseActions(line);

                        String action = chooseAction(currentState, actions);
                        System.out.println(action);

                        if ((line = br.readLine()) == null) {
                            break;
                        }

                        double reward = parseReward(line);

                        line = br.readLine();
                        String oldState = currentState;
                        currentState = parseState(line);

                        updateT(oldState, action, currentState);
                        addReward(currentState, reward);
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void updateT(String oldState, String action, String newState) {
        int oldStateIndex = lookupStateIndex(oldState);
        int actionIndex = lookupActionIndex(action);
        int newStateIndex = lookupStateIndex(newState);

        StateAction sa = T[oldStateIndex][actionIndex];
        sa.statePrimes[newStateIndex]++;
    }

    private String chooseAction(String state, List<String> actions) {
        switch (Program4.algorithm) {
        case GREEDY:
            return chooseGreedy(state, actions);
        case Q:
            return chooseQ(state, actions);
        case RANDOM:
            return chooseRandom(actions);
        case VI:
            return chooseVI(state, actions);
        }

        return null;
    }

    private void updateU() {
        double[] newU = new double[numberStates];
        double loss = 1;
        double bound = 0.1;

        int i = 0;
        while (true) {
            i++;
            for (int stateIndex = 0; stateIndex < U.length; stateIndex++) {
                double max = Double.MIN_VALUE;

                for (int actionIndex = 0; actionIndex < numberActions; actionIndex++) {
                    StateAction sa = T[stateIndex][actionIndex];

                    double expectedValue = sa.getExpectedUtility();

                    if (expectedValue > max) {
                        max = expectedValue;
                    }
                }

                double reward = lookupRewardFromIndex(stateIndex);
                newU[stateIndex] = reward + discount * max;
            }

            double maxDiff = 0.0;

            for (int stateIndex = 0; stateIndex < U.length; stateIndex++) {
                double diff = Math.abs(U[stateIndex] - newU[stateIndex]);

                if (diff > maxDiff) {
                    maxDiff = diff;
                }

                U[stateIndex] = newU[stateIndex];
            }

            if (maxDiff <= (loss - bound * (1.0 - discount) / (2 * discount))) {
                break;
            }
        }
    }

    private String chooseVI(String state, List<String> actions) {
        int stateIndex = lookupStateIndex(state);
        String maxAction = null;
        double max = -1 * Double.MAX_VALUE;

        for (String possibleAction : actions) {
            int actionIndex = lookupActionIndex(possibleAction);
            StateAction sa = T[stateIndex][actionIndex];

            double expectedValue = sa.getExpectedUtility();

            if (expectedValue >= max) {
                maxAction = possibleAction;
                max = expectedValue;
            }
        }

        //updateU();

        return maxAction;
    }

    private String chooseQ(String state, List<String> actions) {
        return null;
    }
    
    public static double lookupUtilityByIndex(int stateIndex) {
        return U[stateIndex];
    }

    public static double lookupUtility(String state) {
        int stateIndex = lookupStateIndex(state);

        return U[stateIndex];
    }

    private String chooseGreedy(String state, List<String> actions) {
        int stateIndex = lookupStateIndex(state);
        String maxAction = null;
        double max = -1 * Double.MAX_VALUE;

        for (String possibleAction : actions) {
            int actionIndex = lookupActionIndex(possibleAction);
            StateAction sa = T[stateIndex][actionIndex];

            double expectedValue = sa.getExpectedValue();

            if (expectedValue > max) {
                maxAction = possibleAction;
                max = expectedValue;
            }
        }

        if (maxAction != null) {
            return maxAction;
        }

        return chooseRandom(actions);
    }

    private String chooseRandom(List<String> actions) {
        Random random = new Random();

        String action = actions.get(random.nextInt(actions.size()));

        return action;
    }

    public static int lookupStateIndex(String state) {
        Integer stateIndex = stateToIndex.get(state.trim());

        if (stateIndex == null) {
            stateIndex = numStatesEncountered;

            stateToIndex.put(state.trim(), stateIndex);
            numStatesEncountered++;
        }

        return stateIndex;
    }

    public static int lookupActionIndex(String action) {
        Integer actionIndex = actionToIndex.get(action.trim());

        if (actionIndex == null) {
            actionIndex = numActionsEncountered;

            actionToIndex.put(action.trim(), actionIndex);
            numActionsEncountered++;
        }

        return actionIndex;
    }

    public static void addReward(String state, double reward) {
        int stateIndex = lookupStateIndex(state);

        if (!stateIndexToReward.containsKey(stateIndex)) {
            stateIndexToReward.put(stateIndex, reward);
        }
    }

    public static double lookupRewardFromIndex(int stateIndex) {
        Double reward = stateIndexToReward.get(stateIndex);

        if (reward != null) {
            return reward;
        }

        return 0;
    }

    public static double lookupReward(String state) {
        int stateIndex = lookupStateIndex(state);

        return lookupRewardFromIndex(stateIndex);
    }

    /*
     * private void addReward(double reward) { totalReward = totalReward +
     * discount * reward; }
     */

    private String parseState(String line) {
        String s = line.replace(STATE, "").trim();

        return s;
    }

    private double parseReward(String line) {
        String r = line.replace(REWARD, "").trim();

        return Double.parseDouble(r);
    }

    private List<String> parseActions(String line) {
        int firstSpace = line.indexOf(" ");
        String a = line.substring(firstSpace).replace(ACTIONS, "").trim();

        String[] tokens = a.split(" ");
        List<String> actions = new ArrayList<String>();

        for (String token : tokens) {
            actions.add(token.trim());
        }

        return actions;
    }

    private void parseFirstLine(String line) {
        String newLine = line.replace(INITIALIZE, "").trim();

        String[] tokens = newLine.split(", ");

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

        T = new StateAction[numberStates][numberActions];

        for (int action = 0; action < numberActions; action++) {
            for (int state = 0; state < numberStates; state++) {
                T[state][action] = new StateAction(numberStates);
            }
        }

        U = new double[numberStates];

        for (int state = 0; state < numberStates; state++) {
            U[state] = 0.0;
        }
    }

    private void parseArgs(String[] args) {
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

    public static void main(String[] args) {
        new Program4(args);
    }
}
