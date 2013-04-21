public class TrainingSample extends Sample implements Comparable<TrainingSample> {
    private static final int BEFORE = -1;
    private static final int EQUAL = 0;
    private static final int AFTER = 1;
    
    public int yOriginal;
    public int[] y;
    
    public double distance = -1.0;
    
    public TrainingSample(int[] xVals, int numberClasses, int yVal) {
        super(xVals);
        
        this.yOriginal = yVal;
        this.y = new int[numberClasses];
        
        for (int i = 0; i < numberClasses; i++) {
            this.y[i] = 0;
        }
        
        this.y[yVal] = 1;
    }
    
    public void calculateDistance(Sample sample) {
        double sumSquares = 0.0;
        
        for (int i = 1; i < xNormal.length; i++) {
            double diff = xNormal[i] - sample.xNormal[i];
            double diffSquared = Math.pow(diff, 2);
            
            
            sumSquares += diffSquared;
        }
        
        distance = Math.pow(sumSquares, 0.5);
        
        if (distance == Double.NaN) {
            throw new RuntimeException("bad distance");
        }
    }

    @Override
    public int compareTo(TrainingSample other) {
        if (distance < other.distance) {
            return BEFORE;
        }
        
        if (distance > other.distance) {
            return AFTER;
        }
        
        return EQUAL;
    }
}