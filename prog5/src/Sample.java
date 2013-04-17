public class Sample {
    public int[] x;
    public double[] xNormal;
    
    public Sample(int numberAttributes, int[] xVals) {
        this.x = new int[numberAttributes];
        
        for (int i = 0; i < numberAttributes; i++) {
            this.x[i] = xVals[i];
        }
    }
    
    public void normalize(double[] averages, double[] standardDeviations) {
        this.xNormal = new double[x.length];
        
        for (int i = 0; i < x.length; i++) {
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
