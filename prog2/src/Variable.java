import java.util.HashMap;

public class Variable extends Term {
    private static HashMap<String, Variable> variables;
    private static int counter = 0;
    
    private String substitution;
    
    private Variable(String name) {
        super(name);
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

    public void subVariablesForPrinting(HashMap<String, String> subs) {
        String sub = subs.get(this.getName());
        
        if (sub == null) {
            sub = "v" + subs.size();
            
            subs.put(this.getName(), sub);
        }
        
        this.substitution = sub;
    } 
    
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
        
        //Substitution newSub = new Substitution(other, this);
        subs.put(this.getName(), other);
        
        return true;
    }
    
    public Term clone(HashMap<String, Term> subs) {
    	if (subs.containsKey(this.getName())) {
    		Term term = subs.get(this.getName());
    		
    		if (term instanceof Variable) {
    			Variable var = (Variable) term;
    			
    			return new Variable(getVariable(var.getName()).getName());
    		}
    		
    		return term.clone(subs);
    	}
    	
    	return new Variable(getVariable(this.getName()).getName());//new Variable(name);
    }
    
    @Override
    public String toString() {
        if (substitution != null) {
            return substitution;
        }
        
        return name;
    }
}
