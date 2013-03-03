import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static void main(String [] args) {
		resolveTest();
	}
	
    public static void resolveTest() {        
        Clause clauseA = parseClauseHelper("-Coffee(FRENCHROAST) | Good(FRENCHROAST) | -Makes(STARBUCKS, FRENCHROAST)");
        Clause clauseB = parseClauseHelper("Coffee(v1) | -CoffeeMaker(v0) | -Makes(v0, v1)");
        
        if (clauseA != null && clauseB != null)  {
        	System.out.println("1: " + clauseA);
        	System.out.println("2: " + clauseB);
        	
        	Clause resolve = clauseA.resolve(clauseB);
        	
        	if (resolve != null) {
            	System.out.println("1 + 2 give 3: " + resolve);
        		System.out.println("1 total resolutions");
        	} else {
        		System.out.println("No proof exists.");
        		System.out.println("0 total resolutions");
        	}
        }
    }
    
    public static KnowledgeBase getKBFromStandardIn() {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	/*BufferedReader br = null;
    	
        try { 
        	br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream("C:/spring2013/cs830/prog2/problems/cnf/complex/grads/curious.cnf")));
            //br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream("/home/csg/crr8/spring2013/cs830/prog2/problems/lotr_3.cnf")));
        } catch (java.io.FileNotFoundException e1) {
        	e1.printStackTrace();
        	System.exit(1);
        }*/
        
        List<Clause> input = new ArrayList<Clause>();
        List<Clause> setOfSupport = new ArrayList<Clause>();
        boolean readingInput = true;
        
        while (true) {
	        try {
				String line = br.readLine();
				
				if (line == null) {
				    break;
				} else if (line.equals("--- negated query ---")) {
				    readingInput = false;
				} else if (readingInput) {
					input.add(parseClauseHelper(line));
				} else {
				    setOfSupport.add(parseClauseHelper(line));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        try {
            br.close();
        } catch (IOException e) {}
        
        if (!setOfSupport.isEmpty()) {
        	return new KnowledgeBase(input, setOfSupport);
        }
               
        return null;
    }
        
    private static Clause parseClauseHelper(String clause) {
        Variable.resetVariables();
        Clause c = parseClause(clause);
        c = Clause.getSortedClause(c);        
        c.subAllVariablesForPrinting();
        
        return c;
    }
    
    private static Clause parseClause(String clause) {
        clause = clause.trim();
        
        String firstLiteral = "";
        int i = 0;
        
        while (i < clause.length() && clause.charAt(i) != '|') {
            firstLiteral = firstLiteral + clause.charAt(i);
            i++;
        }
        
        Literal literal = parseLiteral(firstLiteral);
        
        if (i < clause.length()) {
            String rest = clause.substring(i + 1, clause.length());
            
            return new Clause(literal, parseClause(rest));
        }
        
        return new Clause(literal);
    }
    
    private static Literal parseLiteral(String literal) {
        literal = literal.trim();
        
        boolean positive = true;
        
        if (literal.startsWith("-")) {
            positive = false;
            literal = literal.substring(1, literal.length());
        }
        
        return new Literal(parsePredicate(literal), positive);
    }
    
    private static PredicateInstance parsePredicate(String predicate) {
        predicate = predicate.trim();
        
        int open = predicate.indexOf("(");
        
        String name = predicate.substring(0, open);
        String termList = predicate.substring(open + 1, predicate.length() - 1);
        
        return Predicate.getPredicateInstance(name, parseTermList(termList));
    }
    
    private static List<Term> parseTermList(String termList) {
        termList = termList.trim();
        
        List<Term> list = new ArrayList<Term>();
        
        String firstTerm = "";
        int i = 0;
        int parens = 0;
        
        while (i < termList.length()) {
            if (parens == 0 && termList.charAt(i) == ',') {
                break;
            }
            if (termList.charAt(i) == '(') {
                parens++;
            }
            if (termList.charAt(i) == ')') {
                parens--;
            }
            
            firstTerm = firstTerm + termList.charAt(i);
            i++;
        }
        
        list.add(parseTerm(firstTerm));
        
        if (i < termList.length()) {
            String rest = termList.substring(i + 1, termList.length());
            
            list.addAll(parseTermList(rest));
        }
        
        return list;
    }
    
    private static Term parseTerm(String term) {
        term = term.trim();
        
        if (Character.isLowerCase(term.charAt(0))) {
            return Variable.getVariable(term.toLowerCase());
        }
        
        int open = term.indexOf("(");
        
        if (open == -1) {
            return Constant.getConstant(term.toUpperCase());
        } else {
            String functionName = term.substring(0, open);                        
            String termList = term.substring(functionName.length() + 1, term.length() - 1);
            
            functionName = functionName.trim();
            functionName = functionName.substring(0, 1) + functionName.substring(1).toLowerCase();
            
            return Function.getFunctionInstance(functionName, parseTermList(termList));
        } 
    }
}
