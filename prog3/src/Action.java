import java.util.List;

public class Action {
    private final List<Predicate> pre;
    private final List<Predicate> preneg;    
    private final List<Predicate> del;
    private final List<Predicate> add;
    
    public Action(List<Predicate> pre, List<Predicate> preneg, List<Predicate> del, List<Predicate> add) {
        this.pre = pre;
        this.preneg = preneg;
        this.del = del;
        this.add = add;
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
}
