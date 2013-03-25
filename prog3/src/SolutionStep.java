
public class SolutionStep {
    private final int time;
    private final Action action;
    
    public SolutionStep(int time, Action action) {
        this.time = time;
        this.action = action;
    }

    public int getTime() {
        return time;
    }

    public Action getAction() {
        return action;
    }
    
    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        
        ret.append(time);
        ret.append(" ");
        ret.append(action);
        
        return ret.toString();
    }
}
