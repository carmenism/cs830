import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Predicate {
    private static HashMap<String, Predicate> predicates = new HashMap<String, Predicate>();
    
    private String name;
    private List<PredicateInstance> instances = new ArrayList<PredicateInstance>();
    
    private Predicate(String name) {
        this.name = name;
    }
    
    public static PredicateInstance getPredicateInstance(String name, List<Term> termList) {
        Predicate predicate = predicates.get(name);
        
        if (predicate == null) {
            predicate = new Predicate(name);
            predicates.put(name, predicate);
        }
        
        PredicateInstance pi = new PredicateInstance(predicate, termList);
        predicate.instances.add(pi);
        
        return pi;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
