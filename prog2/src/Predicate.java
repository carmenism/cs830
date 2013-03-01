import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Predicate implements Comparable<Predicate> {
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
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Predicate other = (Predicate) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int compareTo(Predicate other) {
        return name.compareTo(other.name);
    }  
}
