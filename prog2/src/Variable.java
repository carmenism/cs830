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
        System.out.println("RESET");
    }
    
    public static Variable getVariable(String name) {
        Variable variable = variables.get(name);
        
        if (variable == null) {
            variable = new Variable("x" + counter);
            variables.put(name, variable);
            counter++;
            System.out.println("NEW VARIABLE");
        } else {
        	System.out.println("reuse variable");
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
