import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

/**
 * Represents a clause in first-order logic in conjunctive normal form.
 * 
 * @author Carmen St. Jean
 * 
 */
public class Clause implements Comparable<Clause> {
    private final UUID id = UUID.randomUUID();

    private final Literal literal;
    private final Clause clause;

    private int number = -1;
    private Clause parentA;
    private Clause parentB;

    private List<Literal> allLiterals = new ArrayList<Literal>();

    /**
     * Creates a Clause from a Literal and another Clause.
     * 
     * @param literal
     *            The Literal.
     * @param clause
     *            The Clause.
     */
    public Clause(Literal literal, Clause clause) {
        this.literal = literal;
        this.clause = clause;

        addAllLiterals(this);
    }

    /**
     * Creates a Clause from a single Literal.
     * 
     * @param literal
     *            The Literal.
     */
    public Clause(Literal literal) {
        this.literal = literal;
        this.clause = null;

        allLiterals.add(literal);
    }

    /**
     * Create a Clause from a PriorityQueue of Literals with parent pointers.
     * 
     * @param literals
     *            A PriorityQueue of Literals that this Clause should consist
     *            of.
     * @param parentA
     *            The first parent that this Clause was derived from.
     * @param parentB
     *            The second parent that this Clause was derived from.
     */
    public Clause(PriorityQueue<Literal> literals, Clause parentA,
            Clause parentB) {
        this(literals);

        this.parentA = parentA;
        this.parentB = parentB;

        addAllLiterals(this);
    }

    /**
     * Create an empty Clause.
     * 
     * @param parentA
     *            The first parent that this Clause was derived from.
     * @param parentB
     *            The second parent that this Clause was derived from.
     */
    private Clause(Clause parentA, Clause parentB) {
        this.literal = null;
        this.clause = null;

        this.parentA = parentA;
        this.parentB = parentB;
    }

    /**
     * Create a Clause from a PriorityQueue of Literals.
     * 
     * @param literals
     *            A PriorityQueue of Literals that this Clause should consist
     *            of.
     */
    private Clause(PriorityQueue<Literal> literals) {
        this.literal = literals.poll();

        if (!literals.isEmpty()) {
            this.clause = new Clause(literals);
        } else {
            this.clause = null;
        }
    }

    /**
     * Get a parent which this Clause was derived from.
     * 
     * @return The primary parent of this Clause.
     */
    public Clause getParentA() {
        return parentA;
    }

    /**
     * Get a parent which this Clause was derived from.
     * 
     * @return The secondary parent of this Clause.
     */
    public Clause getParentB() {
        return parentB;
    }

    /**
     * Get the number for this Clause.
     * 
     * @return The number for this Clause.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Set the number for this Clause.
     * 
     * @param number
     *            The desired number for this Clause.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Get the ID for this Clause.
     * 
     * @return The UUID for this Clause.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Determines whether or not this Clause is the empty Clause - i.e., logical
     * bottom.
     * 
     * @return True if this Clause is an empty Clause.
     */
    public boolean isEmpty() {
        return literal == null && clause == null;
    }

    /**
     * Determines whether or not this Clause contains a single Literal which is
     * the anwers Literal.
     * 
     * @return True if there is only one Literal in this clause and it is "Ans".
     */
    public boolean isAns() {
        if (clause != null) {
            return false;
        }

        return literal.isAns();
    }

    /**
     * Determines whether or not this Clause contains an answer Literal.
     * 
     * @return True if one of the Literals is "Ans".
     */
    public boolean containsAns() {
        for (Literal lit : allLiterals) {
            if (lit.isAns()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds all Literals found in the specified Clause to this Clause's list of
     * Literals.
     * 
     * @param c
     */
    private void addAllLiterals(Clause c) {
        allLiterals.add(c.literal);

        if (c.clause != null) {
            addAllLiterals(c.clause);
        }
    }

    /**
     * Attempts to resolve this Clause with another Clause.
     * 
     * @param other
     *            The other Clause to attempt resolution with.
     * @return A new Clause if resolution worked, null if resolution failed.
     */
    public Clause resolve(Clause other) {
        HashMap<String, Term> allSubs = new HashMap<String, Term>();
        List<Integer> thisIndices = new ArrayList<Integer>();
        List<Integer> otherIndices = new ArrayList<Integer>();

        for (int i = 0; i < allLiterals.size(); i++) {
            if (!thisIndices.contains(i)) {
                Literal lit = allLiterals.get(i);

                for (int j = 0; j < other.allLiterals.size(); j++) {
                    if (!otherIndices.contains(j)) {
                        Literal otherLit = other.allLiterals.get(j);
                        HashMap<String, Term> subs = lit.resolve(otherLit,
                                allSubs);

                        if (subs != null) {
                            thisIndices.add(i);
                            otherIndices.add(j);
                            allSubs = subs;
                        }
                    }
                }
            }
        }

        if (thisIndices.isEmpty() && otherIndices.isEmpty()) {
            return null;
        }

        PriorityQueue<Literal> sharedLiterals = new PriorityQueue<Literal>();
        HashMap<String, String> printSubs = new HashMap<String, String>();

        Variable.resetVariables();

        for (int i = 0; i < allLiterals.size(); i++) {
            if (!thisIndices.contains(i)) {
                Literal clone = allLiterals.get(i).clone(allSubs);
                clone.subVariablesForPrinting(printSubs);
                sharedLiterals.add(clone);
            }
        }

        for (int j = 0; j < other.allLiterals.size(); j++) {
            if (!otherIndices.contains(j)) {
                Literal clone = other.allLiterals.get(j).clone(allSubs);
                clone.subVariablesForPrinting(printSubs);
                sharedLiterals.add(clone);
            }
        }

        if (sharedLiterals.isEmpty()) {
            return new Clause(this, other);
        }

        Clause clause = new Clause(sharedLiterals, this, other);
        clause.subAllVariablesForPrinting();

        return removeDuplicatesClauses(clause);
    }

    /**
     * Removes duplicate Literals found in this clause.
     * 
     * @param c
     *            The Clause to look for duplicate Literals in.
     * @return The Clause without duplicate Literals.
     */
    private Clause removeDuplicatesClauses(Clause c) {
        List<Integer> dupIndices = new ArrayList<Integer>();

        for (int i = 0; i < c.allLiterals.size(); i++) {
            for (int j = i + 1; j < c.allLiterals.size(); j++) {
                Literal litA = c.allLiterals.get(i);
                Literal litB = c.allLiterals.get(j);

                if (litA.equals(litB)) {
                    dupIndices.add(i);
                }
            }
        }

        if (dupIndices.isEmpty()) {
            return c;
        }

        PriorityQueue<Literal> lits = new PriorityQueue<Literal>();

        for (int i = 0; i < c.allLiterals.size(); i++) {
            if (!dupIndices.contains(i)) {
                lits.add(c.allLiterals.get(i));
            }
        }

        Clause newC = new Clause(lits, c.parentA, c.parentB);
        newC.subAllVariablesForPrinting();

        return newC;
    }

    /**
     * Prints all literals of this Clause to standard output.
     */
    public void printAllLiterals() {
        System.out.println("Literals of: " + this);

        for (Literal lit : allLiterals) {
            System.out.println("\t" + lit);
        }
    }

    /**
     * Creates substitutions for Variables found within this clause so the
     * printing is more standardized.
     */
    public void subAllVariablesForPrinting() {
        subVariablesForPrinting(new HashMap<String, String>());
    }

    /**
     * Creates substitutes for the Variables found within this Clause so the
     * printing is more standardized.
     * 
     * @param subs
     *            The substitutions for Variables for printing purposes.
     */
    private void subVariablesForPrinting(HashMap<String, String> subs) {
        literal.subVariablesForPrinting(subs);

        if (clause != null) {
            clause.subVariablesForPrinting(subs);
        }
    }

    /**
     * Makes a copy of this Clause, taking the specified substitutions into
     * account.
     * 
     * @param subs
     *            The substitutions for Variables determined during unification.
     * @return A new copy of this Clause.
     */
    public Clause clone(HashMap<String, Term> subs) {
        if (clause == null) {
            return new Clause(literal.clone(subs));
        }

        return new Clause(literal.clone(subs), clause.clone(subs));
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "<empty>";
        } else if (clause == null) {
            return literal.toString();
        } else {
            return literal + " | " + clause;
        }
    }

    @Override
    public int compareTo(Clause other) {
        return allLiterals.size() - other.allLiterals.size();
    }

    /**
     * Takes a clause and produces a duplicate clause where the literals are
     * sorted alphabetically.
     * 
     * @param c
     *            The Clause that needs sorting.
     * @return An alphabetically sorted version of the original Clause.
     */
    public static Clause getSortedClause(Clause c) {
        PriorityQueue<Literal> lits = new PriorityQueue<Literal>(c.allLiterals);
        Clause newClause = new Clause(lits, null, null);

        return newClause;
    }
}