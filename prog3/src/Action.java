import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents a grounded (fully instantiated) action. An action consists of
 * lists of positive and negative preconditions, as well as lists of deletion
 * and add effects.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Action {
    private final HashSet<Predicate> pre;
    private final HashSet<Predicate> preneg;
    private final HashSet<Predicate> del;
    private final HashSet<Predicate> add;

    private final String action;

    /**
     * Creates an Action.
     * 
     * @param ungroundedAction
     *            The UngroundedAction where this action came from.
     * @param substitutions
     *            A list of substitutions that were used to ground this action.
     * @param pre
     *            A HashSet of positive preconditions.
     * @param preneg
     *            A HashSet of negative preconditions.
     * @param del
     *            A HashSet of delete effects.
     * @param add
     *            A HashSet of addition effects.
     */
    public Action(UngroundedAction ungroundedAction,
            HashMap<Variable, Constant> substitutions, HashSet<Predicate> pre,
            HashSet<Predicate> preneg, HashSet<Predicate> del,
            HashSet<Predicate> add) {
        this.pre = pre;
        this.preneg = preneg;
        this.del = del;
        this.add = add;

        StringBuffer sb = new StringBuffer();

        sb.append(ungroundedAction.getName());

        for (Variable var : ungroundedAction.getVariables()) {
            sb.append(" ");
            sb.append(substitutions.get(var));
        }

        this.action = sb.toString();
    }

    /**
     * Determines whether or not this Action interferes with another Action by
     * either a) deleting the other's pre-conditions, b) deleting the other's
     * add effects, or c) adding the other's preneg-conditions.
     * 
     * @param other
     *            The other Action to compare with for interference.
     * @return True if this Action interferes with the other Action.
     */
    public boolean interferes(Action other) {
        for (Predicate predicate : del) {
            if (other.pre.contains(predicate)) {
                return true;
            }
        }

        for (Predicate predicate : del) {
            if (other.add.contains(predicate)) {
                return true;
            }
        }

        for (Predicate predicate : add) {
            if (other.preneg.contains(predicate)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether or not this Action is possible given a State.
     * 
     * @param state
     *            The current State of the world.
     * @return True if this Action is currently possible.
     */
    public boolean possible(State state) {
        return possible(state.getPositive(), state.getNegative());
    }

    /**
     * Determines whether or not this Action is possible given a HashSet of
     * positive Predicates and a HashSet of negative Predicates.
     * 
     * @param positive
     *            Predicates which are currently true.
     * @param negative
     *            Predicates which are currently false.
     * @return True if this Action is currently possible.
     */
    public boolean possible(HashSet<Predicate> positive,
            HashSet<Predicate> negative) {
        return positive.containsAll(pre) && negative.containsAll(preneg);
    }

    /**
     * A more elaborate version of toString which returns also the details of
     * this Action's pre, preneg, del, and add.
     * 
     * @return A verbose String describing this Action.
     */
    public String toStringDetails() {
        StringBuffer ret = new StringBuffer();

        ret.append(action);

        ret.append("\npre:");

        for (Predicate p : pre) {
            ret.append(" ");
            ret.append(p);
        }

        ret.append("\npreneg:");

        for (Predicate p : preneg) {
            ret.append(" ");
            ret.append(p);
        }

        ret.append("\ndel:");

        for (Predicate p : del) {
            ret.append(" ");
            ret.append(p);
        }

        ret.append("\nadd:");

        for (Predicate p : add) {
            ret.append(" ");
            ret.append(p);
        }

        return ret.toString();
    }

    public String getAction() {
        return action;
    }

    public HashSet<Predicate> getPre() {
        return pre;
    }

    public HashSet<Predicate> getPreneg() {
        return preneg;
    }

    public HashSet<Predicate> getDel() {
        return del;
    }

    public HashSet<Predicate> getAdd() {
        return add;
    }

    @Override
    public String toString() {
        return action;
    }
}
