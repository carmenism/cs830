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

    private List<TrainingSample> trainingSamples = new ArrayList<TrainingSample>();

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
                    TrainingSample sample = parseTraining(line);

                    trainingSamples.add(sample);
                } else {
                    Sample sample = parseTest(line);
                    sample.normalize(averages, standardDeviations);

                    classify(sample);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void normalize() {
        averages = new double[numberAttributes];
        standardDeviations = new double[numberAttributes];

        int N = trainingSamples.size();

        for (int i = 0; i < numberAttributes; i++) {
            averages[i] = 0.0;

            for (TrainingSample sample : trainingSamples) {
                averages[i] += sample.x[i];
            }

            averages[i] = averages[i] / N;
        }

        for (int i = 0; i < numberAttributes; i++) {
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

    private void classify(Sample sample) {
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

        int[] x = new int[numberAttributes];

        for (int i = 0; i < tokens.length; i++) {
            x[i] = Integer.parseInt(tokens[i]);
        }

        return new Sample(numberAttributes, x);
    }

    private TrainingSample parseTraining(String line) {
        String[] tokens = line.trim().split(" ");

        int[] x = new int[numberAttributes];
        int y = -1;

        for (int i = 0; i < tokens.length; i++) {
            if (i == 0) {
                y = Integer.parseInt(tokens[i]);
            } else {                
                x[i - 1] = Integer.parseInt(tokens[i]);
            }
        }

        return new TrainingSample(numberAttributes, x, numberClasses, y);
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
