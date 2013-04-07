
public class StateFrequency {
    private final int state;
    private int occurences;
    
    public StateFrequency(int state) {
        this.state = state;
    }
    
    public void increment() {
        occurences++;
    }
    
    public int getOccurences() {
        return occurences;
    }
    
    public int getState() {
        return state;
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
        StateFrequency other = (StateFrequency) obj;
        if (state != other.state)
            return false;
        return true;
    }
}
