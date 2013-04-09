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
    public static HashMap<Integer, Double> R = new HashMap<Integer, Double>();
    public static double[] U;
    public static double[][] Q;

    private static HashMap<State, Integer> stateToIndex = new HashMap<State, Integer>();
    private static HashMap<Action, Integer> actionToIndex = new HashMap<Action, Integer>();

    private static int numStatesEncountered = 0;
    private static int numActionsEncountered = 0;

    public static int k = 4;

    enum Algorithm {
        RANDOM, GREEDY, Q, VI
    }

    public static Algorithm algorithm;

    private double discount;
    private int numberStates;
    private int numberActions;
    public static double maxReward;

    private State currentState;

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
                        List<Action> actions = parseActions(line);

                        Action action = chooseAction(currentState, actions);
                        System.out.println(action);

                        if ((line = br.readLine()) == null) {
                            break;
                        }

                        double reward = parseReward(line);

                        line = br.readLine();
                        State oldState = currentState;
                        currentState = parseState(line);

                        update(oldState, action, currentState, reward);
                        addReward(currentState, reward);
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void update(State oldState, Action action, State newState,
            double reward) {
        if (algorithm == Algorithm.Q) {
            updateQ(oldState, action, newState, reward);
        } else {
            updateT(oldState, action, newState, reward);

            if (algorithm == Algorithm.VI) {
                updateU();
            }
        }
    }

    private void updateT(State oldState, Action action, State newState,
            double reward) {
        int oldStateIndex = lookupStateIndex(oldState);
        int actionIndex = lookupActionIndex(action);
        int newStateIndex = lookupStateIndex(newState);
        
        StateAction sa = T[oldStateIndex][actionIndex];
        sa.statePrimes[newStateIndex]++;
        sa.addTimeTaken();
    }
    
    private Action chooseAction(State state, List<Action> actions) {
        switch (algorithm) {
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

    private void updateQ(State s, Action a, State sPrime, double r) {
        int sIndex = lookupStateIndex(s);
        int aIndex = lookupActionIndex(a);
        int sPrimeIndex = lookupStateIndex(sPrime);

        double alpha = 1.0 / numberActions;
        double qsa = Q[sIndex][aIndex];
        double max = -1 * Double.MAX_VALUE;

        for (int action = 0; action < numberActions; action++) {
            double q = Q[sPrimeIndex][action];

            if (q > max) {
                max = q;
            }
        }

        Q[sIndex][aIndex] = qsa + alpha * (discount * (r + max) - qsa);
    }

    private void updateU() {
        double[] newU = new double[numberStates];
        double loss = 0.8;
        double bound = 0.1;

        int i = 0;
        while (true) {
            i++;
            for (int stateIndex = 0; stateIndex < U.length; stateIndex++) {
                double max = Double.MIN_VALUE;

                for (int actionIndex = 0; actionIndex < numberActions; actionIndex++) {
                    StateAction sa = T[stateIndex][actionIndex];

                    double f = sa.getF();

                    if (f > max) {
                        max = f;
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

    private Action chooseVI(State state, List<Action> actions) {
        int stateIndex = lookupStateIndex(state);
        Action maxAction = null;
        double max = -1 * Double.MAX_VALUE;

        for (Action possibleAction : actions) {
            int actionIndex = lookupActionIndex(possibleAction);
            StateAction sa = T[stateIndex][actionIndex];

            /*
             * if (sa.isUnexplored()) { return possibleAction; }
             */

            // double expectedValue = sa.getExpectedUtility();
            double expectedValue = sa.getF();

            if (expectedValue >= max) {
                maxAction = possibleAction;
                max = expectedValue;
            }
        }

        return maxAction;
    }

    private Action chooseQ(State state, List<Action> actions) {
        int stateIndex = lookupStateIndex(state);
        double max = 0;//-1 * Double.MAX_VALUE;
        Action maxAction = null;

        for (Action possibleAction : actions) {
            int actionIndex = lookupActionIndex(possibleAction);
            double q = Q[stateIndex][actionIndex];

            if (q > max) {
                max = q;
                maxAction = possibleAction;
            }
        }
        
        if (max == 0) {
            return chooseRandom(actions);
        }
        
        return maxAction;
    }

    public static double lookupUtilityByIndex(int stateIndex) {
        return U[stateIndex];
    }

    public static double lookupUtility(State state) {
        int stateIndex = lookupStateIndex(state);

        return U[stateIndex];
    }

    private Action chooseGreedy(State state, List<Action> actions) {
        int stateIndex = lookupStateIndex(state);
        Action maxAction = null;
        double max = -1 * Double.MAX_VALUE;

        for (Action possibleAction : actions) {
            int actionIndex = lookupActionIndex(possibleAction);
            StateAction sa = T[stateIndex][actionIndex];

            if (sa.isUnexplored()) {
                return possibleAction;
            }

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

    private Action chooseRandom(List<Action> actions) {
        Random random = new Random();

        Action action = actions.get(random.nextInt(actions.size()));

        return action;
    }

    public static int lookupStateIndex(State state) {
        Integer stateIndex = stateToIndex.get(state);

        if (stateIndex == null) {
            stateIndex = numStatesEncountered;

            stateToIndex.put(state, stateIndex);
            numStatesEncountered++;
        }

        return stateIndex;
    }

    public static int lookupActionIndex(Action action) {
        Integer actionIndex = actionToIndex.get(action);

        if (actionIndex == null) {
            actionIndex = numActionsEncountered;

            actionToIndex.put(action, actionIndex);
            numActionsEncountered++;
        }

        return actionIndex;
    }

    public static void addReward(State state, double reward) {
        int stateIndex = lookupStateIndex(state);

        if (!R.containsKey(stateIndex)) {
            R.put(stateIndex, reward);
        }
    }

    public static double lookupRewardFromIndex(int stateIndex) {
        Double reward = R.get(stateIndex);

        if (reward != null) {
            return reward;
        }

        return 0;
    }

    public static double lookupReward(State state) {
        int stateIndex = lookupStateIndex(state);

        return lookupRewardFromIndex(stateIndex);
    }

    private State parseState(String line) {
        return new State(line.replace(STATE, "").trim());
    }

    private double parseReward(String line) {
        String r = line.replace(REWARD, "").trim();

        return Double.parseDouble(r);
    }

    private List<Action> parseActions(String line) {
        int firstSpace = line.indexOf(" ");
        String a = line.substring(firstSpace).replace(ACTIONS, "").trim();

        String[] tokens = a.split(" ");
        List<Action> actions = new ArrayList<Action>();

        for (String token : tokens) {
            actions.add(new Action(token));
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
        Q = new double[numberStates][numberActions];

        for (int action = 0; action < numberActions; action++) {
            for (int state = 0; state < numberStates; state++) {
                T[state][action] = new StateAction(numberStates);
                Q[state][action] = 0.0;
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
