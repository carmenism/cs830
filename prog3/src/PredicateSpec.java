import java.util.List;

public class PredicateSpec {
    private final Predicate predicate;
    private final List<Variable> terms;
    
    public PredicateSpec(Predicate predicate, List<Variable> terms) {
        this.predicate = predicate;
        this.terms = terms;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public List<Variable> getTerms() {
        return terms;
    }
}
