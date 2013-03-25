import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    public boolean possible(State state) {
        return possible(state.getPositive(), state.getNegative());
    }

    public boolean possible(HashSet<Predicate> positive,
            HashSet<Predicate> negative) {
        BitSet pos = new BitSet(pre.size());
        BitSet neg = new BitSet(preneg.size());

        List<Predicate> preTemp = new ArrayList<Predicate>(pre);
        List<Predicate> prenegTemp = new ArrayList<Predicate>(preneg);
        
        if (pre.size() != 0) {
            pos.flip(0, pre.size());

            for (int i = 0; i < preTemp.size(); i++) {
                Predicate p = preTemp.get(i);

                if (positive.contains(p)) {
                    pos.clear(i);
                }
            }
        }

        if (preneg.size() != 0) {
            neg.flip(0, preneg.size());

            for (int i = 0; i < prenegTemp.size(); i++) {
                Predicate p = prenegTemp.get(i);

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
