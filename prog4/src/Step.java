import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Step {
    private final int state;
        
    private HashMap<Integer, HashMap<Integer, StateFrequency>> actionToStates = new HashMap<Integer, HashMap<Integer, StateFrequency>>();
        
    public Step(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
    
    public int chooseAction(List<Integer> actions) {        
        switch(Program4.algorithm) {
        case GREEDY:
            return chooseGreedy(actions);
        case Q:
            return -1;
        case RANDOM:
            return chooseRandom(actions);
        case VI:
            return -1;
        }
        
        return -1;
    }
   
    public void addToHistory(int action, int statePrime) {
        HashMap<Integer, StateFrequency> states = actionToStates.get(action);
        
        if (states == null) {
            states = new HashMap<Integer, StateFrequency>();
            actionToStates.put(action, states);
        }
        
        StateFrequency sf = states.get(statePrime);
        
        if (sf == null) {
            sf = new StateFrequency(statePrime);
            states.put(statePrime, sf);
        }
        
        sf.increment();
    }
    
    private int mostLikelyStateFromAction(int possibleAction) {
        HashMap<Integer, StateFrequency> stateHistory = actionToStates.get(possibleAction);
                       
        StateFrequency maxSf = new StateFrequency(-1);
        
        for (StateFrequency sf : stateHistory.values()) {
            if (sf.getOccurences() > maxSf.getOccurences()) {
                maxSf = sf;
            }
        }
        
        return maxSf.getState();
    }
    
    private int chooseGreedy(List<Integer> actions) {
        double maxReward = Double.MIN_VALUE;
        int maxAction = -1;
        
        for (Integer possibleAction : actions) {
            HashMap<Integer, StateFrequency> stateHistory = actionToStates.get(possibleAction);
            
            if (stateHistory != null) {
                int mostLikelyState = mostLikelyStateFromAction(possibleAction);                
                double reward = Program4.lookupReward(mostLikelyState);
                
                if (reward > maxReward) {
                    maxReward = reward;
                    maxAction = possibleAction;
                }
            }
        }
        
        if (maxAction != -1) {
            return maxAction;
        }
        
        return chooseRandom(actions);
    }
    
    private int chooseRandom(List<Integer> actions) {
        Random random = new Random();
        
        int action = actions.get(random.nextInt(actions.size()));
        
        return action;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + state;
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
        Step other = (Step) obj;
        if (state != other.state)
            return false;
        return true;
    }
}
