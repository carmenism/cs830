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
}
