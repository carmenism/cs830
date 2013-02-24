import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Clause {
    private final Literal literal;
    private final Clause clause;
    
    private int number = -1;
    private Clause parentA;
    private Clause parentB;
    
    private List<Literal> allLiterals = new ArrayList<Literal>();
    
    private Clause(Clause parentA, Clause parentB) {
    	this.literal = null;
    	this.clause = null;
    	
    	this.parentA = parentA;
    	this.parentB = parentB;
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
    
    public Clause(List<Literal> literals, Clause parentA, Clause parentB) {
    	this(literals);
    	
    	this.parentA = parentA;
    	this.parentB = parentB;
    }
    
    private Clause(List<Literal> literals) {
    	this.literal = literals.get(0);
    	literals.remove(0);
    	
    	if (!literals.isEmpty()) {
    		this.clause = new Clause(literals);
    	} else {
    		this.clause = null;
    	}
    }
    
    public boolean isEmpty() {
    	return literal == null && clause == null;
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
        HashMap<String, Substitution> subs = null;
        
        int thisIndex = -1;
        int otherIndex = -1;
        
        for (int i = 0; i < allLiterals.size() && thisIndex == -1; i++) {
        	Literal lit = allLiterals.get(i);

			for (int j = 0; j < other.allLiterals.size() && otherIndex == -1; j++) {
				Literal otherLit = other.allLiterals.get(j);
				System.out.println("Comparing \n\t" + lit + "\n\t" + otherLit);
				subs = lit.resolve(otherLit);

				if (subs != null) {
					System.out.println("VICTORY!!!!!!!!!!!!!!!!!!!!!!!!!!");
					thisIndex = i;
					otherIndex = j;
				}
			}
        }
        
        if (thisIndex == -1 && otherIndex == -1) {
        	return null;
        }
        
        List<Literal> sharedLiterals = new ArrayList<Literal>();
        
        Variable.resetVariables();
        
        for (int i = 0; i < allLiterals.size(); i++) {
        	if (i != thisIndex) {
        		sharedLiterals.add(allLiterals.get(i).clone(subs));
        	}
        }
        
        for (int j = 0; j < other.allLiterals.size(); j++) {
        	if (j != otherIndex) {
        		sharedLiterals.add(other.allLiterals.get(j).clone(subs));
        	}
        }
        
        Clause parentA = this;
        Clause parentB = other;
        
        //if (this.getNumber() > other.getNumber()) {
        //	parentA = other;
        //	parentB = this;
        //}
        
        if (sharedLiterals.isEmpty()) {
        	return new Clause(parentA, parentB);
        }
        
        return new Clause(sharedLiterals, parentA, parentB);
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
}