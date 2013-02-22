import java.util.List;


public class FunctionInstance extends Term {
    private Function function;
    private List<Term> termList;
    
    public FunctionInstance(Function function, List<Term> termList) {
        super(function.name);
        this.function = function;
        this.termList = termList;
        
        for (Term term : termList) {
            addChild(term);
        }
    }
    
    @Override
    public String toString() {
        if (termList.size() == 1) {
            return function + "(" + termList.get(0) + ")";
        } else {
            String ret = function + "(";

            for (int i = 0; i < termList.size() - 1; i++) {
                ret = ret + termList.get(i) + ", ";
            }

            return ret + termList.get(termList.size() - 1) + ")";
        }
    }
}
