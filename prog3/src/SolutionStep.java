/**
 * Represents a step of the solution to the STRIPS planning problem. A step
 * consists of an action and the time when the action was done.
 * 
 * @author Carmen St. Jean
 * 
 */
public class SolutionStep {
    private final int time;
    private final Action action;

    /**
     * Creates a SolutionStep from the specified time and action.
     * 
     * @param time
     *            The time where the action was taken.
     * @param action
     *            The action taken in this step of the solution.
     */
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
