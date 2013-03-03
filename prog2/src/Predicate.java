import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a predicate in first-order logic represented in conjunctive normal
 * form. A predicate is followed by a term list. This class represents a
 * predicate that is used wherever the function name appears in the knowledge
 * base.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Predicate implements Comparable<Predicate> {
    private static HashMap<String, Predicate> predicates = new HashMap<String, Predicate>();

    private final String name;
    private List<PredicateInstance> instances = new ArrayList<PredicateInstance>();

    /**
     * Creates a Predicate with the given name.
     * 
     * @param name
     *            The name of the predicate.
     */
    private Predicate(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the predicate.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Determines whether or not this is an answer predicate.
     * 
     * @return True if the predicate name is "Ans".
     */
    public boolean isAns() {
        return name.equals("Ans");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return name;
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

    /**
     * Retrieves a PredicateInstance with the given Term list and the Predicate
     * matching the specified predicate name.
     * 
     * @param name
     *            The name of the predicate.
     * @param termList
     *            A List of Terms.
     * @return A PredicateInstance with the Term list.
     */
    public static PredicateInstance getPredicateInstance(String name,
            List<Term> termList) {
        Predicate predicate = predicates.get(name);

        if (predicate == null) {
            predicate = new Predicate(name);
            predicates.put(name, predicate);
        }

        PredicateInstance pi = new PredicateInstance(predicate, termList);
        predicate.instances.add(pi);

        return pi;
    }
}
