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
    public static final String ALG_PI = "pi";

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
    public static double[] R;
    public static double[] U;
    public static double[][] Q;
    public static int[] PI;

    private static HashMap<State, Integer> stateToIndex = new HashMap<State, Integer>();
    private static HashMap<Action, Integer> actionToIndex = new HashMap<Action, Integer>();

    private static int numStatesEncountered = 0;
    private static int numActionsEncountered = 0;

    public static int k = 4;

    private int N = 0;

    enum Algorithm {
        RANDOM, GREEDY, Q, VI, PI
    }

    public static Algorithm algorithm;

    private double discount;
    private int numberStates;
    private int numberActions;
    public static double maxReward;

    public Program4(String[] args) {
        parseArgs(args);
        parseStandardIn();
    }

    private void parseStandardIn() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean firstLine = true;
        State currentState = null;

        try {
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;

                    parseFirstLine(line);
                } else {
                    if (line.equals(TERMINATION)) {
                        System.exit(0);
                    } else if (line.equals(NEW_TRIAL)) {
                        line = br.readLine();
                        currentState = parseState(line);
                        N++;
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
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update(State oldState, Action action, State newState,
            double reward) {
        updateT(oldState, action, newState, reward);
        addReward(newState, reward);

        if (algorithm == Algorithm.Q) {
            updateQ(oldState, action, newState, reward);
        } else if (algorithm == Algorithm.VI) {
            updateU();
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

    private void updateQ(State s, Action a, State sPrime, double r) {
        int sIndex = lookupStateIndex(s);
        int aIndex = lookupActionIndex(a);
        int sPrimeIndex = lookupStateIndex(sPrime);

        double alpha = 1.0 / N;
        double qsa = Q[sIndex][aIndex];
        double max = -1 * Double.MAX_VALUE;

        for (int action = 0; action < numberActions; action++) {
            double q = Q[sPrimeIndex][action];
            // double q = T[sPrimeIndex][action].getF();

            if (q > max) {
                max = q;
            }
        }

        Q[sIndex][aIndex] = qsa + alpha * (discount * (r + max) - qsa);
    }

    private void updateU() {
        double[] newU = new double[numberStates];

        while (true) {
            for (int state = 0; state < U.length; state++) {
                double max = -1 * Double.MAX_VALUE;

                for (int action = 0; action < numberActions; action++) {
                    double f = T[state][action].getF();

                    if (f > max) {
                        max = f;
                    }
                }

                double reward = R[state];
                newU[state] = reward + discount * max;
            }

            double maxDiff = 0.0;

            for (int state = 0; state < U.length; state++) {
                double diff = Math.abs(U[state] - newU[state]);

                if (diff > maxDiff) {
                    maxDiff = diff;
                }

                U[state] = newU[state];
            }

            if (maxDiff <= 0.01) {
                break;
            }
        }
    }

    private void updatePI() {
        double[] Upi = new double[numberStates];

        while (true) {
            boolean unchanged = true;
            for (int s = 0; s < numberStates; s++) {
                int a = PI[s];
                double utility = T[s][a].getExpectedUtility();

                Upi[s] = R[s] + discount * utility;
            }

            for (int s = 0; s < numberStates; s++) {
                U[s] = Upi[s];
            }

            for (int s = 0; s < numberStates; s++) {
                double maxUtility = -1 * Double.MAX_VALUE;
                int maxAction = -1;

                for (int a = 0; a < numberActions; a++) {
                    double expectedUtility = T[s][a].getExpectedUtility();

                    if (expectedUtility > maxUtility) {
                        maxUtility = expectedUtility;
                        maxAction = a;
                    }
                }

                int pAction = PI[s];
                double pUtility = T[s][pAction].getExpectedUtility();

                if (maxUtility > pUtility) {
                    PI[s] = maxAction;
                    unchanged = false;
                }
            }

            if (unchanged) {
                break;
            }
        }
    }

    private Action chooseAction(State state, List<Action> actions) {
        if (algorithm == Algorithm.PI) {
            return choosePI(state, actions);
        }

        int stateIndex = lookupStateIndex(state);
        Action maxAction = null;
        double max = -1 * Double.MAX_VALUE;
        int n = 0;

        for (Action possibleAction : actions) {
            int actionIndex = lookupActionIndex(possibleAction);
            double f = T[stateIndex][actionIndex].getF();

            if (f >= max) {
                maxAction = possibleAction;
                max = f;
                n = T[stateIndex][actionIndex].getNumberTimesTaken();
            }
        }

        if (maxAction != null) {
            //return chooseOtherRandom(maxAction, actions, n);
            return maxAction;
        }

        return chooseRandom(actions);
    }

    private Action choosePI(State state, List<Action> actions) {
        updatePI();
        int stateIndex = lookupStateIndex(state);
        int policyAction = PI[stateIndex];

        for (Action action : actions) {
            int actionIndex = lookupActionIndex(action);

            if (actionIndex == policyAction) {
                int n = T[stateIndex][actionIndex].getNumberTimesTaken();

                return chooseOtherRandom(action, actions, n);
            }
        }

        return chooseLessVisited(stateIndex, actions);
    }

    private Action chooseLessVisited(int stateIndex, List<Action> actions) {
        for (Action action : actions) {
            int actionIndex = lookupActionIndex(action);

            if (T[stateIndex][actionIndex].getNumberTimesTaken() < k) {
                return action;
            }
        }

        return chooseRandom(actions);
    }

    private Action chooseOtherRandom(Action action, List<Action> actions, int n) {
        if (n == 0) {
            return action;
        }

        Random random = new Random(n);

        if (random.nextInt(n) == 0) {
            ArrayList<Action> newList = new ArrayList<Action>();
            newList.addAll(actions);
            newList.remove(action);

            return chooseRandom(newList);
        }

        return action;
    }

    private Action chooseRandom(List<Action> actions) {
        Random random = new Random();

        Action action = actions.get(random.nextInt(actions.size()));

        return action;
    }

    public static double lookupUtilityByIndex(int stateIndex) {
        return U[stateIndex];
    }

    public static double lookupUtility(State state) {
        int stateIndex = lookupStateIndex(state);

        return U[stateIndex];
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

        R[stateIndex] = reward;
    }

    public static double lookupReward(State state) {
        int stateIndex = lookupStateIndex(state);

        return R[stateIndex];
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

        //k = numberActions + 1;
        T = new StateAction[numberStates][numberActions];
        Q = new double[numberStates][numberActions];

        for (int action = 0; action < numberActions; action++) {
            for (int state = 0; state < numberStates; state++) {
                T[state][action] = new StateAction(numberStates, state, action);
                Q[state][action] = 0.0;
            }
        }

        R = new double[numberStates];
        U = new double[numberStates];
        PI = new int[numberStates];

        Random random = new Random();

        for (int state = 0; state < numberStates; state++) {
            R[state] = 0.0;
            U[state] = 0.0;
            PI[state] = random.nextInt(numberActions);
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
        } else if (alg.equals(ALG_PI)) {
            algorithm = Algorithm.PI;
        }

        discount = Double.parseDouble(dis.trim());
    }

    public static void main(String[] args) {
        new Program4(args);
    }
}
