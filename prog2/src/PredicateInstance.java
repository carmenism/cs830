import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the usage of a predicate in first-order logic represented in
 * conjunctive normal form. A predicate is followed by a term list. This class
 * represents individual usages of a predicate.
 * 
 * @author Carmen St. Jean
 * 
 */
public class PredicateInstance {
    private final Predicate predicate;
    private final List<Term> termList;

    /**
     * Creates a PredicateInstace from an associated Predicate and list of
     * Terms.
     * 
     * @param predicate
     *            The Predicate for this PredicateInstance.
     * @param termList
     *            The list of Terms.
     */
    public PredicateInstance(Predicate predicate, List<Term> termList) {
        this.predicate = predicate;
        this.termList = termList;
    }

    /**
     * Gets the associated Predicate.
     * 
     * @return The Predicate for this PredicateInstance.
     */
    public Predicate getPredicate() {
        return predicate;
    }

    /**
     * Gets the List of Terms for this PredicateInstance.
     * 
     * @return The List of Terms.
     */
    public List<Term> getTermList() {
        return termList;
    }

    /**
     * Creates substitutes for the Variables found within this PredicateInstance
     * so the printing is more standardized.
     * 
     * @param subs
     *            The substitutions for Variables for printing purposes.
     */
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        for (Term term : termList) {
            term.subVariablesForPrinting(subs);
        }
    }

    /**
     * Makes a copy of this PredicateInstance, taking the specified
     * substitutions into account.
     * 
     * @param subs
     *            The substitutions for Variables determined during unification.
     * @return A new copy of this PredicateInstance.
     */
    public PredicateInstance clone(HashMap<String, Term> subs) {
        List<Term> newTermList = new ArrayList<Term>();

        for (Term term : termList) {
            newTermList.add(term.clone(subs));
        }

        return new PredicateInstance(this.predicate, newTermList);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((predicate == null) ? 0 : predicate.hashCode());
        result = prime * result
                + ((termList == null) ? 0 : termList.hashCode());
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
        PredicateInstance other = (PredicateInstance) obj;
        if (predicate == null) {
            if (other.predicate != null)
                return false;
        } else if (!predicate.equals(other.predicate))
            return false;
        if (termList == null) {
            if (other.termList != null)
                return false;
        } else if (!termList.equals(other.termList))
            return false;
        return true;
    }

    @Override
    public String toString() {
        if (termList.size() == 1) {
            return predicate + "(" + termList.get(0) + ")";
        } else {
            String ret = predicate + "(";

            for (int i = 0; i < termList.size() - 1; i++) {
                ret = ret + termList.get(i) + ", ";
            }

            return ret + termList.get(termList.size() - 1) + ")";
        }
    }
}
