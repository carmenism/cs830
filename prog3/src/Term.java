/**
 * Represents a term, which is the shared behavior between constant and
 * variable.
 * 
 * @author Carmen St. Jean
 * 
 */
public abstract class Term {
    protected final String name;

    public Term(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
