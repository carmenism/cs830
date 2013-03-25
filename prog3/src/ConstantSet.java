import java.util.List;
import java.util.ArrayList;

public class ConstantSet extends ArrayList<List<Constant>> {
    private static final long serialVersionUID = 1L;
    
    private final Integer length;

    public ConstantSet(int length, List<Constant> allConstants) {
        this.length = length;
        
        int n = allConstants.size();
        
        for (int r = 0; r < Math.pow(n, length); r++) {
            List<Constant> list = new ArrayList<Constant>();
            
            for (int c = 0; c < length; c++) {
                int i = (r / (int) Math.pow(n, c)) % n;
                
                list.add(allConstants.get(i));
            }

            super.add(list);
        } 
    }
    
    public Integer getLength() {
        return length;
    }

    @SuppressWarnings("unused")
    public static void main(String [] args) {
        List<Constant> constants = new ArrayList<Constant>();
        
        constants.add(new Constant("A"));
        constants.add(new Constant("B"));
        constants.add(new Constant("C"));
        constants.add(new Constant("D"));
        constants.add(new Constant("E"));
        
        ConstantSet cs = new ConstantSet(2, constants);
    }
}
