public class StateAction {
    public int[] statePrimes;
    private int numberTimesTaken = 0;
    private final int stateIndex;
    private final int actionIndex;

    public StateAction(int numberStates, int stateIndex, int actionIndex) {
        statePrimes = new int[numberStates];
        this.stateIndex = stateIndex;
        this.actionIndex = actionIndex;

        for (int s = 0; s < numberStates; s++) {
            statePrimes[s] = 0;
        }
    }
    
    public void addTimeTaken() {
        numberTimesTaken++;
    }
    
    public int getNumberTimesTaken() {
        return numberTimesTaken;
    }

    public boolean isUnexplored() {
        return getTotal() == 0;//statePrimes.length;
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
       
    public double getQ() {
        return Program4.Q[stateIndex][actionIndex];
    }       
     
    public double getExpectedUtility() {
        double expectedUtility = 0.0;
        int total = getTotal();
        
        if (total != 0) {
            for (int s = 0; s < statePrimes.length; s++) {
                double probability = ((double) statePrimes[s]) / total;

                expectedUtility += probability * Program4.lookupUtilityByIndex(s);
            }
        }
        
        return expectedUtility;
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
    
    public double getF() {
        double u = 0;
        int n = getNumberTimesTaken();

        switch (Program4.algorithm) {
        case GREEDY:
            u = getExpectedValue();
            break;
        case Q:
            u = getQ();
            break;
        case VI:
            u = getExpectedUtility();
            break;
        case PI:
            // u = ??
            break;
        }

        return getF(u, n);
    }
    
    public static double getF(double u, double n) {
        if (n < Program4.k) {
            return Program4.maxReward;
        }

        return u;
    }
}
