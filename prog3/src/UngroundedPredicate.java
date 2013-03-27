import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Represents an ungrounded (uninstantiated) predicate. This includes a name and
 * a list of terms (variables and possibly constants).
 * 
 * @author Carmen St. Jean
 * 
 */
public class UngroundedPredicate {
    private final String name;
    private final List<Term> terms;

    /**
     * Creates an UngroundedPredicate from a name and list of terms.
     * 
     * @param name
     *            The name of the UngroundedPredicate.
     * @param terms
     *            The terms of the UngroundedPredicate - Variables and possibly
     *            Constants - listed in order.
     */
    public UngroundedPredicate(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    /**
     * Grounds (instantiates) the predicate according to the list of
     * substitutions for each variable of the term list.
     * 
     * @param substitutions
     *            The mappings from Variable to Constant.
     * @return A fully instantiated Predicate according to the given
     *         substitutions.
     * @throws SubstitutionMissingException
     *             If there is a substitution that was not specified properly.
     */
    public Predicate ground(HashMap<Variable, Constant> substitutions)
            throws SubstitutionMissingException {
        List<Constant> constants = new ArrayList<Constant>();

        for (Term term : terms) {
            Constant con = null;

            if (term instanceof Variable) {
                Variable var = (Variable) term;
                con = substitutions.get(var);

                if (con == null) {
                    System.out.println(substitutions.keySet());
                    System.out.println(substitutions.values());

                    throw new SubstitutionMissingException();
                }
            } else {
                con = (Constant) term;
            }

            constants.add(con);
        }

        Predicate predicate = new Predicate(name, constants);

        if (Program3.groundedPredicates.containsKey(predicate)) {
            predicate = Program3.groundedPredicates.get(predicate);
        } else {
            Program3.groundedPredicates.put(predicate, predicate);
        }

        return predicate;
    }

    public String getName() {
        return name;
    }

    public List<Term> getVariables() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();

        ret.append(name);
        ret.append("(");

        for (Term term : terms) {
            ret.append(term);
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((terms == null) ? 0 : terms.hashCode());
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
        UngroundedPredicate other = (UngroundedPredicate) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (terms == null) {
            if (other.terms != null)
                return false;
        } else if (!terms.equals(other.terms))
            return false;
        return true;
    }

    /**
     * Grounds the list of ungrounded predicates according to the substitutions.
     * 
     * @param ugPreds
     *            The UngroundedPredicates to be grounded.
     * @param substitutions
     *            Mappings from Variable to Constant for substitution.
     * @return A list of fully instantiated Predicates according to the given
     *         substitutions.
     * @throws SubstitutionMissingException
     *             If there is a substitution that was not specified properly.
     */
    public static HashSet<Predicate> ground(List<UngroundedPredicate> ugPreds,
            HashMap<Variable, Constant> substitutions)
            throws SubstitutionMissingException {
        HashSet<Predicate> preds = new HashSet<Predicate>();

        for (UngroundedPredicate ugPred : ugPreds) {
            preds.add(ugPred.ground(substitutions));
        }

        return preds;
    }
}
