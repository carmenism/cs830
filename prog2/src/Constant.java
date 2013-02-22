import java.util.HashMap;

public class Constant extends Term {
    private static HashMap<String, Constant> constants = new HashMap<String, Constant>();
    
    private Constant(String name) {    
        super(name);
    }
    
    public static Constant getConstant(String name) {
        Constant constant = constants.get(name);
        
        if (constant == null) {
            constant = new Constant(name);
            
            constants.put(name, constant);
        }
        
        return constant;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
