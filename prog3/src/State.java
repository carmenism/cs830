import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {
    private final HashSet<Predicate> positive;
    private final HashSet<Predicate> negative;
    private final List<SolutionStep> stepsTaken;
    private final int time;
    private boolean isDummyAction = false;
    private int treeDepth = -1;
    private List<Action> parallelActions;

    public State(List<SolutionStep> stepsTaken, HashSet<Predicate> positive,
            HashSet<Predicate> negative, int time) {
        this.stepsTaken = stepsTaken;
        this.positive = positive;
        this.negative = negative;
        this.time = time;
    }

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

    public HashSet<Action> getPossibleActions() {
        HashSet<Action> actions = new HashSet<Action>();

        for (Action action : Program3.groundedActions) {
            if (action.possible(this)) {
                actions.add(action);
            }
        }

        return actions;
    }

    public Action getLastAction() {
        if (stepsTaken.size() < 1) {
            return null;
        }

        return stepsTaken.get(stepsTaken.size() - 1).getAction();
    }

    public boolean isGoal() {
        /*
         * for (Predicate goal : Program3.goal) { if (!positive.contains(goal))
         * { return false; } }
         * 
         * for (Predicate goalNeg : Program3.goalNeg) { if
         * (!negative.contains(goalNeg)) { return false; } }
         */

        return positive.containsAll(Program3.goal)
                && negative.containsAll(Program3.goalNeg);
    }

    public String toString() {
        return "Positive: " + positive + "\nNegative: " + negative;
    }

    public HashSet<Predicate> getEffectedPositive(Action action) {
        HashSet<Predicate> newPos = new HashSet<Predicate>(positive);

        newPos.removeAll(action.getDel());
        newPos.addAll(action.getAdd());

        return newPos;
    }

    public HashSet<Predicate> getEffectedNegative(Action action) {
        HashSet<Predicate> newNeg = new HashSet<Predicate>(negative);

        newNeg.addAll(action.getDel());
        newNeg.removeAll(action.getAdd());

        return newNeg;
    }

    public List<SolutionStep> getNewStepsTaken(Action action, int t) {
        List<SolutionStep> newStepsTaken = new ArrayList<SolutionStep>();

        newStepsTaken.addAll(stepsTaken);
        newStepsTaken.add(new SolutionStep(t, action));

        return newStepsTaken;
    }

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

    private List<State> expandParallel() {
        List<State> states = new ArrayList<State>();

        if (treeDepth == 0) {
            parallelActions = new ArrayList<Action>(getPossibleActions());
        }

        if (treeDepth == parallelActions.size()) {
            State at = new State(stepsTaken, positive, negative, time + 1);
            at.setTreeDepth(0);

            states.add(at);
        } else {
            Action possibleAction = parallelActions.get(treeDepth);

            List<SolutionStep> newStepsTaken = getNewStepsTaken(possibleAction,
                    time);
            HashSet<Predicate> newPos = getEffectedPositive(possibleAction);
            HashSet<Predicate> newNeg = getEffectedNegative(possibleAction);

            State doA = new State(newStepsTaken, newPos, newNeg, time,
                    treeDepth + 1, parallelActions);

            if (!doA.isConflicting()) {
                states.add(doA);
            }

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
}
