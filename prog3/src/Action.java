import java.util.HashMap;
import java.util.HashSet;

public class Action {
    private final HashSet<Predicate> pre;
    private final HashSet<Predicate> preneg;
    private final HashSet<Predicate> del;
    private final HashSet<Predicate> add;
    
    private final String action;
    
    public Action(UngroundedAction ungroundedAction,
            HashMap<Variable, Constant> substitutions, HashSet<Predicate> pre,
            HashSet<Predicate> preneg, HashSet<Predicate> del,
            HashSet<Predicate> add) {
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

    public HashSet<Predicate> getPre() {
        return pre;
    }

    public HashSet<Predicate> getPreneg() {
        return preneg;
    }

    public HashSet<Predicate> getDel() {
        return del;
    }

    public HashSet<Predicate> getAdd() {
        return add;
    }

    public boolean interferes(Action other) {        
        for (Predicate predicate : del) {
            if (other.pre.contains(predicate)) {
                return true;
            }
        }
        
        for (Predicate predicate : del) {
            if (other.add.contains(predicate)) {
                return true;
            }
        }
        
        for (Predicate predicate : add) {
            if (other.preneg.contains(predicate)) {
                return true;
            }
        }
                
        return false;
    }
    
    public boolean possible(State state) {
        return possible(state.getPositive(), state.getNegative());
    }

    public boolean possible(HashSet<Predicate> positive,
            HashSet<Predicate> negative) {        
        return positive.containsAll(pre) && negative.containsAll(preneg);
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
