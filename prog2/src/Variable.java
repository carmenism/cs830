import java.util.HashMap;

public class Variable extends Term {
    private static HashMap<String, Variable> variables;
    private static int counter = 0;
    
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
            variable = new Variable("x" + counter);
            variables.put(name, variable);
            counter++;
        }
        
        return variable;
    }
    
    
    
    public boolean matches(Term other, HashMap<String, Substitution> subs) {
        if (subs.containsKey(this.getName())) {
            Term sub = subs.get(this.getName()).getSubstitute();
            
            return sub.matches(other, subs);
        }
        
        if (subs.containsKey(other.getName())) {
            other = subs.get(other.getName()).getSubstitute();
        }
        
        if (other instanceof FunctionInstance) {
        	FunctionInstance fi = (FunctionInstance) other;
        	
        	if (fi.containsVariable(this)) {
            	return false;
            }
        }
        
        Substitution newSub = new Substitution(other, this);
        subs.put(this.getName(), newSub);
        
        return true;
    }
    
    public Term clone(HashMap<String, Substitution> subs) {
    	if (subs.containsKey(this.getName())) {
    		Term term = subs.get(this.getName()).getSubstitute();
    		
    		if (term instanceof Variable) {
    			Variable var = (Variable) term;
    			
    			return getVariable(var.getName());
    		}
    		
    		return term.clone(subs);
    	}
    	
    	return getVariable(this.getName());//new Variable(name);
    }
}
