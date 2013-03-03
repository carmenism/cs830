import java.util.HashMap;

/**
 * Represents a constant in first-order logic represented in conjunctive normal
 * form. A constant is a type of term where the name is upper-cased. A constant
 * can also be regarded as a Function with no parameters.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Constant extends Term {
    private static HashMap<String, Constant> constants = new HashMap<String, Constant>();

    /**
     * Creates a Constant from the specified name.
     * 
     * @param name
     *            The name for the constant.
     */
    private Constant(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean matches(Term other, HashMap<String, Term> subs) {
        if (subs.containsKey(other.getName())) {
            other = subs.get(other.getName());
        }

        if (other instanceof Constant) {
            Constant otherConstant = (Constant) other;

            return otherConstant.equals(this);
        } else if (other instanceof FunctionInstance) {
            return false;
        }

        // Then other is a Variable.
        subs.put(other.getName(), this);

        return true;
    }

    @Override
    public Constant clone(HashMap<String, Term> subs) {
        return this;
    }

    @Override
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        return;
    }

    /**
     * Retrieves the Constant object matching the specified Constant name.
     * 
     * @param name
     *            The name of the constant in the original clause from the
     *            input.
     * @return A Constant object.
     */
    public static Constant getConstant(String name) {
        Constant constant = constants.get(name);

        if (constant == null) {
            constant = new Constant(name);

            constants.put(name, constant);
        }

        return constant;
    }
}
