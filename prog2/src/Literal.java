import java.util.List;


public class Literal extends Node {
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
    
    public boolean cancelsOut(Literal other) {
        if (!getPredicate().equals(other.getPredicate()))
            return false;
        
        if (isPositive() == other.isPositive()) {
            return false;
        }
        
        List<Term> termList = predicateInstance.getTermList();
        List<Term> otherTermList = other.getPredicateInstance().getTermList();
        
        if (termList.size() != otherTermList.size()) {
            return false;
        }
        
        for (int i = 0; i < termList.size(); i++) {
            Term term = termList.get(i);
            Term otherTerm = otherTermList.get(i);
            
            /////////////////////////////////////////////////////////////
        }
        
        return true;
    }

    @Override
    public String toString() {
        if (positive) {
            return predicateInstance.toString();
        } else {
            return "-" + predicateInstance.toString();
        }
    }
}
