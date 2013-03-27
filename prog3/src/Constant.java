/**
 * Represents a constant, that is a symbol that represents some object.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Constant extends Term {
    /**
     * Creates a Constant from a name.
     * 
     * @param name
     *            The name for this Constant.
     */
    public Constant(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        Constant other = (Constant) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
