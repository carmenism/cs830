import java.util.ArrayList;
import java.util.List;


public class Clause extends Node {
    private Literal literal;
    private Clause clause;
    
    private List<Literal> allLiterals = new ArrayList<Literal>();
    
    public Clause(Literal literal, Clause clause) {
        this(literal);
        this.clause = clause;

        addChild(clause);
        
        addAllLiterals(this);        
    }
    
    public Clause(Literal literal) {
        this.literal = literal;
        
        addChild(literal);
    }
    
    public Clause resolve(Clause other) {
        List<Literal> sharedLiterals = new ArrayList<Literal>();
        
        for (Literal lit : allLiterals) {
            boolean canceledOut = false;
            
            for (Literal otherLit : other.allLiterals) {                
                if (lit.cancelsOut(otherLit)) {
                    canceledOut = true;
                    break;
                }
            }
            
            if (!canceledOut) {
                sharedLiterals.add(lit);
            }
        }
        
        for (Literal otherLit : other.allLiterals) {
            boolean canceledOut = false;
            
            for (Literal lit : allLiterals) {
                if (lit.cancelsOut(otherLit)) {
                    canceledOut = true;
                    break;
                }
            }
            
            if (!canceledOut) {
                sharedLiterals.add(otherLit);
            }
        }
        
        return null;
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
        if (clause == null) {
            return literal.toString();
        } else {
            return literal + " | " + clause;
        }
    }
}
