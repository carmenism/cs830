import java.util.List;

public class PredicateSpec {
    private final String name;
    private final List<Variable> variables;
    
    public PredicateSpec(String name, List<Variable> variables) {
        this.name = name;
        this.variables = variables;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return variables;
    }
    
    @Override
    public String toString() {
        String ret = name + "(";
        
        for (Variable var : variables) {
            ret = ret + var + ", ";
        }
        
        ret = ret.substring(0, ret.length() - 2) + ")";
        
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (variables == null) {
            if (other.variables != null)
                return false;
        } else if (!variables.equals(other.variables))
            return false;
        return true;
    }
}
