import java.util.List;

public class PredicateUsage {
    private final Predicate predicate;
    private final List<Constant> terms;   
    
    public PredicateUsage(Predicate predicate, List<Constant> terms) {
        this.predicate = predicate;
        this.terms = terms;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public List<Constant> getTerms() {
        return terms;
    }
}
