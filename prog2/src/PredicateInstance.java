import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PredicateInstance {
    private final Predicate predicate;
    private final List<Term> termList;    
    
    public PredicateInstance(Predicate predicate, List<Term> termList) {        
        this.predicate = predicate;
        this.termList = termList;
    }
    
    public Predicate getPredicate() {
        return predicate;
    }

    public List<Term> getTermList() {
        return termList;
    }

    public void subVariablesForPrinting(HashMap<String, String> subs) {
        for (Term term : termList) {
            term.subVariablesForPrinting(subs);
        }
    } 
    
    @Override
    public String toString() {
        if (termList.size() == 1) {
            return predicate + "(" + termList.get(0) + ")";
        } else {
            String ret = predicate + "(";
            
            for (int i = 0; i < termList.size() - 1; i++) {
                ret = ret + termList.get(i) + ", ";
            }
            
            return ret + termList.get(termList.size() - 1) + ")";
        }
    }
    
    public PredicateInstance clone(HashMap<String, Substitution> subs) {
    	List<Term> newTermList = new ArrayList<Term>();
    	
    	for (Term term : termList) {
    		newTermList.add(term.clone(subs));
    	}
    	
    	return new PredicateInstance(this.predicate, newTermList);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((predicate == null) ? 0 : predicate.hashCode());
        result = prime * result
                + ((termList == null) ? 0 : termList.hashCode());
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
        PredicateInstance other = (PredicateInstance) obj;
        if (predicate == null) {
            if (other.predicate != null)
                return false;
        } else if (!predicate.equals(other.predicate))
            return false;
        if (termList == null) {
            if (other.termList != null)
                return false;
        } else if (!termList.equals(other.termList))
            return false;
        return true;
    }
}
