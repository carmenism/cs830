import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UngroundedPredicate {
    private final String name;
    private final List<Term> terms;

    public UngroundedPredicate(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    public Predicate ground(HashMap<Variable, Constant> substitutions)
            throws SubstitutionMissingException {
        List<Constant> constants = new ArrayList<Constant>();

        for (Term term : terms) {
            Constant con = null;
            
            if (term instanceof Variable) {
                Variable var = (Variable) term;
                con = substitutions.get(var);
    
                if (con == null) {    
                    System.out.println(substitutions.keySet());
                    System.out.println(substitutions.values());
                    
                    throw new SubstitutionMissingException();
                }
            } else {
                con = (Constant) term;
            }

            constants.add(con);
        }

        Predicate predicate = new Predicate(name, constants);
        
        if (Program3.allGroundedPredicates.containsKey(predicate)) {
            predicate = Program3.allGroundedPredicates.get(predicate);
        } else {
            Program3.allGroundedPredicates.put(predicate, predicate);
        }
        
        return predicate;
    }

    public static List<Predicate> ground(List<UngroundedPredicate> ugPreds,
            HashMap<Variable, Constant> substitutions)
            throws SubstitutionMissingException {
        List<Predicate> preds = new ArrayList<Predicate>();

        for (UngroundedPredicate ugPred : ugPreds) {
            preds.add(ugPred.ground(substitutions));
        }

        return preds;
    }

    public String getName() {
        return name;
    }

    public List<Term> getVariables() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();

        ret.append(name);
        ret.append("(");

        for (Term term : terms) {
            ret.append(term);
            ret.append(", ");
        }

        ret.deleteCharAt(ret.length() - 1);
        ret.deleteCharAt(ret.length() - 1);
        ret.append(")");

        return ret.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((terms == null) ? 0 : terms.hashCode());
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
        UngroundedPredicate other = (UngroundedPredicate) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (terms == null) {
            if (other.terms != null)
                return false;
        } else if (!terms.equals(other.terms))
            return false;
        return true;
    }
}
