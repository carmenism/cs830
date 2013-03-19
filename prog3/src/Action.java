import java.util.List;

public class Action {
    private List<PredicateSpec> pre;
    private List<PredicateSpec> preneg;
    
    private List<PredicateSpec> del;
    private List<PredicateSpec> add;
    
    private String name;
    private List<Variable> terms;
    
    public Action() {
        
    }
} 
