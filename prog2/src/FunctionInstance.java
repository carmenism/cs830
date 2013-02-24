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

    @Override
    public boolean matches(Term other, HashMap<String, Substitution> subs) {
        if (subs.containsKey(other.getName())) {
            other = subs.get(other.getName()).getSubstitute();
        }
        
        if (other instanceof FunctionInstance) {
            FunctionInstance otherFunction = (FunctionInstance) other;
            
            System.out.println("COMPARING TWO FUNCTIONS");
            
            if (!otherFunction.function.equals(function)) {
                System.out.println("function names are different");
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
        
        for (Term term : termList) {
            if (term instanceof Variable) {
                Variable termVariable = (Variable) term;
                
                if (otherVariable.equals(termVariable)) {
                    return false;
                }
            }
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
}
