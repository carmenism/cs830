import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Program5 {
    private static final String ARG_KNN = "knn";
    private static final String ARG_LINEAR = "linear";
    private static final String ARG_NB = "nb";

    private static final String INDICATOR_TRAIN = "-- training --";
    private static final String INDICATOR_TEST = "-- test --";

    private static final String FIRST_LINE_ATTRS = " attributes";
    private static final String FIRST_LINE_VALUES = " values";
    private static final String FIRST_LINE_CLASSES = " classes";
   
    private enum Algorithm {
        KNN, LINEAR, NB
    }

    private Algorithm algorithm;
    private int numberAttributes;
    private int numberValues;
    private int numberClasses;

    private int k = 5;

    private int N = 0;
    
    private List<TrainingSample> trainingSamples = new ArrayList<TrainingSample>();

    private double[][] thetas;
    private double[] averages;
    private double[] standardDeviations;

    public Program5(String[] args) {
        parseArgs(args);
        parseStandardIn();
    }

    private void parseStandardIn() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        boolean firstLine = true;
        boolean readingTraining = true;
        
        try {            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    parseFirstLine(line);

                    firstLine = false;
                } else if (line.equals(INDICATOR_TRAIN)) {
                    readingTraining = true;
                } else if (line.equals(INDICATOR_TEST)) {
                    readingTraining = false;
                    normalize();
                } else if (readingTraining) {
                    N++;
                    
                    TrainingSample sample = parseTraining(line);

                    trainingSamples.add(sample);
                    
                    learn(sample);
                } else {
                    Sample sample = parseTest(line);
                    normalize(sample);
                    classify(sample);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void normalize(Sample sample) {
        if (algorithm == Algorithm.LINEAR) {
            return;
        }        

        sample.normalize(averages, standardDeviations);
    }
    
    private void normalize() {
        if (algorithm == Algorithm.LINEAR) {
            return;
        } 
        
        averages = new double[numberAttributes + 1];
        standardDeviations = new double[numberAttributes + 1];

        int N = trainingSamples.size();

        averages[1] = 1;
        
        for (int i = 1; i < numberAttributes; i++) {
            averages[i] = 0.0;

            for (TrainingSample sample : trainingSamples) {
                averages[i] += sample.x[i];
            }

            averages[i] = averages[i] / N;
        }

        standardDeviations[0] = 0;
        
        for (int i = 1; i < numberAttributes; i++) {
            standardDeviations[i] = 0.0;

            for (TrainingSample sample : trainingSamples) {
                double diff = sample.x[i] - averages[i];
                double diffSquared = Math.pow(diff, 2);

                standardDeviations[i] += diffSquared;
            }

            double tmp = (1.0 / N) * standardDeviations[i];

            standardDeviations[i] = Math.pow(tmp, 0.5);
        }

        for (TrainingSample sample : trainingSamples) {
            sample.normalize(averages, standardDeviations);
        }
    }

    private void learn(TrainingSample sample) {
        if (algorithm == Algorithm.LINEAR) {
            offlineLinear(sample);
        }        
    }
    
    private void offlineLinear(TrainingSample sample) {
        double alpha = 1.0 / N;        
        double[] yHat = regressionPredict(sample);
        
        for (int aClass = 0; aClass < numberClasses; aClass++) {            
            for (int attr = 0; attr < numberAttributes + 1; attr++) {
                thetas[aClass][attr] = thetas[aClass][attr] - alpha * (yHat[aClass] - sample.y[aClass]) * sample.x[attr];
            }
        }
    }
    
    private void regressionClassify(Sample sample) {
        double[] yHat = regressionPredict(sample);
        int maxIndex = 0;
        double sumOfAll = 0.0;
        
        for (int i = 0; i < yHat.length; i++) {  
            if (yHat[i] > yHat[maxIndex]) {
                maxIndex = i;
            }
            
            if (yHat[i] > 0) {
                sumOfAll += 1;//Math.abs(yHat[i]);
           }
        }
        
        double confidence = 1.0/sumOfAll;//yHat[maxIndex] / sumOfAll;

        //System.err.println(maxIndex + " " + confidence);
        System.out.println(maxIndex + " " + confidence);
    }
    
    private double[] regressionPredict(Sample sample) {
        double[] yHat = new double[numberClasses];
        
        for (int i = 0; i < numberClasses; i++) {
            yHat[i] = dotProduct(thetas[i], sample.x); 
        }
        
        return yHat;
    }
    
    private double dotProduct(double [] vectorA, int [] vectorB) {
        double sum = 0.0;
        
        for (int i = 0; i < vectorA.length; i++) {
            sum += vectorA[i] * vectorB[i];
        }
        
        return sum;
    }
    
    private void classify(Sample sample) {
        if (algorithm == Algorithm.KNN) {
            knn(sample);
        } else if (algorithm == Algorithm.LINEAR) {
            regressionClassify(sample);
        }
    }
    
    private void knn(Sample sample) {
        List<TrainingSample> kNN = getKNearestNeightbors(sample, k);

        int kSize = k;

        if (kNN.size() < k) {
            kSize = kNN.size();
        }

        double minDistance = kNN.get(0).distance;

        if (minDistance == 0) {
            List<TrainingSample> zeroSamples = new ArrayList<TrainingSample>();
        
            for (TrainingSample ts : kNN) {
                if (ts.distance == 0) {
                    zeroSamples.add(ts);
                }
            }

            kNN = zeroSamples;
            
            double[] y = new double[numberClasses];

            for (int i = 0; i < numberClasses; i++) {
                y[i] = 0;

                for (TrainingSample ts : zeroSamples) {
                    y[i] += ts.y[i];
                }

                y[i] = (1.0 / zeroSamples.size()) * y[i];
            }

            int bestChoice = 0;

            for (int i = 1; i < numberClasses; i++) {
                if (y[bestChoice] < y[i]) {
                    bestChoice = i;
                }
            }

            System.out.println(bestChoice + " " + y[bestChoice]);
            
            return;
        }

        double[] y = new double[numberClasses];

        for (int i = 0; i < numberClasses; i++) {
            y[i] = 0.0;
        }
        
        for (TrainingSample ts : kNN) {
            double w = minDistance / ts.distance;

            y[ts.yOriginal] += w * ts.y[ts.yOriginal];
        }
        
        for (int i = 0; i < numberClasses; i++) {
            y[i] = (1.0 / kSize) * y[i];
        }

        int bestChoice = 0;

        for (int i = 1; i < numberClasses; i++) {
            if (y[bestChoice] < y[i]) {
                bestChoice = i;
            }
        }

        System.out.println(bestChoice + " " + y[bestChoice]);
    }

    private List<TrainingSample> getKNearestNeightbors(Sample sample, int k) {
        PriorityQueue<TrainingSample> pq = new PriorityQueue<TrainingSample>();

        for (TrainingSample ts : trainingSamples) {
            ts.calculateDistance(sample);

            pq.add(ts);
        }

        List<TrainingSample> kNearest = new ArrayList<TrainingSample>(k);

        for (int i = 0; i < k && !pq.isEmpty(); i++) {
            kNearest.add(pq.poll());
        }

        return kNearest;
    }

    private Sample parseTest(String line) {
        String[] tokens = line.trim().split(" ");

        int[] x = new int[numberAttributes + 1];

        x[0] = 0;
        
        for (int i = 0; i < tokens.length; i++) {
            x[i + 1] = Integer.parseInt(tokens[i]);
        }

        return new Sample(x);
    }

    private TrainingSample parseTraining(String line) {
        String[] tokens = line.trim().split(" ");

        int[] x = new int[numberAttributes + 1];
        int y = -1;

        x[0] = 0;
        
        for (int i = 0; i < tokens.length; i++) {
            if (i == 0) {
                y = Integer.parseInt(tokens[i]);
            } else {                
                x[i] = Integer.parseInt(tokens[i]);
            }
        }

        return new TrainingSample(x, numberClasses, y);
    }

    private void parseFirstLine(String line) {
        String[] tokens = line.trim().split(", ");

        for (String token : tokens) {
            if (token.contains(FIRST_LINE_ATTRS)) {
                String attrs = token.replaceAll(FIRST_LINE_ATTRS, "");

                numberAttributes = Integer.parseInt(attrs);
            } else if (token.contains(FIRST_LINE_VALUES)) {
                String values = token.replaceAll(FIRST_LINE_VALUES, "");

                numberValues = Integer.parseInt(values);
            } else if (token.contains(FIRST_LINE_CLASSES)) {
                String classes = token.replaceAll(FIRST_LINE_CLASSES, "");

                numberClasses = Integer.parseInt(classes);
            }
        }
        
        thetas = new double[numberClasses][numberAttributes + 1];
        
        for (int i = 0; i < numberClasses; i++) {
            for (int j = 0; j < numberAttributes + 1; j++) {
                thetas[i][j] = 0;
            }
        }
    }

    private void parseArgs(String[] args) {
        if (args.length != 1) {
            System.exit(1);
        }

        if (args[0].equals(ARG_KNN)) {
            algorithm = Algorithm.KNN;
        } else if (args[0].equals(ARG_LINEAR)) {
            algorithm = Algorithm.LINEAR;
        } else if (args[0].equals(ARG_NB)) {
            algorithm = Algorithm.NB;
        }
    }

    public static void main(String[] args) {
        new Program5(args);
    }
}
