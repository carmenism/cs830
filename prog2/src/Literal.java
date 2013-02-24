import java.util.HashMap;
import java.util.List;


public class Literal {
    private final PredicateInstance predicateInstance;
    private final boolean positive;
    
    public Literal(PredicateInstance predicateInstance, boolean positive) {
        this.predicateInstance = predicateInstance;
        this.positive = positive;
    }
          
    public PredicateInstance getPredicateInstance() {
        return predicateInstance;
    }

    public Predicate getPredicate() {
        return predicateInstance.getPredicate();
    }
    
    public boolean isPositive() {
        return positive;
    }
    
    public HashMap<String, Substitution> resolve(Literal other) {
        if (!getPredicate().equals(other.getPredicate())) {
            //System.out.println("predicates are different");
            return null;
        }
    
        if (isPositive() == other.isPositive()) {
            //System.out.println("same signage");
            return null;
        }
        
        List<Term> termList = predicateInstance.getTermList();
        List<Term> otherTermList = other.getPredicateInstance().getTermList();
        
        if (termList.size() != otherTermList.size()) {
            //System.out.println("different number of terms");
            return null;
        }
        
        HashMap<String, Substitution> subs = new HashMap<String, Substitution>();
        
        for (int i = 0; i < termList.size(); i++) {
            Term term = termList.get(i);
            Term otherTerm = otherTermList.get(i);
            
            if (!term.matches(otherTerm, subs)) {
                //System.out.println("terms didn't match: " + term +" and " + otherTerm);
                return null;
            }
        }
        
        return subs;
    }

    @Override
    public String toString() {
        if (positive) {
            return predicateInstance.toString();
        } else {
            return "-" + predicateInstance.toString();
        }
    }
    
    public Literal clone(HashMap<String, Substitution> subs) {
    	return new Literal(predicateInstance.clone(subs), positive);
    }
}
