import java.util.List;

public class PredicateSpec {
    private final Predicate predicate;
    private final List<Variable> variables;   
    
    public PredicateSpec(Predicate predicate, List<Variable> variables) {
        this.predicate = predicate;
        this.variables = variables;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public List<Variable> getVariables() {
        return variables;
    }
    
    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        
        ret.append(predicate);
        ret.append("(");
        
        for (Variable variable : variables) {
            ret.append(variable);
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
                + ((predicate == null) ? 0 : predicate.hashCode());
        result = prime * result
                + ((variables == null) ? 0 : variables.hashCode());
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
        PredicateSpec other = (PredicateSpec) obj;
        if (predicate == null) {
            if (other.predicate != null)
                return false;
        } else if (!predicate.equals(other.predicate))
            return false;
        if (variables == null) {
            if (other.variables != null)
                return false;
        } else if (!variables.equals(other.variables))
            return false;
        return true;
    }
}
