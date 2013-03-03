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
    
    
    private void subVariablesForPrinting(HashMap<String, String> subs) {
        literal.subVariablesForPrinting(subs);
        
        if (clause != null) {
            clause.subVariablesForPrinting(subs);
        }
    }
        
    
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
    
    public Clause(PriorityQueue<Literal> literals, Clause parentA, Clause parentB) {
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

	public Clause resolve(Clause other) {
        HashMap<String, Substitution> allSubs = new HashMap<String, Substitution>();
        List<Integer> thisIndices = new ArrayList<Integer>();
        List<Integer> otherIndices = new ArrayList<Integer>();
        
        for (int i = 0; i < allLiterals.size(); i++) {
            if (!thisIndices.contains(i)) {
            	Literal lit = allLiterals.get(i);
    
    			for (int j = 0; j < other.allLiterals.size(); j++) {
    				if (!otherIndices.contains(j)) {
    			        Literal otherLit = other.allLiterals.get(j);
    			        HashMap<String, Substitution> subs = lit.resolve(otherLit, allSubs);
        
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
        
        for (int i = 0; i < clause.allLiterals.size(); i++) {
            for (int j = i + 1; j < clause.allLiterals.size(); j++) {
                Literal litA = clause.allLiterals.get(i);
                Literal litB = clause.allLiterals.get(j);
                
                if (litA.equals(litB)) {
                    return null;
                }
                
                if (litA.isPositive() == litB.isPositive() && litA.getPredicate().equals(litB.getPredicate())) {
                    //System.out.println(clause);
                    //System.out.println("\t"+this);
                    //System.out.println("\t"+other);
                    //System.exit(1);
                }
            }
        }
        
        return clause;
    }
    	
    private void addAllLiterals(Clause c) {
        allLiterals.add(c.literal);
        
        if (c.clause != null) {
            addAllLiterals(c.clause);
        }
    }
    
    public void printAllLiterals() {
        System.out.println("Literals of: " + this);
        
        for (Literal lit : allLiterals) {
            System.out.println("\t" + lit);
        }
    }
    
    public UUID getId() {
		return id;
	}

	@Override
    public String toString() {        
    	if (isEmpty()) {
    		return "<empty>";
    	}
    	else if (clause == null) {
            return literal.toString();
        } else {
            return literal + " | " + clause;
        }
    }
    
    public Clause clone(HashMap<String, Substitution> subs) {    	
    	if (clause == null) {
    		return new Clause(literal.clone(subs));
    	}
    	
    	return new Clause(literal.clone(subs), clause.clone(subs));
    }

    public static Clause getSortedClause(Clause c) {
        PriorityQueue<Literal> lits = new PriorityQueue<Literal>(c.allLiterals);
        Clause newClause = new Clause(lits, null, null);

        return newClause;
    }

    @Override
    public int compareTo(Clause other) {
        return allLiterals.size() - other.allLiterals.size();
    }
    
    public void subAllVariablesForPrinting() {
        subVariablesForPrinting(new HashMap<String, String>());
    }
}