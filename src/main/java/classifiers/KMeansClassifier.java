package classifiers;

import data.Sample;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@code KMeansClassifier} class implements the K-means clustering algorithm to create clusters of similar samples.
 * It is used to partition a dataset into a specified number of clusters.
 *
 * <p>Although KMeans is an unsupervised algorithm, we can still use it as a classifier with a labeled dataset.
 *
 * @author [Latif Yaya, Kentaro Sauce]
 * @version 1.0
 */
public class KMeansClassifier {

    /** Number of clusters (K) to form. */
    private final int k;

    /** Whether centroids are initialized randomly or using the k-means++ strategy. */
    private final boolean usingPP;

    /** Maximum number of iterations for the algorithm. */
    private final int maxIterations;

    /** Order of the Minkowski norm (p) for distance calculation. */
    private final int distanceNorm;

    /** The random object to generate random values throughout the algorithm. */
    private final Random random;

    /** List of Cluster objects representing the clusters formed by the algorithm. */
    private List<Cluster> clusters;

    /** List of Sample objects representing the dataset. */
    private final List<Sample> dataSet;

    /** List of Float values representing the precision values for each class. */
    public List<Float> precisionValues;

    /** List of Float values representing the recall values for each class. */
    public List<Float> recallValues;

    /** List of Float values representing the F1 scores for each class. */
    public List<Float> f1Scores;

    /** Float value representing the macro F1 score for the classifier model. */
    public float macroF1Score;

    /** Int value representing the number of iterations for the algorithm to fully converge. */
    public int iterations;

    /** 2D array of int values representing the confusion matrix of the classification. */
    public int[][] confusionMatrix;

    /**
     * Constructs a KMeansClassifier object with the specified parameters.
     *
     * @param k              Number of clusters to form.
     * @param dataSet        List of samples representing the dataset.
     * @param usingPP        Whether to initialize centroids using k-means++.
     * @param maxIterations  Maximum number of iterations for the algorithm.
     * @param distanceNorm   Order of the Minkowski norm for distance calculation.
     * @param randomSeed     Seed for all RNG in the algorithm.
     */
    public KMeansClassifier(int k, List<Sample> dataSet, boolean usingPP, int maxIterations, int distanceNorm, int randomSeed) {
        this.k = k;
        this.dataSet = dataSet;
        this.usingPP = usingPP;
        this.maxIterations = maxIterations;
        this.random = new Random(randomSeed);
        this.distanceNorm = distanceNorm;
        this.clusters = new ArrayList<>();
        this.precisionValues = new ArrayList<>();
        this.f1Scores = new ArrayList<>();
        this.macroF1Score = 0f;
        this.recallValues = new ArrayList<>();
        this.iterations = 0;
        this.confusionMatrix = new int[9][9];
    }

    /**
     * Constructs a KMeansClassifier object with the specified parameters.
     *
     * @param k              Number of clusters to form.
     * @param dataSet        List of samples representing the dataset.
     * @param usingPP        Whether to initialize centroids using k-means++.
     * @param maxIterations  Maximum number of iterations for the algorithm.
     * @param distanceNorm   Order of the Minkowski norm for distance calculation.
     */
    public KMeansClassifier(int k, List<Sample> dataSet, boolean usingPP, int maxIterations, int distanceNorm) {
        this.k = k;
        this.dataSet = dataSet;
        this.usingPP = usingPP;
        this.maxIterations = maxIterations;
        this.random = new Random();
        this.distanceNorm = distanceNorm;
        this.clusters = new ArrayList<>();
        this.precisionValues = new ArrayList<>();
        this.f1Scores = new ArrayList<>();
        this.macroF1Score = 0f;
        this.recallValues = new ArrayList<>();
        this.iterations = 0;
        this.confusionMatrix = new int[9][9];
    }

    /**
     * The Centroid class represents the centroid of a cluster.
     */
    public static class Centroid extends Sample {

        /** Represents the coordinates of the centroid. */
        private final List<Float> coordinates;

        /**
         * Constructs a Centroid object with the specified coordinates.
         *
         * @param coordinates List of coordinates for the centroid.
         */
        public Centroid(List<Float> coordinates) {
            super(coordinates);
            this.coordinates = super.getFeatures();
        }
    }

    /**
     * The Cluster class represents a cluster formed by the K-means algorithm.
     */
    public static class Cluster {

        /**
         * Represents the center of a cluster.
         */
        private Centroid centroid;

        /**
         * List of the samples contained in the cluster.
         */
        private final List<Sample> samples;

        /**
         * Represents the center point of the cluster.
         * It is given by the computing the mean coordinates of all samples belonging to the cluster.
         * Is used as the coordinates of the new {@code centroid}.
         */
        private List<Float> centerPoint;

        /**
         * Represents the {@code centerPoint} of the last state of the cluster.
         */
        private List<Float> previousCenterPoint;

        /**
         * Constructs a Cluster object with the specified centroid.
         *
         * @param centroid The centroid of the cluster.
         */
        public Cluster(Centroid centroid) {
            this.centroid = centroid;
            this.samples = new ArrayList<>();
            this.centerPoint = new ArrayList<>();
            this.previousCenterPoint = new ArrayList<>();
        }

        /**
         * Gets the centroid of the cluster.
         *
         * @return The centroid.
         */
        public Centroid getCentroid() {
            return centroid;
        }

        /**
         * Sets the centroid of the cluster.
         *
         * @param centroid The new centroid.
         */
        public void setCentroid(Centroid centroid) {
            this.centroid = centroid;
        }

        /**
         * Gets the list of samples in the cluster.
         *
         * @return The list of samples.
         */
        public List<Sample> getSamples() {
            return samples;
        }

        /**
         * Adds a sample to the cluster.
         *
         * @param sample The sample to add.
         */
        public void addSample(Sample sample) {
            samples.add(sample);
        }

        /**
         * Gets the attribute means of the cluster.
         *
         * @return The list of attribute means.
         */
        public List<Float> getCenterPoint() {
            return centerPoint;
        }

        /**
         * Computes the center point of the cluster by averaging each coordinate of every sample in the cluster.
         */
        public void computeClusterCenter() {
            if (samples.isEmpty()) {
                return;
            }

            previousCenterPoint = new ArrayList<>(centerPoint);
            centerPoint = new ArrayList<>();

            for (int dimension = 1; dimension <= samples.getFirst().getFeatures().size(); dimension++) {
                float sum = 0.0f;
                for (Sample sample : samples) {
                    sum += sample.getFeature(dimension - 1);
                }

                float currentMean = sum / samples.size();
                centerPoint.add(currentMean);
            }
        }

        /**
         * Checks if the cluster's attribute means have converged.
         *
         * @return True if attribute means have converged, false otherwise.
         */
        private boolean hasNotChanged() {
            return centerPoint.equals(previousCenterPoint);
        }
    }

    /**
     * Runs the K-means clustering algorithm.
     */
    public void runKMeans() {
        initializeClusters();
        int iteration = 0;

        do {
            assignSamplesToClusters();
            updateCentroids();
            iteration++;
        } while (iteration < maxIterations && !areAllClustersConverged());

        this.iterations = --iteration;

        computeConfusionMatrix();
        computeEvaluationScores();
    }

    /**
     * Initializes the clusters based on the initialization strategy.
     */
    private void initializeClusters() {
        clusters = new ArrayList<>();

        if (usingPP) {
            initializeClustersKMeansPlusPlus();
        } else {
            initializeClustersRandom();
        }
    }

    /**
     * Initializes clusters randomly by selecting k random samples as initial centroids.
     */
    private void initializeClustersRandom() {
        List<Sample> randomSamples = new ArrayList<>(dataSet);
        Collections.shuffle(randomSamples, random);

        for (int i = 0; i < k; i++) {
            Centroid centroid = new Centroid(randomSamples.get(i).getFeatures());
            clusters.add(new Cluster(centroid));
        }
    }

    /**
     * Initializes clusters using the k-means++ strategy.
     */
    private void initializeClustersKMeansPlusPlus() {
        int randomIndex = random.nextInt(dataSet.size());
        Centroid firstCentroid = new Centroid(dataSet.get(randomIndex).getFeatures());
        clusters.add(new Cluster(firstCentroid));

        for (int i = 1; i < k; i++) {
            List<Double> distances = calculateDistancesToNearestCentroids();
            double totalDistance = distances.stream().mapToDouble(Double::doubleValue).sum();
            double randomValue = random.nextDouble() * totalDistance;
            addClusterBasedOnProbability(distances, randomValue);
        }
    }

    /**
     * Calculates distances from each sample to its nearest cluster centroid.
     * Used by {@code initializeClustersKMeansPlusPlus()}.
     *
     * @return List of distances to nearest centroids.
     */
    private List<Double> calculateDistancesToNearestCentroids() {
        List<Double> distances = new ArrayList<>();
        for (Sample sample : dataSet) {
            double minDistance = calculateMinimumDistanceToClusters(sample);
            distances.add(minDistance);
        }
        return distances;
    }

    /**
     * Calculates the minimum distance from a sample to the existing cluster centroids.
     * Used by {@code calculateDistancesToNearestCentroids()} for {@code initializeClustersKMeansPlusPlus()}.
     *
     * @param sample The sample to calculate distances for.
     * @return Minimum distance to existing centroids.
     */
    private double calculateMinimumDistanceToClusters(Sample sample) {
        // The distance needs to be in L2 norm here (p = 2).
        return clusters.stream()
                .mapToDouble(cluster -> ClassifierUtilities.calculateMinkowskiDistance
                        (sample.getFeatures(), cluster.getCentroid().coordinates, 2))
                .min()
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Adds a cluster based on a probability distribution determined by distances.
     *
     * @param distances      List of distances to nearest centroids.
     * @param randomValue    Random value used for probability calculation.
     */
    private void addClusterBasedOnProbability(List<Double> distances, double randomValue) {
        double cumulativeProbability = 0.0;

        for (int j = 0; j < dataSet.size(); j++) {
            cumulativeProbability += distances.get(j);
            if (cumulativeProbability >= randomValue) {
                clusters.add(new Cluster(new Centroid(dataSet.get(j).getFeatures())));
                break;
            }
        }
    }

    /**
     * Assigns each sample to its nearest cluster.
     */
    private void assignSamplesToClusters() {
        for (Cluster cluster : clusters) {
            cluster.getSamples().clear();
        }

        for (Sample sample : dataSet) {
            Cluster nearestCluster = findNearestCluster(sample);
            nearestCluster.addSample(sample);
        }
    }

    /**
     * Finds the nearest cluster for a given sample.
     *
     * @param sample The sample to find the nearest cluster for.
     * @return The nearest cluster.
     */
    private Cluster findNearestCluster(Sample sample) {
        return clusters.stream()
                .min(Comparator.comparingDouble(cluster ->
                        ClassifierUtilities.calculateMinkowskiDistance
                                (sample.getFeatures(), cluster.getCentroid().coordinates, distanceNorm)))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Updates the centroids of all clusters.
     */
    private void updateCentroids() {
        for (Cluster cluster : clusters) {
            cluster.computeClusterCenter();
            if (!cluster.getCenterPoint().isEmpty()) {
                Centroid newCentroid = new Centroid(cluster.getCenterPoint());
                cluster.setCentroid(newCentroid);
            }
        }
    }

    /**
     * Checks if all clusters have converged to their last state.
     *
     * @return True if all clusters have converged, false otherwise.
     */
    private boolean areAllClustersConverged() {
        return clusters.stream().allMatch(Cluster::hasNotChanged);
    }

    /**
     * Prints information about the clusters including the centroid coordinates
     * and the labels of the samples corresponding to them.
     *
     * @param iteration The current iteration number.
     * @param clusters  List of clusters to print.
     */
    public void printClusters(int iteration, List<Cluster> clusters) {
        System.out.println("Iteration " + iteration + ":");
        for (Cluster cluster : clusters) {
            Centroid centroid = cluster.getCentroid();
            List<Sample> samplesInCluster = cluster.getSamples();

            System.out.print("Cluster - Centroid: " + centroid.coordinates + " Samples: ");
            for (Sample sample : samplesInCluster) {
                System.out.print(sample.getLabelNumber() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints the label of each sample corresponding to the clusters.
     *
     * @param iteration The current iteration number.
     * @param clusters  List of clusters to print.
     */
    public void printClusterLabels(int iteration, List<Cluster> clusters) {
        System.out.println(getMethodName() + " - Iteration " + iteration + ":");
        int clusterNumber = 1;
        for (Cluster cluster : clusters) {
            List<Sample> samplesInCluster = cluster.getSamples();

            System.out.print("Cluster " + clusterNumber + " - Samples: ");
            for (Sample sample : samplesInCluster) {
                System.out.print(sample.getLabelNumber() + " ");
            }
            clusterNumber++;
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Gets the method name used to generate the current dataset based on the number of features in the dataset.
     *
     * @return A string representing the method name.
     */
    private String getMethodName() {
        final int amountOfFeatures = dataSet.getFirst().getFeatures().size();
        if (amountOfFeatures == 16) return "E34";
        if (amountOfFeatures == 100) return "GFD";
        if (amountOfFeatures == 90) return "SA";
        return "F0";
    }

    /**
     * Assigns cluster labels based on the most frequent label in each cluster.
     */
    public void assignClusterLabels() {
//        List<Map<Integer, Long>> labelOccurrencesInEachCluster = new ArrayList<>();
//        for (Cluster cluster : clusters) {
//            List<Integer> labels = cluster.samples.stream().map(Sample::getLabelNumber).toList();
//            Map<Integer, Long> labelOccurences = labels.stream()
//                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//            labelOccurrencesInEachCluster.add(labelOccurences);
//            for (int i = 1; i < 10; i++) {
//                if (!labelOccurences.containsKey(i)) labelOccurences.put(i, 0L);
//            }
//        }
//        // TODO : find a better way to assign labels by comparing proportion of label in each cluster.

        for (Cluster cluster : clusters) {
            List<Integer> labels = cluster.samples.stream().map(Sample::getLabelNumber).toList();
            Map<Integer, Long> labelOccurences = labels.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            if (!cluster.samples.isEmpty()) {
                cluster.getCentroid().setLabel
                        (Collections.max(labelOccurences.entrySet(), Map.Entry.comparingByValue()).getKey());
            } else {
                cluster.getCentroid().setLabel(0);
            }
        }
    }

    /**
     * Computes the confusion matrix for the classification results after assigning labels to the clusters.
     */
    private void computeConfusionMatrix() {
        int numClasses = 9;
        int[][] matrix = new int[numClasses][numClasses];

        assignClusterLabels();

        for (Sample sample : dataSet) {
            int predictedLabel = 0;
            for (Cluster cluster : clusters) {
                if (cluster.getSamples().contains(sample)) {
                    predictedLabel = cluster.getCentroid().getLabelNumber() - 1;
                }
            }

            int actualLabel = sample.getLabelNumber() - 1;

            // Vérifier que les étiquettes sont valides avant d'accéder à la matrice
            if (actualLabel >= 0 && actualLabel < numClasses && predictedLabel >= 0 && predictedLabel < numClasses) {
                matrix[actualLabel][predictedLabel]++;
            } else {
                throw new RuntimeException
                        ("Étiquette invalide : actual=" + (actualLabel + 1) + ", predicted=" + (predictedLabel + 1));
            }
        }

        this.confusionMatrix = matrix;
    }

    /**
     * Computes precision, recall, and F1 scores for each class and calculates the macro F1 score of the model.
     */
    public void computeEvaluationScores() {
        for (int i = 0; i < 9; i++) {
            float truePositives = confusionMatrix[i][i];
            float falsePositives = 0;
            float falseNegatives = 0;

            for (int j = 0; j < 9; j++) {
                if (i != j) {
                    falsePositives += confusionMatrix[i][j];
                    falseNegatives += confusionMatrix[j][i];
                }
            }

            // Calculate precision value for the current class
            float precisionValue;
            if (truePositives + falsePositives == 0.0f) {
                precisionValue = 0.0f;
            } else {
                precisionValue = truePositives / (truePositives + falsePositives);
            }

            // Calculate recall value for the current class
            float recallValue;
            if (truePositives + falseNegatives == 0.0f) {
                recallValue = 0.0f;
            } else {
                recallValue = truePositives / (truePositives + falseNegatives);
            }

            precisionValues.add(precisionValue);
            recallValues.add(recallValue);

            // Calculate F1 score for the current class
            float f1Score;
            if (precisionValue + recallValue == 0.0f) {
                f1Score = 0.0f;
            } else {
                f1Score = 2 * (precisionValue * recallValue) / (precisionValue + recallValue);
            }

            f1Scores.add(f1Score);
        }

        // Calculate macro F1 score for the model
        macroF1Score = (float) f1Scores.stream().mapToDouble(a -> a).average().orElse(0);
    }

    /**
     * Prints the evaluation metrics for each class and the macro F1 score in % format,
     * before ultimately printing the confusion matrix.
     * 'P' corresponds to the precision value, 'R' corresponds to the recall value and 'F' corresponds to the F1 score.
     */
    public void printEvaluations() {
        System.out.println("\n- " + getMethodName());

        // Print the precision, recall and f1 score values in % for every class.
        for (int classIndex = 0; classIndex < 9; classIndex++) {
            String formattedLine = String.format("Classe %d: \tP = %.2f%%\tR = %.2f%%\tF = %.2f%%",
                    classIndex + 1, precisionValues.get(classIndex) * 100,
                    recallValues.get(classIndex) * 100, f1Scores.get(classIndex) * 100);
            System.out.println(formattedLine);
        }

        // Print the macro F1 score of the model.
        String macroF1Score = String.format("%.2f%%", this.macroF1Score * 100);
        System.out.println("Macro F1 Score: " + macroF1Score);
        System.out.println();

        // Print the confusion matrix.
        ClassifierUtilities.printConfusionMatrix(confusionMatrix);
    }
}
