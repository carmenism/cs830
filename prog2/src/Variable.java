import java.util.HashMap;

public class Variable extends Term {
    private static HashMap<String, Variable> variables;
    
    private Variable(String name) {
        super(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public static void resetVariables() {
        variables = new HashMap<String, Variable>();
    }
    
    public static Variable getVariable(String name) {
        Variable variable = variables.get(name);
        
        if (variable == null) {
            variable = new Variable(name);
            
            variables.put(name, variable);
        }
        
        return variable;
    }
}
