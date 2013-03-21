import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Action {
    private final List<Predicate> pre;
    private final List<Predicate> preneg;
    private final List<Predicate> del;
    private final List<Predicate> add;

    private final String action;

    public Action(UngroundedAction ungroundedAction,
            HashMap<Variable, Constant> substitutions, List<Predicate> pre,
            List<Predicate> preneg, List<Predicate> del, List<Predicate> add) {
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

    public boolean possible(State state) {
        return possible(state.getPositive(), state.getNegative());
    }
    
    public boolean possible(HashSet<Predicate> positive, HashSet<Predicate> negative) {
        BitSet pos = new BitSet(pre.size());
        BitSet neg = new BitSet(preneg.size());
        
        
        if (pre.size() != 0) {
            pos.flip(0, pre.size());
    
            for (int i = 0; i < pre.size(); i++) {
                Predicate p = pre.get(i);
                
                if (positive.contains(p)) {
                    pos.clear(i);
                }
            }            
        }
        
        if (preneg.size() != 0) {
            neg.flip(0, preneg.size());
            
            for (int i = 0; i < preneg.size(); i++) {
                Predicate p = preneg.get(i);
                
                if (negative.contains(p)) {
                    neg.clear(i);
                }
            }
        }
        
        return pos.isEmpty() && neg.isEmpty();
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
