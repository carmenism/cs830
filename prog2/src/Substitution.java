
public class Substitution {
    private Term substitute;
    private Variable replaced;
    
    public Substitution(Term substitute, Variable replaced) {
        this.substitute = substitute;
        this.replaced = replaced;
    }
    
    public boolean equalsSubstitute(Term s) {
        return s.equals(substitute);
    }
       
    public boolean equalsReplaced(Variable r) {
        return r.equals(replaced);
    }

    public Term getSubstitute() {
        return substitute;
    }

    public Variable getReplaced() {
        return replaced;
    }    
}
