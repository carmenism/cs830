import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Function {
    private static HashMap<String, Function> functions = new HashMap<String, Function>();
    
    private List<FunctionInstance> instances = new ArrayList<FunctionInstance>();
    private String name; 
    
    private Function(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
        
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Function other = (Function) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
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
