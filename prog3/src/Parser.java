import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private static final String PREDICATES = "predicates:";
    private static final String CONSTANTS = "constants:";
    
    private static final String INITIAL = "initial:";
    private static final String GOAL = "goal:";
    private static final String GOALNEG = "goalneg:";
    
    private static final String PRE = "pre:";
    private static final String PRENEG = "preneg:";
    private static final String ADD = "add:";
    private static final String DEL = "del:";
    
    private static final String COMMENT = "#";
    
    public static void main(String [] args) {
        parseInput();
        
        for (Constant c : Program3.constants.values()) {
            System.out.println(c);
        }
        
        for (PredicateSpec ps : Program3.predSpecs) {
            System.out.println(ps);
        }
        
        for (Action act : Program3.actions) {
            System.out.println(act);
        }
    }
    
    public static void parseInput() {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // For running on my PC.
        BufferedReader br = null;     
        try { 
            br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream("C:/spring2013/cs830/prog3/input/coffeeworld-1.in")));
            //br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream("/home/csg/crr8/spring2013/cs830/prog2/problems/lotr_3.cnf")));
        } catch (java.io.FileNotFoundException e1) {
            e1.printStackTrace();
            System.exit(1);
        }
        
        String line = null;
        List<String> actionLines = new ArrayList<String>();
        boolean readingAction = false;
        
        try {
            while ((line = br.readLine()) != null) {                
                if (line.matches("^\\d+ actions")) {
                    // number of actions
                } else if (line.startsWith(COMMENT)) {
                    // comment line, skip
                } else if (line.startsWith(PREDICATES)) {
                    line = removeHeader(line, PREDICATES);
                    
                    Program3.predSpecs.addAll(parsePredicateSpecs(line));
                } else if (line.startsWith(CONSTANTS)) {
                    line = removeHeader(line, CONSTANTS);
                    
                    for (Constant constant : parseConstants(line)) {
                        Program3.constants.put(constant.getName(), constant);
                    }
                } else if (line.startsWith(INITIAL)) {
                    line = removeHeader(line, INITIAL);
                    
                    Program3.initial.addAll(parsePredicateUsages(line));
                } else if (line.startsWith(GOAL)) {
                    line = removeHeader(line, GOAL);
                    
                    Program3.goal.addAll(parsePredicateUsages(line));
                } else if (line.startsWith(GOALNEG)) {
                    line = removeHeader(line, GOALNEG);
                    
                    Program3.goalNeg.addAll(parsePredicateUsages(line));
                } else if (line.trim().isEmpty()) { 
                    if (readingAction) {
                        readingAction = false;
                        
                        Program3.actions.add(parseAction(actionLines));
                    }
                } else if (readingAction) {
                    actionLines.add(line.trim());
                } else {
                    readingAction = true;
                    
                    actionLines = new ArrayList<String>();
                    actionLines.add(line.trim());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static String removeHeader(String line, String header) {
        return line.substring(header.length()).trim();
    }
    
    private static Action parseAction(List<String> lines) {
        boolean firstLine = true;
        List<PredicateSpec> pre = new ArrayList<PredicateSpec>();
        List<PredicateSpec> preneg = new ArrayList<PredicateSpec>();
        List<PredicateSpec> del = new ArrayList<PredicateSpec>();
        List<PredicateSpec> add = new ArrayList<PredicateSpec>();
        
        Action action = null;
        
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
                
                int space = line.indexOf(" ");
                String name = line.substring(0, space);
                line = line.substring(space);                
                List<Variable> vars = parseVariableList(line);
                
                action = new Action(name, vars);
            } else if (line.startsWith(PRE)) {
                line = removeHeader(line, PRE);
                
                pre.addAll(parsePredicateSpecs(line));
            } else if (line.startsWith(PRENEG)) {
                line = removeHeader(line, PRENEG);
                
                preneg.addAll(parsePredicateSpecs(line));       
            } else if (line.startsWith(DEL)) {                
                line = removeHeader(line, DEL); 
                
                del.addAll(parsePredicateSpecs(line));               
            } else if (line.startsWith(ADD)) {
                line = removeHeader(line, ADD);
                
                add.addAll(parsePredicateSpecs(line));                 
            }
        }
        
        if (action != null) {
            action.setPre(pre);
            action.setPreneg(preneg);
            action.setDel(del);
            action.setAdd(add);
        }
        
        return action;
    }
    
    private static List<PredicateSpec> parsePredicateSpecs(String line) {  
        List<PredicateSpec> predSpecs = new ArrayList<PredicateSpec>();
        int open = line.indexOf("(");
        
        while (open != -1) {
            int closed = line.indexOf(")");
            
            String name = line.substring(0, open);
            Predicate pred = getPredicate(name);
            
            String terms = line.substring(open + 1, closed);
            List<Variable> termList = parseVariableList(terms);
                        
            predSpecs.add(new PredicateSpec(pred, termList));
            
            line = line.substring(closed + 1).trim();
            open = line.indexOf("(");
        }
        
        return predSpecs;
    }
    
    private static List<PredicateUsage> parsePredicateUsages(String line) {  
        List<PredicateUsage> predUses = new ArrayList<PredicateUsage>();
        int open = line.indexOf("(");
        
        while (open != -1) {
            int closed = line.indexOf(")");
            
            String name = line.substring(0, open);
            Predicate pred = getPredicate(name);
            
            String terms = line.substring(open + 1, closed);
            List<Constant> termList = parseConstantList(terms);
                        
            predUses.add(new PredicateUsage(pred, termList));
            
            line = line.substring(closed + 1).trim();
            open = line.indexOf("(");
        }
        
        return predUses;
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
        List<Variable> list = new ArrayList<Variable>();
        
        for (String term : terms.split(",")) {
            term = term.trim();
            Variable var = vars.get(term);
            
            if (var == null) {
                var = new Variable(term);
                vars.put(term, var);
            }
            
            list.add(var);
        }
        
        return list;
    }
    
    private static List<Constant> parseConstantList(String terms) {
        List<Constant> list = new ArrayList<Constant>();
        
        for (String term : terms.split(",")) {
            list.add(Program3.constants.get(term.trim()));
        }
        
        return list;
    }
    
    private static List<Constant> parseConstants(String line) {        
        List<Constant> constants = new ArrayList<Constant>();
        String[] tokens = line.split("\\s+");

        for (String token : tokens) {
            constants.add(new Constant(token));
        }
        
        return constants;
    }
}
