import java.util.HashSet;
import java.util.List;

/**
 * Represents a grounded (fully instantiated) predicate, which consists of a
 * name and a list of constants.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Predicate {
    private final String name;
    private final List<Constant> constants;

    /**
     * Creates a Predicate from a name String and a list of Constants.
     * 
     * @param name
     *            The name for the Predicate.
     * @param constants
     */
    public Predicate(String name, List<Constant> constants) {
        this.name = name;
        this.constants = constants;
    }

    /**
     * Gets a list of Actions where this Predicate is a positive precondition.
     * 
     * @param actions
     *            The HashSet of all grounded Actions.
     * @return A HashSet where this Predicate is a pre-condition.
     */
    public HashSet<Action> actionsWherePrecondition(HashSet<Action> actions) {
        HashSet<Action> subset = new HashSet<Action>();

        for (Action action : actions) {
            if (action.getPre().contains(this)) {
                subset.add(action);
            }
        }

        return subset;
    }

    /**
     * Gets a list of Actions where this Predicate is a negative precondition.
     * 
     * @param actions
     *            The HashSet of all grounded Actions.
     * @return A HashSet where this Predicate is a preneg-condition.
     */
    public HashSet<Action> actionsWherePrenegcondition(HashSet<Action> actions) {
        HashSet<Action> subset = new HashSet<Action>();

        for (Action action : actions) {
            if (action.getPreneg().contains(this)) {
                subset.add(action);
            }
        }

        return subset;
    }

    public String getName() {
        return name;
    }

    public List<Constant> getConstants() {
        return constants;
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();

        ret.append(name);
        ret.append("(");

        for (Constant constant : constants) {
            ret.append(constant);
            ret.append(", ");
        }

        ret.deleteCharAt(ret.length() - 1);
        ret.deleteCharAt(ret.length() - 1);
        ret.append(")");

        return ret.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((constants == null) ? 0 : constants.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Predicate other = (Predicate) obj;
        if (constants == null) {
            if (other.constants != null)
                return false;
        } else if (!constants.equals(other.constants))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
