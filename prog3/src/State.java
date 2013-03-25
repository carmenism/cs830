import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {
    private final HashSet<Predicate> positive;
    private final HashSet<Predicate> negative;
    private final List<SolutionStep> stepsTaken;
    private final int time;

    public State(List<SolutionStep> stepsTaken, HashSet<Predicate> positive,
            HashSet<Predicate> negative, int time) {
        this.stepsTaken = stepsTaken;
        this.positive = positive;
        this.negative = negative;
        this.time = time;
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
        for (Predicate goal : Program3.goal) {
            if (!positive.contains(goal)) {
                return false;
            }
        }

        for (Predicate goalNeg : Program3.goalNeg) {
            if (!negative.contains(goalNeg)) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        return "Positive: " + positive + "\nNegative: " + negative;
    }

    public List<State> expand() {
        List<State> states = new ArrayList<State>();

        for (Action possibleAction : getPossibleActions()) {
            List<SolutionStep> newStepsTaken = new ArrayList<SolutionStep>();
            newStepsTaken.addAll(stepsTaken);
            newStepsTaken.add(new SolutionStep(time, possibleAction));

            HashSet<Predicate> newPos = new HashSet<Predicate>(positive);
            HashSet<Predicate> newNeg = new HashSet<Predicate>(negative);

            for (Predicate del : possibleAction.getDel()) {
                newPos.remove(del);
                newNeg.add(del);
            }

            for (Predicate add : possibleAction.getAdd()) {
                newNeg.remove(add);
                newPos.add(add);
            }

            if (!Program3.isParallel) {
                states.add(new State(newStepsTaken, newPos, newNeg, time + 1));
            } else {
                states.add(new State(newStepsTaken, newPos, newNeg, time));
            }
        }

        return states;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
