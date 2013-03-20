import java.util.List;

public class Action {
    private List<PredicateSpec> pre;
    private List<PredicateSpec> preneg;
    
    private List<PredicateSpec> del;
    private List<PredicateSpec> add;
    
    private final String name;
    private final List<Variable> variables;
    
    public Action(String name, List<Variable> variables) {
        this.name = name;
        this.variables = variables;
    }

    public List<PredicateSpec> getPre() {
        return pre;
    }

    public void setPre(List<PredicateSpec> pre) {
        this.pre = pre;
    }

    public List<PredicateSpec> getPreneg() {
        return preneg;
    }

    public void setPreneg(List<PredicateSpec> preneg) {
        this.preneg = preneg;
    }

    public List<PredicateSpec> getDel() {
        return del;
    }

    public void setDel(List<PredicateSpec> del) {
        this.del = del;
    }

    public List<PredicateSpec> getAdd() {
        return add;
    }

    public void setAdd(List<PredicateSpec> add) {
        this.add = add;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return variables;
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
        
        for (PredicateSpec ps : pre) {
            ret.append(" ");
            ret.append(ps);
        }
        
        ret.append("\npreneg:");
        
        for (PredicateSpec ps : preneg) {
            ret.append(" ");
            ret.append(ps);
        }
        
        ret.append("\ndel:");
        
        for (PredicateSpec ps : del) {
            ret.append(" ");
            ret.append(ps);
        }
        
        ret.append("\nadd:");
        
        for (PredicateSpec ps : add) {
            ret.append(" ");
            ret.append(ps);
        }
        
        return ret.toString();
    }
} 
