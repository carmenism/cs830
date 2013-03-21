import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {
    private final HashSet<Predicate> positive;
    private final HashSet<Predicate> negative;
    
    public State(HashSet<Predicate> positive, HashSet<Predicate> negative) {
        this.positive = positive;
        this.negative = negative;
    }
    
    public HashSet<Predicate> getPositive() {
        return positive;
    }

    public HashSet<Predicate> getNegative() {
        return negative;
    }

    public HashSet<Action> getPossibleActions() {
        HashSet<Action> actions = new HashSet<Action>();
        
        for (Action action : Program3.groundedActions) {
            if (action.possible(this)) {
                actions.add(action);
            }
        }
        
        return actions;
    }
    
    public List<State> expand() {
        List<State> states = new ArrayList<State>();
        
        return states;
    }
}
