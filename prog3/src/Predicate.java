import java.util.HashSet;
import java.util.List;

public class Predicate {
    private final String name;
    private final List<Constant> constants;
    
    public Predicate(String name, List<Constant> constants) {
        this.name = name;
        this.constants = constants;
    }

    public String getName() {
        return name;
    }

    public List<Constant> getConstants() {
        return constants;
    }
    
    public HashSet<Action> actionsWherePrecondition(HashSet<Action> actions) {
        HashSet<Action> subset = new HashSet<Action>();
        
        for (Action action : actions) {
            if (action.getPre().contains(this)) {
                subset.add(action);
            }
        }
        
        return subset;
    }
    
    public HashSet<Action> actionsWherePrenegcondition(HashSet<Action> actions) {
        HashSet<Action> subset = new HashSet<Action>();
        
        for (Action action : actions) {
            if (action.getPreneg().contains(this)) {
                subset.add(action);
            }
        }
        
        return subset;
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
