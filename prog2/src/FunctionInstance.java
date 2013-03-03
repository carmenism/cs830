import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FunctionInstance extends Term {
    private final Function function;
    private final List<Term> termList;
    
    public FunctionInstance(Function function, List<Term> termList) {
        super(function.getName());
        
        this.function = function;
        this.termList = termList;
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
    
    public List<Term> getTermList() {
		return termList;
	}
    
    public void subVariablesForPrinting(HashMap<String, String> subs) {
        for (Term term : termList) {
            term.subVariablesForPrinting(subs);
        }
    } 
    
    public boolean containsVariable(Variable var, HashMap<String, Substitution> subs) {
    	for (Term term : termList) {
    	    if (subs.containsKey(term.getName())) {
                term = subs.get(term.getName()).getSubstitute();
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
    public boolean matches(Term other, HashMap<String, Substitution> subs) {
        if (subs.containsKey(other.getName())) {
            other = subs.get(other.getName()).getSubstitute();
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
                if (!termList.get(i).matches(otherFunction.termList.get(i), subs)) {
                    return false;
                }
            }
            
            return true;
        } else if (other instanceof Constant) {
            return false;
        }
        
        // then other is a variable
        Variable otherVariable = (Variable) other;

        System.out.println("check to see if this is contained");
        if (containsVariable(otherVariable, subs)) {
        	return false;
        }
        
        Substitution newSub = new Substitution(this, otherVariable);
        subs.put(otherVariable.getName(), newSub);
        
        return true;
    }
    
    public FunctionInstance clone(HashMap<String, Substitution> subs) {
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
}
