import java.util.HashMap;
import java.util.List;

public class Action {
    private final List<Predicate> pre;
    private final List<Predicate> preneg;    
    private final List<Predicate> del;
    private final List<Predicate> add;
    
    private final String action;
        
    public Action(UngroundedAction ungroundedAction, HashMap<Variable, Constant> substitutions, List<Predicate> pre, List<Predicate> preneg, List<Predicate> del, List<Predicate> add) {
        this.pre = pre;
        this.preneg = preneg;
        this.del = del;
        this.add = add;

        StringBuffer sb = new StringBuffer();
        
        sb.append(ungroundedAction.getName());
        
        for (Variable var : ungroundedAction.getVariables()) {
            sb.append(" ");
            sb.append(substitutions.get(var));
        }
        
        this.action = sb.toString();
    }

    public String getAction() {
        return action;
    }

    public List<Predicate> getPre() {
        return pre;
    }

    public List<Predicate> getPreneg() {
        return preneg;
    }

    public List<Predicate> getDel() {
        return del;
    }

    public List<Predicate> getAdd() {
        return add;
    }
    
    public String toStringDetails() {
        StringBuffer ret = new StringBuffer();

        ret.append(action);
        
        ret.append("\npre:");
        
        for (Predicate p : pre) {
            ret.append(" ");
            ret.append(p);
        }
        
        ret.append("\npreneg:");
        
        for (Predicate p : preneg) {
            ret.append(" ");
            ret.append(p);
        }
        
        ret.append("\ndel:");
        
        for (Predicate p : del) {
            ret.append(" ");
            ret.append(p);
        }
        
        ret.append("\nadd:");
        
        for (Predicate p : add) {
            ret.append(" ");
            ret.append(p);
        }
        
        return ret.toString();
    }
    
    @Override
    public String toString() {
        return action;
    }
}
