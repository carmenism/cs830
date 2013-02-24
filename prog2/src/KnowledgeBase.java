import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {
	private final List<Clause> initialClauses;
	private List<Clause> setOfSupport;
	//private int num;
	
	public KnowledgeBase(List<Clause> clauses, Clause negatedQuery) {
		this.initialClauses = clauses;
		
		for (int i = 0; i < clauses.size(); i++) {
			clauses.get(i).setNumber(i + 1);
		}
		
		//this.num = clauses.size() + 1;
		
		negatedQuery.setNumber(clauses.size() + 1);
		
		setOfSupport = new ArrayList<Clause>();
		setOfSupport.add(negatedQuery);
	}
	
	public void resolve() {
		int position = 0;
		
		while (true) {
			if (position >= setOfSupport.size()) {
				System.err.println("ERROR?!");
				System.exit(1);
			}
			
			Clause latest = setOfSupport.get(position);
			System.out.println(position + " GOING TO COMPARE " + latest);
			
			for (int i = position; i < setOfSupport.size(); i++) {
				System.out.println("COMPARING AGAINST SET OF SUPPORT: "  + setOfSupport.get(i));
				Clause clause = setOfSupport.get(i);
				Clause resolved = latest.resolve(clause);
				
				if (resolved != null) {
					if (resolved.isEmpty()) {
						System.out.println("SUCCESS!");
						return;
					} else {
						setOfSupport.add(resolved);
					}
				}
			}
			
			for (int i = 0; i < initialClauses.size(); i++) {
				System.out.println("COMPARING AGAINST KB");
				Clause clause = initialClauses.get(i);
				Clause resolved = latest.resolve(clause);
				
				if (resolved != null) {
					if (resolved.isEmpty()) {
						System.out.println("SUCCESS!");
						return;
					} else {
						setOfSupport.add(resolved);
					}
				}
			}
			
			position++;
			
			System.out.println("System of support");
			for (Clause clause : setOfSupport) {
				System.out.println("\t" + clause);
			}
		}
	}
}
