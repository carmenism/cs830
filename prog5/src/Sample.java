public class Sample {
    public int[] x;
    public double[] xNormal;
    
    public Sample(int[] xVals) {
        this.x = xVals;
    }
    
    public void normalize(double[] averages, double[] standardDeviations) {
        this.xNormal = new double[x.length];
        
        this.xNormal[0] = 0;
        
        for (int i = 1; i < x.length; i++) {
            if (standardDeviations[i] == 0) {
                xNormal[i] = 0;//x[i];
            } else {
                xNormal[i] = (x[i] - averages[i]) / standardDeviations[i];                
            }
            
            if (xNormal[i] == Double.NaN) {
                throw new RuntimeException("bad normalization");
            }
        }
    }
}
