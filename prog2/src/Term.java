import java.util.HashMap;

/**
 * Represents the abstract concept of a term in first-order logic as it is
 * represented in conjunctive normal form. A term is either a variable,
 * constant, or function.
 * 
 * @author Carmen St. Jean
 * 
 */
public abstract class Term {
    protected String name;

    /**
     * Creates a Term with the given name.
     * 
     * @param name
     *            The name for the Term as a String.
     */
    public Term(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this Term.
     * 
     * @return The name of this Term as a String.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Compare this Term with another Term to see if they match when making
     * substitutions for Variables according to the rules of unification.
     * 
     * @param other
     *            The other Term to compare to this Term.
     * @param subs
     *            The substitutions for Variables determined during unification.
     * @return True if the Terms match after substitution and unification.
     */
    public abstract boolean matches(Term other, HashMap<String, Term> subs);
    
    /**
     * Makes a copy of this Term, taking the specified substitutions into
     * account.
     * 
     * @param subs
     *            The substitutions for Variables determined during unification.
     * @return A new copy of this Term.
     */
    public abstract Term clone(HashMap<String, Term> subs);
    
    /**
     * Creates substitutes for the Variables so the printing is more
     * standardized.
     * 
     * @param subs
     *            The substitutions for Variables for printing purposes.
     */
    public abstract void subVariablesForPrinting(HashMap<String, String> subs);
    
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
        Term other = (Term) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}