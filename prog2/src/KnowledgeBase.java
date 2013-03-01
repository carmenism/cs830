import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public class KnowledgeBase {
	private final List<Clause> input;
	private final List<Clause> setOfSupport;
	
    private List<Clause> solution;
    private PriorityQueue<Clause> sos;
    private List<Clause> sosGrave;
    
    private int numberResolved = 0;
    
	public KnowledgeBase(List<Clause> input, List<Clause> setOfSupport) {
		this.input = input;
		this.setOfSupport = setOfSupport;
		this.solution = new ArrayList<Clause>();
				
		for (int i = 0; i < input.size(); i++) {
		    input.get(i).setNumber(i + 1);
		}
		
		for (int i = 0; i < setOfSupport.size(); i++) {
		    setOfSupport.get(i).setNumber(i + 1 + input.size());
        }
	}
	
	private void printInput() {
		for (Clause clause : input) {
			System.out.println(clause.getNumber() + ": " + clause);
		}
	}
	
	private void printSetOfSupport() {
		for (Clause clause : setOfSupport) {
			System.out.println(clause.getNumber() + ": " + clause);
		}
	}
	
	private void printNumberResolutions() {
		System.out.println(numberResolved + " total resolutions");
	}
	
	public boolean resolve() {
	    sos = new PriorityQueue<Clause>(setOfSupport);
	    sosGrave = new ArrayList<Clause>();
	    
	    while (!sos.isEmpty()) {
	        Clause smallest = sos.poll();

	        if (resolve(sos, smallest)) {
	        	return true;
	        }
	        
	        if (resolve(sosGrave, smallest)) {
	        	return true;
	        }
	                
	        if (resolve(input, smallest)) {
	        	return true;
	        }
	        	        
	        sosGrave.add(smallest);
	    }
	    
        System.out.println("empty :(");
        return false;
	}
	
	private boolean resolve(Collection<Clause> clauses, Clause clause) {
		for (Clause other : clauses) {
            Clause resolved = clause.resolve(other);
            
            if (resolved != null) {
                if (resolved.isEmpty()) {
                    printSolution(resolved);
                    
                    return true; 
                } else {
                	System.out.println(resolved);
                    sos.add(resolved);
                    numberResolved++;
                }
            }
        }
		
		return false;
	}
	
	private void printSolution(Clause emptyClause) {
	    buildSolution(emptyClause);
	    
	    int num = input.size() + setOfSupport.size() + 1;
	    
	    for (int i = solution.size() - 1; i >= 0; i--) {
	        solution.get(i).setNumber(num);
	        num++;
	    }
	    
	    printInput();
	    printSetOfSupport();
	    
	    for (int i = solution.size() - 1; i >= 0; i--) {
	    	Clause clause = solution.get(i);	
	    	int c = clause.getNumber();
	        int a = clause.getParentA().getNumber();
	        int b = clause.getParentB().getNumber();
	        
	        System.out.println(a + " and " + b + " give " + c + ": " + clause);
        }
	    
	    printNumberResolutions();
	}
	
	private void buildSolution(Clause clause) {
	    if (clause.getParentA() != null && clause.getParentB() != null) {
	    	//System.out.println("Adding " + clause);
	    	solution.add(clause);
	        buildSolution(clause.getParentA());
	        buildSolution(clause.getParentB());
        }
	}
}
