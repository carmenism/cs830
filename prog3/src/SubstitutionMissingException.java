
public class SubstitutionMissingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7711729846180226052L;
    
    public SubstitutionMissingException() {
        super("Variable was not properly substituted.");
    }
    
    public SubstitutionMissingException(String message) {
        super(message);
    }
}
