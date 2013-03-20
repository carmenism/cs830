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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((add == null) ? 0 : add.hashCode());
        result = prime * result + ((del == null) ? 0 : del.hashCode());
        result = prime * result + ((pre == null) ? 0 : pre.hashCode());
        result = prime * result + ((preneg == null) ? 0 : preneg.hashCode());
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
        Action other = (Action) obj;
        if (add == null) {
            if (other.add != null)
                return false;
        } else if (!add.equals(other.add))
            return false;
        if (del == null) {
            if (other.del != null)
                return false;
        } else if (!del.equals(other.del))
            return false;
        if (pre == null) {
            if (other.pre != null)
                return false;
        } else if (!pre.equals(other.pre))
            return false;
        if (preneg == null) {
            if (other.preneg != null)
                return false;
        } else if (!preneg.equals(other.preneg))
            return false;
        return true;
    }
}
