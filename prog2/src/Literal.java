import java.util.HashMap;
import java.util.List;


public class Literal implements Comparable<Literal> {
    private final int BEFORE = -1;
    private final int AFTER = 1;
    
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
    
    public boolean isAns() {
    	return getPredicate().isAns();
    }
    
    public void subVariablesForPrinting(HashMap<String, String> subs) {        
        predicateInstance.subVariablesForPrinting(subs);
    }    
    
    public HashMap<String, Term> resolve(Literal other, HashMap<String, Term> allSubs) {
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
        
        HashMap<String, Term> subs = new HashMap<String, Term>();
        subs.putAll(allSubs);
        
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
    
    public Literal clone(HashMap<String, Term> subs) {
    	return new Literal(predicateInstance.clone(subs), positive);
    }

    @Override
    public int compareTo(Literal other) {
        Predicate p = this.getPredicate();
        Predicate otherP = other.getPredicate();
        
        if (p.getName().equals(otherP.getName())) {
            if (!positive && other.positive) {
                return BEFORE;
            }
            
            if (positive && !other.positive) {
                return AFTER;
            }
            
            return this.toString().compareTo(other.toString());
        }
        
        return p.compareTo(otherP);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (positive ? 1231 : 1237);
        result = prime
                * result
                + ((predicateInstance == null) ? 0 : predicateInstance
                        .hashCode());
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
        Literal other = (Literal) obj;
        if (positive != other.positive)
            return false;
        if (predicateInstance == null) {
            if (other.predicateInstance != null)
                return false;
        } else if (!predicateInstance.equals(other.predicateInstance))
            return false;
        return true;
    }
}
