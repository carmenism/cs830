import java.util.List;

public class PredicateUsage {
    private final Predicate predicate;
    private final List<Constant> constants;
    
    public PredicateUsage(Predicate predicate, List<Constant> constants) {
        this.predicate = predicate;
        this.constants = constants;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public List<Constant> getConstants() {
        return constants;
    }
    
    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        
        ret.append(predicate);
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
        result = prime * result
                + ((predicate == null) ? 0 : predicate.hashCode());
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
        PredicateUsage other = (PredicateUsage) obj;
        if (constants == null) {
            if (other.constants != null)
                return false;
        } else if (!constants.equals(other.constants))
            return false;
        if (predicate == null) {
            if (other.predicate != null)
                return false;
        } else if (!predicate.equals(other.predicate))
            return false;
        return true;
    }
}
