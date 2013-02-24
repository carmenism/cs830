import java.util.ArrayList;
import java.util.List;


public class Parser {    
    public static void main(String [] args) {
        /*String [] clauses = {
            "-CoffeeMaker(x5) | -Makes(x5, x6) | Coffee(x6)",
            "Makes(GreenMountain, OurBlend)",
            "-Loves(x2, F2(x2)) | Loves(F1(x2), x2)",
            "God(x) | CanDie(x)",
            "Nesting(F(F(F(F(Jimmy, Sussie),Mark), John), Bob), Tim)",
            "Something(x1)|Nesting(F(F(F(F(Jimmy,Sussie),Mark),John),Bob),Tim)",
            "Something( x1 ) | Nesting( F ( F ( F ( F ( Jimmy, Sussie ), Mark ) , John ) , Bob ) , Tim ) ",
            "-Loves(x2, F2(x2)) | Loves(F1(x2), x2)",
            "-CoffeeMaker(x5)|-Makes(x5, x6)|Coffee(x6)"
        };
        
        parse(clauses, "");*/
        
        test();
    }
    
    public static void parse(String []  clauses, String negatedClause) {
        for (String s : clauses) {
            Clause c = parseClauseHelper(s);
            System.out.println(c);
            c.printAllLiterals();
        }
    }
        
    public static void test() {
        String clauseA = "-CoffeeMaker(x5) | -Makes(x5, F(b)) | Coffee(x6)";
        String clauseB = "Makes(GreenMountain, F(a))";
        
        Clause a = parseClauseHelper(clauseA);
        a.printAllLiterals();
        Clause b = parseClauseHelper(clauseB);
        b.printAllLiterals();
        
        System.out.println(a.resolve(b));
    }
    
    private static Clause parseClauseHelper(String clause) {
        Variable.resetVariables();
        
        return parseClause(clause);
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
            return Variable.getVariable(term);
        }
        
        int open = term.indexOf("(");
        
        if (open == -1) {
            return Constant.getConstant(term);
        } else {
            String functionName = term.substring(0, open);                        
            String termList = term.substring(functionName.length() + 1, term.length() - 1);
            
            return Function.getFunctionInstance(functionName.trim(), parseTermList(termList));
        } 
    }
}
