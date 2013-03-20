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

        System.out.println("CONSTANTS");
        
        for (Constant c : Program3.constants.values()) {
            System.out.println(c);
        }

        System.out.println("\nPREDICATES");
        
        for (UngroundedPredicate up : Program3.ungroundedPreds.values()) {
            System.out.println(up);
        }
        
        System.out.println("\nACTIONS");
        
        for (UngroundedAction act : Program3.ungroundedActions) {
            System.out.println(act+"\n");
        }
        

        System.out.println("\nACTIONS");
        
        for (UngroundedAction act : Program3.ungroundedActions) {
            System.out.println(act+"\n");
        }
        
        System.out.println("\nINITIAL");
        
        for (Predicate p : Program3.initial) {
            System.out.println(p);
        }

        System.out.println("\nGOAL");
        
        for (Predicate p : Program3.goal) {
            System.out.println(p);
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
                    
                    for (UngroundedPredicate up : parseUngroundedPredicates(line)) {
                        Program3.ungroundedPreds.put(up.getName(), up);
                    }
                } else if (line.startsWith(CONSTANTS)) {
                    line = removeHeader(line, CONSTANTS);
                    
                    for (Constant constant : parseConstants(line)) {
                        Program3.constants.put(constant.getName(), constant);
                    }
                } else if (line.startsWith(INITIAL)) {
                    line = removeHeader(line, INITIAL);
                    
                    Program3.initial.addAll(parsePredicates(line));
                } else if (line.startsWith(GOAL)) {
                    line = removeHeader(line, GOAL);
                    
                    Program3.goal.addAll(parsePredicates(line));
                } else if (line.startsWith(GOALNEG)) {
                    line = removeHeader(line, GOALNEG);
                    
                    Program3.goalNeg.addAll(parsePredicates(line));
                } else if (line.trim().isEmpty()) { 
                    if (readingAction) {
                        readingAction = false;
                        
                        Program3.ungroundedActions.add(parseUngroundedAction(actionLines));
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
    
    private static UngroundedAction parseUngroundedAction(List<String> lines) {
        boolean firstLine = true;
        List<UngroundedPredicate> pre = new ArrayList<UngroundedPredicate>();
        List<UngroundedPredicate> preneg = new ArrayList<UngroundedPredicate>();
        List<UngroundedPredicate> del = new ArrayList<UngroundedPredicate>();
        List<UngroundedPredicate> add = new ArrayList<UngroundedPredicate>();
        
        UngroundedAction action = null;
        
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
                
                int space = line.indexOf(" ");
                String name = line.substring(0, space);
                line = line.substring(space);                
                List<Variable> vars = parseVariableList(line);
                
                action = new UngroundedAction(name, vars);
            } else if (line.startsWith(PRE)) {
                line = removeHeader(line, PRE);
                
                pre.addAll(parseUngroundedPredicates(line));
            } else if (line.startsWith(PRENEG)) {
                line = removeHeader(line, PRENEG);
                
                preneg.addAll(parseUngroundedPredicates(line));       
            } else if (line.startsWith(DEL)) {                
                line = removeHeader(line, DEL); 
                
                del.addAll(parseUngroundedPredicates(line));               
            } else if (line.startsWith(ADD)) {
                line = removeHeader(line, ADD);
                
                add.addAll(parseUngroundedPredicates(line));                 
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
    
    private static List<UngroundedPredicate> parseUngroundedPredicates(String line) {  
        List<UngroundedPredicate> preds = new ArrayList<UngroundedPredicate>();
        int open = line.indexOf("(");
        
        while (open != -1) {
            int closed = line.indexOf(")");
            
            String name = line.substring(0, open);
            //Predicate pred = getPredicate(name);
            
            String terms = line.substring(open + 1, closed);
            List<Variable> termList = parseVariableList(terms);
                        
            preds.add(new UngroundedPredicate(name, termList));
            
            line = line.substring(closed + 1).trim();
            open = line.indexOf("(");
        }
        
        return preds;
    }
    
    private static List<Predicate> parsePredicates(String line) {  
        List<Predicate> predUses = new ArrayList<Predicate>();
        int open = line.indexOf("(");
        
        while (open != -1) {
            int closed = line.indexOf(")");
            
            String name = line.substring(0, open);
            //UngroundedPredicate pred = getUngroundedPredicate(name);
            
            String terms = line.substring(open + 1, closed);
            List<Constant> termList = parseConstantList(terms);
                        
            predUses.add(new Predicate(name, termList));
            
            line = line.substring(closed + 1).trim();
            open = line.indexOf("(");
        }
        
        return predUses;
    }
    
    /*private static UngroundedPredicate getUngroundedPredicate(String name) {
        UngroundedPredicate predicate = Program3.ungroundedPreds.get(name);
        
        //if (predicate == null) {
        //    predicate = new UngroundedPredicate(name);
        //    
        //    Program3.predicates.put(name, predicate);
        //}
        
        return predicate;
    }*/
    
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