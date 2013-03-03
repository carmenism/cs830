import java.util.HashMap;

/**
 * Represents a variable in first-order logic represented in conjunctive normal
 * form.  A variable is a type of term where the name is lower-cased.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Variable extends Term {
    private static HashMap<String, Variable> variables;
    private static int counter = 0;

    private String substitution;

    /**
     * Creates a Variable based on the specified name.
     * 
     * @param name
     *            The name of the Variable.
     */
    private Variable(String name) {
        super(name);
    }

    @Override
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        String sub = subs.get(this.getName());

        if (sub == null) {
            sub = "v" + subs.size();

            subs.put(this.getName(), sub);
        }

        this.substitution = sub;
    }

    @Override
    public boolean matches(Term other, HashMap<String, Term> subs) {
        if (subs.containsKey(this.getName())) {
            Term sub = subs.get(this.getName());

            return sub.matches(other, subs);
        }

        if (subs.containsKey(other.getName())) {
            other = subs.get(other.getName());
        }

        if (other instanceof FunctionInstance) {
            FunctionInstance fi = (FunctionInstance) other;

            if (fi.containsVariable(this, subs)) {
                return false;
            }
        }

        subs.put(this.getName(), other);

        return true;
    }

    @Override
    public Term clone(HashMap<String, Term> subs) {
        if (subs.containsKey(this.getName())) {
            Term term = subs.get(this.getName());

            if (term instanceof Variable) {
                Variable var = (Variable) term;

                return new Variable(getVariable(var.getName()).getName());
            }

            return term.clone(subs);
        }

        return new Variable(getVariable(this.getName()).getName());
    }

    @Override
    public String toString() {
        if (substitution != null) {
            return substitution;
        }

        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Variable other = (Variable) obj;
        if (substitution == null) {
            if (other.substitution != null)
                return false;
        } else if (!substitution.equals(other.substitution))
            return false;
        return true;
    }

    /**
     * Resets the Variables so that each Clause has separate, non-conflicting
     * Variable names.
     */
    public static void resetVariables() {
        variables = new HashMap<String, Variable>();
    }

    /**
     * Retrieves the Variable that should be used for this Clause.
     * 
     * @param name
     *            The name of the variable as it appears in the original clause.
     * @return A Variable object to make this Variable unique in the whole
     *         knowledge base.
     */
    public static Variable getVariable(String name) {
        Variable variable = variables.get(name);

        if (variable == null) {
            variable = new Variable("x" + counter);
            variables.put(name, variable);
            counter++;
        }

        return variable;
    }
}
