import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class KnowledgeBase {
	private final List<Clause> input;
	private final List<Clause> setOfSupport;
    
	private boolean lookForEmpty;
    
	private List<Clause> solution;
    
    private int numberResolved = 0;
    
	public KnowledgeBase(List<Clause> input, List<Clause> setOfSupport) {
		this.input = input;
		this.setOfSupport = setOfSupport;
		this.solution = new ArrayList<Clause>();
				
		for (int i = 0; i < input.size(); i++) {
		    input.get(i).setNumber(i + 1);
		}
		
		this.lookForEmpty = true;
		
		for (int i = 0; i < setOfSupport.size(); i++) {
		    Clause clause = setOfSupport.get(i);		    
		    clause.setNumber(i + 1 + input.size());
		    
		    if (clause.containsAns()) {
		    	this.lookForEmpty = false;
		    }
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
	
	public boolean isGoal(Clause clause) {
		if (lookForEmpty) {
			return clause.isEmpty();
		}
		
		return clause.isAns();
	}
	
	public boolean resolve() {	    
		PriorityQueue<Clause> sos = new PriorityQueue<Clause>(setOfSupport);
		List<Clause> sosGrave = new ArrayList<Clause>();
		HashSet<Pair> pairs = new HashSet<Pair>();
		HashSet<String> clausesProduced = new HashSet<String>();
	    
	    for (Clause i : input) {
	        clausesProduced.add(i.toString());
	    }
	    for (Clause s : setOfSupport) {
            clausesProduced.add(s.toString());
        }
	    
	    while (!sos.isEmpty()) {	    	
	        Clause smallest = sos.poll();

	        PriorityQueue<Clause> others = getOtherClauses(sos, sosGrave);
	        
	        while (!others.isEmpty()) {
		        Clause smallestOther = others.poll();
		        Pair pair = new Pair(smallest, smallestOther);
		        
		        if (!pairs.contains(pair)) {
			        Clause resolved = smallest.resolve(smallestOther);
			        
			        if (resolved != null) {
	                    numberResolved++;
	                    
		                if (isGoal(resolved)) {
		                    printSolution(resolved);
		                    
		                    return true; 
		                } else if (!clausesProduced.contains(resolved.toString())) {
		                    clausesProduced.add(resolved.toString());
		                	sos.add(resolved);
		                }
		            }
			        
			        pairs.add(pair);
		        }
	        }
	        
	        sosGrave.add(smallest);
	    }
	    
	    printInput();
	    printSetOfSupport();
        System.out.println("No proof exists.");
        printNumberResolutions();
	    
        return false;
	}
	
	private PriorityQueue<Clause> getOtherClauses(PriorityQueue<Clause> sos, List<Clause> sosGrave) {
		PriorityQueue<Clause> others = new PriorityQueue<Clause>();
		
        others.addAll(sos);
        others.addAll(sosGrave);
        others.addAll(input);
        
        return others;
	}
	
	/*private boolean resolve(Collection<Clause> clauses, Clause clause) {
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
	}*/
	
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
