import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Function extends Term {
    private static HashMap<String, Function> functions = new HashMap<String, Function>();
    private List<FunctionInstance> instances = new ArrayList<FunctionInstance>();
    
    private Function(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static FunctionInstance getFunctionInstance(String name, List<Term> termList) {
        Function function = functions.get(name);
        
        if (function == null) {
            function = new Function(name);
            functions.put(name, function);
        }
        
        FunctionInstance fi = new FunctionInstance(function, termList);
        function.instances.add(fi);
        
        return fi;
    }
}
