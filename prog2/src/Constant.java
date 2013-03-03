import java.util.HashMap;

public class Constant extends Term {
    private static HashMap<String, Constant> constants = new HashMap<String, Constant>();
    
    private Constant(String name) {    
        super(name);
    }
    
    public static Constant getConstant(String name) {
        Constant constant = constants.get(name);
        
        if (constant == null) {
            constant = new Constant(name);
            
            constants.put(name, constant);
        }
        
        return constant;
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
    
    public Constant clone(HashMap<String, Term> subs) {
    	return this;
    }
    
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        return;
    }

}
