import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a function in first-order logic represented in conjunctive normal
 * form. A function is a type of term where the name is upper-cased and it is
 * followed by a term list. This class represents a function that is used
 * wherever the function name appears in the knowledge base.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Function {
    private static HashMap<String, Function> functions = new HashMap<String, Function>();
    
    private List<FunctionInstance> instances = new ArrayList<FunctionInstance>();
    private final String name; 

    /**
     * Creates a Function with the given name.
     * 
     * @param name
     *            The name for the Function.
     */
    private Function(String name) {
        this.name = name;
    }
    
    /**
     * Gets the name of the Function.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
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
        Function other = (Function) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * Retrieves a FunctionInstance of the Function that matches the specified
     * function name. Attaches the given List of Terms to the FunctionInstance.
     * 
     * @param name
     *            The name of the function.
     * @param termList
     *            The List of Terms for the FunctionInstance.
     * @return A FunctionInstance associated with the List of Terms and the
     *         correct Function.
     */
    public static FunctionInstance getFunctionInstance(String name,
            List<Term> termList) {
        Function function = functions.get(name);

        if (function == null) {
            function = new Function(name);
            functions.put(name, function);
        }

        FunctionInstance fi = new FunctionInstance(function, termList);
        function.instances.add(fi);

        return fi;
    }
}
