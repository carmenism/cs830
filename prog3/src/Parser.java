import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private static final String PREDICATES = "predicates:";
    private static final String CONSTANTS = "constants:";
    private static final String COMMENT = "#";
    
    private static void parseInput() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // For running on my PC.
        /*BufferedReader br = null;     
        try { 
            br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream("C:/spring2013/cs830/prog2/problems/cnf/complex/grads/curious.cnf")));
            //br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream("/home/csg/crr8/spring2013/cs830/prog2/problems/lotr_3.cnf")));
        } catch (java.io.FileNotFoundException e1) {
            e1.printStackTrace();
            System.exit(1);
        }*/
    }
    
    private static void parseAction(List<String> lines) {
        
    }
    
    private static void parsePredicateSpecs(String line) {
        line = line.substring(PREDICATES.length()).trim();
        
        int open = line.indexOf("(");
        
        while (open != -1) {
            int closed = line.indexOf(")");
            
            String name = line.substring(0, open);
            Predicate predicate = getPredicate(name);
            
            String terms = line.substring(open + 1, closed);
            List<Variable> termList = parseVariableList(terms);
                        
            Program3.predicateSpecs.add(new PredicateSpec(predicate, termList));
            
            line = line.substring(closed + 1);
            open = line.indexOf("(");
        }
    }
    
    private static Predicate getPredicate(String name) {
        Predicate predicate = Program3.predicates.get(name);
        
        if (predicate == null) {
            predicate = new Predicate(name);
            
            Program3.predicates.put(name, predicate);
        }
        
        return predicate;
    }
    
    private static List<Variable> parseVariableList(String terms) {
        HashMap<String, Variable> vars = new HashMap<String, Variable>();
        List<Variable> termList = new ArrayList<Variable>();
        
        for (String term : terms.split(",")) {
            Variable var = vars.get(term);
            
            if (var == null) {
                var = new Variable(term);
                vars.put(term, var);
            }
            
            termList.add(var);
        }
        
        return termList;
    }
    
    private static void parseConstants(String line) {
        line = line.substring(CONSTANTS.length()).trim();
        
        String[] constants = line.split("\\s+");

        for (String constant : constants) {
            Program3.constants.add(new Constant(constant));
        }
    }
}
