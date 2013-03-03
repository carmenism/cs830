import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the usage of a function in first-order logic represented in
 * conjunctive normal form. A function is a type of term where the name is
 * upper-cased and it is followed by a term list. This class represents
 * individual usages of a function.
 * 
 * @author Carmen St. Jean
 * 
 */
public class FunctionInstance extends Term {
    private final Function function;
    private final List<Term> termList;

    /**
     * Creates a FunctionInstance from a Function and a list of Terms.
     * 
     * @param function
     *            The Function that this FunctionInstance is associated with.
     * @param termList
     *            The list of Terms for this FunctionInstance.
     */
    public FunctionInstance(Function function, List<Term> termList) {
        super(function.getName());

        this.function = function;
        this.termList = termList;
    }

    /**
     * Get the list of Terms for this FunctionInstance.
     * 
     * @return The List of Terms.
     */
    public List<Term> getTermList() {
        return termList;
    }

    /**
     * Determines whether or not the specified Variable appears anywhere in this
     * FunctionInstance's Term list.
     * 
     * @param var
     *            The Variable to check for in the Term list.
     * @param subs
     *            The substitutions made for Variables thus far.
     * @return True if the Variable appears in this FunctionInstance.
     */
    public boolean containsVariable(Variable var, HashMap<String, Term> subs) {
        for (Term term : termList) {
            if (subs.containsKey(term.getName())) {
                term = subs.get(term.getName());
            }

            if (term instanceof Variable) {
                Variable v = (Variable) term;

                if (v.equals(var)) {
                    return true;
                }
            } else if (term instanceof FunctionInstance) {
                FunctionInstance fi = (FunctionInstance) term;

                if (fi.containsVariable(var, subs)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        for (Term term : termList) {
            term.subVariablesForPrinting(subs);
        }
    }

    @Override
    public boolean matches(Term other, HashMap<String, Term> subs) {
        if (subs.containsKey(other.getName())) {
            other = subs.get(other.getName());
        }

        if (other instanceof FunctionInstance) {
            FunctionInstance otherFunction = (FunctionInstance) other;

            if (!otherFunction.function.equals(function)) {
                return false;
            }

            if (termList.size() != otherFunction.termList.size()) {
                return false;
            }

            for (int i = 0; i < termList.size(); i++) {
                Term term = termList.get(i);
                Term otherTerm = otherFunction.termList.get(i);

                if (!term.matches(otherTerm, subs)) {
                    return false;
                }
            }

            return true;
        } else if (other instanceof Constant) {
            return false;
        }

        // Then other is a variable.
        Variable otherVariable = (Variable) other;

        if (containsVariable(otherVariable, subs)) {
            return false;
        }

        subs.put(otherVariable.getName(), this);

        return true;
    }

    @Override
    public FunctionInstance clone(HashMap<String, Term> subs) {
        List<Term> newTermList = new ArrayList<Term>();

        for (Term term : termList) {
            newTermList.add(term.clone(subs));
        }

        return new FunctionInstance(function, newTermList);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((function == null) ? 0 : function.hashCode());
        result = prime * result
                + ((termList == null) ? 0 : termList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        FunctionInstance other = (FunctionInstance) obj;
        if (function == null) {
            if (other.function != null)
                return false;
        } else if (!function.equals(other.function))
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
            return function + "(" + termList.get(0) + ")";
        } else {
            String ret = function + "(";

            for (int i = 0; i < termList.size() - 1; i++) {
                ret = ret + termList.get(i) + ", ";
            }

            return ret + termList.get(termList.size() - 1) + ")";
        }
    }
}
