import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public class KnowledgeBase {
	private final List<Clause> input;
	private final List<Clause> setOfSupport;
    private List<Clause> solution;
	
	public KnowledgeBase(List<Clause> input, List<Clause> setOfSupport) {
		this.input = input;
		this.setOfSupport = setOfSupport;
				
		for (int i = 0; i < input.size(); i++) {
		    input.get(i).setNumber(i + 1);
		}
		
		for (int i = 0; i < setOfSupport.size(); i++) {
		    setOfSupport.get(i).setNumber(i + 1 + input.size());
        }
	}
	
	public void print() {
		for (Clause clause : input) {
			System.out.println(clause);
		}
		
		System.out.println("--- negated query ---");
		
        for (Clause clause : setOfSupport) {
            System.out.println(clause);
        }
	}
	
	public void resolve() {
	    PriorityQueue<Clause> sos = new PriorityQueue<Clause>(setOfSupport);
	    List<Clause> sosGrave = new ArrayList<Clause>();
	    
	    while (!sos.isEmpty()) {
	        Clause smallest = sos.poll();
	        //System.out.println("polled " + smallest);
	        	        
	        for (Clause other : sos) {
	            Clause resolved = smallest.resolve(other);
	            
	            if (resolved != null) {
	                if (resolved.isEmpty()) {
	                    System.out.println("SUCCESS!");
                        printSolution(resolved);
                        return; 
	                } else {
	                    sos.add(resolved);
	                }
	            }
	        }
	        
	        for (Clause other : sosGrave) {
                Clause resolved = smallest.resolve(other);
                
                if (resolved != null) {
                    if (resolved.isEmpty()) {
                        System.out.println("SUCCESS!");
                        printSolution(resolved);
                        return; 
                    } else {
                        sos.add(resolved);
                    }
                }
            }
	        
	        for (Clause other : input) {
	            //System.out.println("\tcompare " + other);
                Clause resolved = smallest.resolve(other);
                
                if (resolved != null) {
                    if (resolved.isEmpty()) {
                        System.out.println("SUCCESS!");
                        printSolution(resolved);
                        return; 
                    } else {
                        sos.add(resolved);
                    }
                }
	        }
	        
	        sosGrave.add(smallest);
	    }
	    

        System.out.println("empty :(");
	}
	
	private void printSolution(Clause emptyClause) {
	    buildSolution(emptyClause);
	    
	    int num = input.size() + setOfSupport.size() + 1;
	    
	    for (int i = solution.size() - 1; i >= 0; i--) {
	        solution.get(i).setNumber(num);
	        num++;
	    }
	    
	    for (int i = solution.size() - 1; i >= 0; i--) {
	        Clause c = solution.get(i);
	        int a = c.getParentA().getNumber();
	        int b = c.getParentB().getNumber();
	        System.out.println(a + " " + b + " give " + );
        }
	}
	
	private void buildSolution(Clause clause) {
	    solution.add(clause);
	    
	    if (clause.getParentA() != null) {
	        buildSolution(clause.getParentA());
	    }
	    
	    if (clause.getParentB() != null) {
	        buildSolution(clause.getParentB());
        }
	}
}
