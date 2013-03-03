import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

public class Clause implements Comparable<Clause> {
    private final UUID id = UUID.randomUUID();

    private final Literal literal;
    private final Clause clause;

    private int number = -1;
    private Clause parentA;
    private Clause parentB;

    private List<Literal> allLiterals = new ArrayList<Literal>();

    public Clause(Literal literal, Clause clause) {
        this.literal = literal;
        this.clause = clause;

        addAllLiterals(this);
    }

    public Clause(Literal literal) {
        this.literal = literal;
        this.clause = null;

        allLiterals.add(literal);
    }

    public Clause(PriorityQueue<Literal> literals, Clause parentA,
            Clause parentB) {
        this(literals);

        this.parentA = parentA;
        this.parentB = parentB;

        addAllLiterals(this);
    }

    private Clause(Clause parentA, Clause parentB) {
        this.literal = null;
        this.clause = null;

        this.parentA = parentA;
        this.parentB = parentB;
    }

    private Clause(PriorityQueue<Literal> literals) {
        this.literal = literals.poll();

        if (!literals.isEmpty()) {
            this.clause = new Clause(literals);
        } else {
            this.clause = null;
        }
    }

    public boolean isEmpty() {
        return literal == null && clause == null;
    }

    public boolean isAns() {
        if (clause != null) {
            return false;
        }

        return literal.isAns();
    }

    public boolean containsAns() {
        for (Literal lit : allLiterals) {
            if (lit.isAns()) {
                return true;
            }
        }

        return false;
    }

    public Clause getParentA() {
        return parentA;
    }

    public Clause getParentB() {
        return parentB;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    private void addAllLiterals(Clause c) {
        allLiterals.add(c.literal);

        if (c.clause != null) {
            addAllLiterals(c.clause);
        }
    }

    public UUID getId() {
        return id;
    }
    
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
    			        HashMap<String, Term> subs = lit.resolve(otherLit, allSubs);
        
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
    
    public void printAllLiterals() {
        System.out.println("Literals of: " + this);

        for (Literal lit : allLiterals) {
            System.out.println("\t" + lit);
        }
    }

    public void subAllVariablesForPrinting() {
        subVariablesForPrinting(new HashMap<String, String>());
    }
    
    private void subVariablesForPrinting(HashMap<String, String> subs) {
        literal.subVariablesForPrinting(subs);

        if (clause != null) {
            clause.subVariablesForPrinting(subs);
        }
    }

    /**
     * Clones the given Clause, taking any of the given substitutions into account as well.
     * 
     * @param subs A HashMap of 
     * @return
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