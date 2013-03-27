import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a possible state of the STRIPS planning world. A state consists of
 * predicates which are true and predicates which are false.
 * 
 * @author Carmen St. Jean
 * 
 */
public class State {
    private final HashSet<Predicate> positive;
    private final HashSet<Predicate> negative;
    private final List<SolutionStep> stepsTaken;
    private final int time;
    private boolean isDummyAction = false;
    private int treeDepth = -1;
    private List<Action> parallelActions;

    /**
     * Creates a State.
     * 
     * @param stepsTaken
     *            The steps taken to reach this state.
     * @param positive
     *            The predicates which are true.
     * @param negative
     *            The predicates which are false.
     * @param time
     *            The time of this state.
     */
    public State(List<SolutionStep> stepsTaken, HashSet<Predicate> positive,
            HashSet<Predicate> negative, int time) {
        this.stepsTaken = stepsTaken;
        this.positive = positive;
        this.negative = negative;
        this.time = time;
    }

    /**
     * 
     * @param stepsTaken
     *            The steps taken to reach this state.
     * @param positive
     *            The predicates which are true.
     * @param negative
     *            The predicates which are false.
     * @param time
     *            The time of this state.
     * @param treeDepth
     *            The tree depth for this state's tree of operator
     *            decomposition.
     * @param parallelActions
     *            The set of all possible actions to be tried in parallel.
     */
    public State(List<SolutionStep> stepsTaken, HashSet<Predicate> positive,
            HashSet<Predicate> negative, int time, int treeDepth,
            List<Action> parallelActions) {
        this.stepsTaken = stepsTaken;
        this.positive = positive;
        this.negative = negative;
        this.time = time;
        this.parallelActions = parallelActions;
        this.treeDepth = treeDepth;
    }

    /**
     * Gets all actions possible from this state.
     * 
     * @return A HashSet of all possible Actions.
     */
    public HashSet<Action> getPossibleActions() {
        HashSet<Action> actions = new HashSet<Action>();

        for (Action action : Program3.groundedActions) {
            if (action.possible(this)) {
                actions.add(action);
            }
        }

        return actions;
    }

    /**
     * Determines whether or not this state is the goal state.
     * 
     * @return True if the goal has been reached.
     */
    public boolean isGoal() {
        return positive.containsAll(Program3.goal)
                && negative.containsAll(Program3.goalNeg);
    }

    /**
     * Gets the list of new positive Predicates for this state after the Action
     * taken.
     * 
     * @param action
     *            The new Action to be taken.
     * @return The new HashSet of positive Predicates.
     */
    private HashSet<Predicate> getEffectedPositive(Action action) {
        HashSet<Predicate> newPos = new HashSet<Predicate>(positive);

        newPos.removeAll(action.getDel());
        newPos.addAll(action.getAdd());

        return newPos;
    }

    /**
     * Gets the list of new negative Predicates for this state after the Action
     * taken.
     * 
     * @param action
     *            The new Action to be taken.
     * @return The new HashSet of negative Predicates.
     */
    private HashSet<Predicate> getEffectedNegative(Action action) {
        HashSet<Predicate> newNeg = new HashSet<Predicate>(negative);

        newNeg.addAll(action.getDel());
        newNeg.removeAll(action.getAdd());

        return newNeg;
    }

    /**
     * Makes a brand new list of steps taken by appending to the list of steps
     * taken for this state.
     * 
     * @param action
     *            The new action.
     * @param t
     *            The time for the new action.
     * @return A list of steps taken for the new state.
     */
    private List<SolutionStep> getNewStepsTaken(Action action, int t) {
        List<SolutionStep> newStepsTaken = new ArrayList<SolutionStep>();

        newStepsTaken.addAll(stepsTaken);
        newStepsTaken.add(new SolutionStep(t, action));

        return newStepsTaken;
    }

    /**
     * Expands the state.
     * 
     * @return A list of possible states.
     */
    public List<State> expand() {
        List<State> states = new ArrayList<State>();

        if (!Program3.isParallel) {
            for (Action possibleAction : getPossibleActions()) {
                List<SolutionStep> newStepsTaken = getNewStepsTaken(
                        possibleAction, time);
                HashSet<Predicate> newPos = getEffectedPositive(possibleAction);
                HashSet<Predicate> newNeg = getEffectedNegative(possibleAction);

                states.add(new State(newStepsTaken, newPos, newNeg, time + 1));
            }
        } else {
            states = expandParallel();
        }

        return states;
    }

    /**
     * Expands the state for parallel problem-solving domains.
     * 
     * @return A list of possible states.
     */
    private List<State> expandParallel() {
        List<State> states = new ArrayList<State>();

        if (treeDepth == 0) {
            // Root node for operator decomposition.
            parallelActions = new ArrayList<Action>(getPossibleActions());
        }

        if (treeDepth == parallelActions.size()) {
            // Leaf node - advance time and start operator decomposition all
            // over again.
            State at = new State(stepsTaken, positive, negative, time + 1);
            at.setTreeDepth(0);

            states.add(at);
        } else {
            // Internal node.
            Action possibleAction = parallelActions.get(treeDepth);

            List<SolutionStep> newStepsTaken = getNewStepsTaken(possibleAction,
                    time);
            HashSet<Predicate> newPos = getEffectedPositive(possibleAction);
            HashSet<Predicate> newNeg = getEffectedNegative(possibleAction);

            State doA = new State(newStepsTaken, newPos, newNeg, time,
                    treeDepth + 1, parallelActions);

            // Only add the "do action" state if it does not conflict with
            // actions made in the same time-step.
            if (!doA.isConflicting()) {
                states.add(doA);
            }

            // Create a dummy state from not doing the action.
            State notA = new State(stepsTaken, positive, negative, time,
                    treeDepth + 1, parallelActions);

            // We do not want to create a state from the
            // "do absolutely no actions in the decomposed tree" path.
            if (!notA.isLastAlongDummyPath()) {
                notA.setDummyAction(true);
                states.add(notA);
            }
        }

        return states;
    }

    /**
     * Determines whether or not this state conflicts with any of the other
     * states have happened in the same time-step - i.e., this action taken to
     * reach this state interferes with another action made in the same
     * time-step.
     * 
     * @return True if this state conflicts with another state the same branch
     *         of the tree resulting from operator decomposition.
     */
    public boolean isConflicting() {
        if (isDummyAction || stepsTaken.size() < 2) {
            return false;
        }

        Action last = stepsTaken.get(stepsTaken.size() - 1).getAction();

        for (int i = stepsTaken.size() - 2; i >= 0; i--) {
            if (stepsTaken.get(i).getTime() != time) {
                break;
            }

            if (stepsTaken.get(i).getAction().interferes(last)) {
                return true;
            }

            if (last.interferes(stepsTaken.get(i).getAction())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether or not this state is the last along a completely dummy
     * path. If so, this state was the result of no actions whatsoever since
     * operator decomposition started.
     * 
     * @return True if this action is the last along a dummy path.
     */
    public boolean isLastAlongDummyPath() {
        if (treeDepth < parallelActions.size()) {
            return false;
        }

        int numRealActions = 0;

        for (int i = stepsTaken.size() - 1; i >= 0; i--) {
            if (stepsTaken.get(i).getTime() != time) {
                break;
            }

            numRealActions++;
        }

        return numRealActions == 0;
    }

    public boolean isDummyAction() {
        return isDummyAction;
    }

    public void setDummyAction(boolean isDummyAction) {
        this.isDummyAction = isDummyAction;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public List<Action> getPossibleParallelActions() {
        return parallelActions;
    }

    public void setPossibleParallelActions(List<Action> parallelActions) {
        this.parallelActions = parallelActions;
    }

    public int getTime() {
        return time;
    }

    public List<SolutionStep> getStepsTaken() {
        return stepsTaken;
    }

    public HashSet<Predicate> getPositive() {
        return positive;
    }

    public HashSet<Predicate> getNegative() {
        return negative;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isDummyAction ? 1231 : 1237);
        result = prime * result
                + ((negative == null) ? 0 : negative.hashCode());
        result = prime * result
                + ((positive == null) ? 0 : positive.hashCode());

        if (Program3.isParallel) {
            result = prime * result + time;
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (isDummyAction != other.isDummyAction)
            return false;
        if (negative == null) {
            if (other.negative != null)
                return false;
        } else if (!negative.equals(other.negative))
            return false;
        if (positive == null) {
            if (other.positive != null)
                return false;
        } else if (!positive.equals(other.positive))
            return false;
        if (Program3.isParallel && time != other.time)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Positive: " + positive + "\nNegative: " + negative;
    }
}
