import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class UngroundedAction {
    private List<UngroundedPredicate> pre;
    private List<UngroundedPredicate> preneg;
    
    private List<UngroundedPredicate> del;
    private List<UngroundedPredicate> add;
    
    private final String name;
    private final List<Variable> variables;
    
    public UngroundedAction(String name, List<Variable> variables) {
        this.name = name;
        this.variables = variables;
    }

    public List<UngroundedPredicate> getPre() {
        return pre;
    }

    public void setPre(List<UngroundedPredicate> pre) {
        this.pre = pre;
    }

    public List<UngroundedPredicate> getPreneg() {
        return preneg;
    }

    public void setPreneg(List<UngroundedPredicate> preneg) {
        this.preneg = preneg;
    }

    public List<UngroundedPredicate> getDel() {
        return del;
    }

    public void setDel(List<UngroundedPredicate> del) {
        this.del = del;
    }

    public List<UngroundedPredicate> getAdd() {
        return add;
    }

    public void setAdd(List<UngroundedPredicate> add) {
        this.add = add;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return variables;
    }
    
    public int length() {
        return variables.size();
    }
    
    public List<Action> ground(ConstantSet constantSets) {        
        List<Action> actions = new ArrayList<Action>();
        
        for (List<Constant> constants : constantSets) {
            Action action = ground(constants);
            
            if (action != null) {
                actions.add(action);
            }
        }
                
        return actions;
    }
    
    public Action ground(List<Constant> constants) {
        HashSet<Predicate> newPre = null;            
        HashSet<Predicate> newPreneg = null;
        HashSet<Predicate> newDel = null;
        HashSet<Predicate> newAdd = null;
        
        try {
            if (variables.size() == constants.size()) {
                HashMap<Variable, Constant> subs = new HashMap<Variable, Constant>();
                
                for (int i = 0; i < variables.size(); i++) {
                    Variable var = variables.get(i);
                    Constant con = constants.get(i);
                    
                    subs.put(var, con);
                }                
                
                newPre = UngroundedPredicate.ground(pre, subs);            
                newPreneg = UngroundedPredicate.ground(preneg, subs);
                newDel = UngroundedPredicate.ground(del, subs);
                newAdd = UngroundedPredicate.ground(add, subs);
                
                return new Action(this, subs, newPre, newPreneg, newDel, newAdd);
            }
        } catch (SubstitutionMissingException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        
        ret.append(name);
        
        for (Variable var : variables) {
            ret.append(" ");
            ret.append(var);
        }
        
        ret.append("\npre:");
        
        for (UngroundedPredicate up : pre) {
            ret.append(" ");
            ret.append(up);
        }
        
        ret.append("\npreneg:");
        
        for (UngroundedPredicate up : preneg) {
            ret.append(" ");
            ret.append(up);
        }
        
        ret.append("\ndel:");
        
        for (UngroundedPredicate up : del) {
            ret.append(" ");
            ret.append(up);
        }
        
        ret.append("\nadd:");
        
        for (UngroundedPredicate up : add) {
            ret.append(" ");
            ret.append(up);
        }
        
        return ret.toString();
    }
} 
