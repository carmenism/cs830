public class StateAction {
    public int[] statePrimes;
    private int numberTimesTaken = 0;

    public StateAction(int numberStates) {
        statePrimes = new int[numberStates];

        for (int s = 0; s < numberStates; s++) {
            statePrimes[s] = 1;
        }
    }
    
    public void addTimeTaken() {
        numberTimesTaken++;
    }
    
    public int getNumberTimesTaken() {
        return numberTimesTaken;
    }

    public boolean isUnexplored() {
        return getTotal() == statePrimes.length;
    }
    
    public int getTotal() {
        int total = 0;

        for (int s = 0; s < statePrimes.length; s++) {
            total += statePrimes[s];
        }

        return total;
    }

    public double getProbability(State state) {
        int stateIndex = Program4.lookupStateIndex(state);
        int total = getTotal();
        
        return ((double) statePrimes[stateIndex]) / total;
    }
    
    public double getF() {
        double u = getExpectedUtility();
        int n = getNumberTimesTaken();
        
        if (n < Program4.k) {
            return Program4.maxReward;
        }

        return u;
    }
    
    public double getExpectedUtility() {
        double expectedValue = 0.0;
        int total = getTotal();
        
        if (total != 0) {
            for (int s = 0; s < statePrimes.length; s++) {
                double probability = ((double) statePrimes[s]) / total;

                expectedValue += probability * Program4.lookupUtilityByIndex(s);
            }
        }
        
        return expectedValue;
    }
    
    public double getExpectedValue() {
        double expectedValue = 0.0;
        int total = getTotal();

        if (total != 0) {
            for (int s = 0; s < statePrimes.length; s++) {
                double probability = ((double) statePrimes[s]) / total;

                expectedValue += Program4.lookupRewardFromIndex(s) * probability;
            }
        }

        return expectedValue;
    }
}
