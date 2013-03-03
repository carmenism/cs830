import java.util.HashMap;
import java.util.List;

/**
 * Represents a literal in first-order logic represented using conjunctive
 * normal form syntax. A literal is either a predicate or a minus sign ("-")
 * followed by a predicate. The minus sign symbolizes logical "not".
 * 
 * @author Carmen St. Jean
 * 
 */
public class Literal implements Comparable<Literal> {
    private final int BEFORE = -1;
    private final int AFTER = 1;

    private final PredicateInstance predicateInstance;
    private final boolean positive;

    /**
     * Creates a Literal from a PredicateInstance and a boolean indicating the
     * signage.
     * 
     * @param predicateInstance
     *            The PredicateInstance for this literal.
     * @param positive
     *            False if the literal starts with a "not" (a minus sign).
     */
    public Literal(PredicateInstance predicateInstance, boolean positive) {
        this.predicateInstance = predicateInstance;
        this.positive = positive;
    }

    /**
     * Gets the PredicateInstance of this Literal.
     * 
     * @return The PredicateInstance for this Literal.
     */
    public PredicateInstance getPredicateInstance() {
        return predicateInstance;
    }

    /**
     * Gets the Predicate associated with this Literal.
     * 
     * @return The Predicate for this Literal.
     */
    public Predicate getPredicate() {
        return predicateInstance.getPredicate();
    }

    /**
     * Determines whether or not the signage of this Literal is positive.
     * 
     * @return True if this Literal does not start with the "not" sign.
     */
    public boolean isPositive() {
        return positive;
    }

    /**
     * Determines whether or not this Literal is an answer Predicate.
     * 
     * @return True if the associated Predicate is "Ans".
     */
    public boolean isAns() {
        return getPredicate().isAns();
    }

    /**
     * Attempts to resolves this Literal with the other Literal specified and
     * the previously found substitutions. If resolution fails, then null is
     * returned. Otherwise it returns the substitutions, consisting of all
     * previously found substitutions and all newly found substitutions.
     * 
     * @param other
     *            The other Clause to attempt resolution with.
     * @param allSubs
     *            The substitutions for Variables found thus far.
     * @return Null if unification fails. All substitutions found so far if
     *         unification works.
     */
    public HashMap<String, Term> resolve(Literal other,
            HashMap<String, Term> allSubs) {
        if (!getPredicate().equals(other.getPredicate())) {
            return null;
        }

        if (isPositive() == other.isPositive()) {
            return null;
        }

        List<Term> termList = predicateInstance.getTermList();
        List<Term> otherTermList = other.getPredicateInstance().getTermList();

        if (termList.size() != otherTermList.size()) {
            return null;
        }

        HashMap<String, Term> subs = new HashMap<String, Term>();
        subs.putAll(allSubs);

        for (int i = 0; i < termList.size(); i++) {
            Term term = termList.get(i);
            Term otherTerm = otherTermList.get(i);

            if (!term.matches(otherTerm, subs)) {
                return null;
            }
        }

        return subs;
    }

    /**
     * Creates substitutes for the Variables found within this Literal so the
     * printing is more standardized.
     * 
     * @param subs
     *            The substitutions for Variables for printing purposes.
     */
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        predicateInstance.subVariablesForPrinting(subs);
    }

    /**
     * Makes a copy of this Literal, taking the specified substitutions into
     * account.
     * 
     * @param subs
     *            The substitutions for Variables determined during unification.
     * @return A new copy of this Literal.
     */
    public Literal clone(HashMap<String, Term> subs) {
        return new Literal(predicateInstance.clone(subs), positive);
    }

    @Override
    public String toString() {
        if (positive) {
            return predicateInstance.toString();
        } else {
            return "-" + predicateInstance.toString();
        }
    }

    @Override
    public int compareTo(Literal other) {
        Predicate p = this.getPredicate();
        Predicate otherP = other.getPredicate();

        if (p.getName().equals(otherP.getName())) {
            if (!positive && other.positive) {
                return BEFORE;
            }

            if (positive && !other.positive) {
                return AFTER;
            }

            return this.toString().compareTo(other.toString());
        }

        return p.compareTo(otherP);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (positive ? 1231 : 1237);
        result = prime
                * result
                + ((predicateInstance == null) ? 0 : predicateInstance
                        .hashCode());
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
        Literal other = (Literal) obj;
        if (positive != other.positive)
            return false;
        if (predicateInstance == null) {
            if (other.predicateInstance != null)
                return false;
        } else if (!predicateInstance.equals(other.predicateInstance))
            return false;
        return true;
    }
}
