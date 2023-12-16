/**
 * KMeans is a class that implements the K-means clustering algorithm.
 * It is used to partition a dataset into a specified number of clusters.
 */
package classifiers;

import data.Sample;

import java.util.*;

/**
 * The KMeans class represents the K-means clustering algorithm.
 */
public class KMeans {

    /**
     * Number of clusters (K) to form.
     */
    private final int k;

    /**
     * Whether centroids are initialized randomly or using k-means++.
     */
    private final boolean usingPP;

    /**
     * Maximum number of iterations for the algorithm.
     */
    private final int maxIterations;

    /**
     * Order of the Minkowski norm (p) for distance calculation.
     */
    private final int distanceNorm;

    /**
     * The random object to generate random values throughout the algorithm.
     */
    private final Random random;

    /**
     * List of Cluster objects representing the clusters formed by the algorithm.
     */
    private List<Cluster> clusters;

    /**
     * List of Sample objects representing the dataset.
     */
    private final List<Sample> dataSet;

    /**
     * Constructs a KMeans object with the specified parameters.
     *
     * @param k              Number of clusters to form.
     * @param dataSet        List of samples representing the dataset.
     * @param usingPP        Whether to initialize centroids using k-means++.
     * @param maxIterations  Maximum number of iterations for the algorithm.
     * @param distanceNorm   Order of the Minkowski norm for distance calculation.
     * @param randomSeed     Seed for all RNG in the algorithm.
     */
    public KMeans(int k, List<Sample> dataSet, boolean usingPP, int maxIterations, int distanceNorm, int randomSeed) {
        this.k = k;
        this.dataSet = dataSet;
        this.usingPP = usingPP;
        this.maxIterations = maxIterations;
        this.random = new Random(randomSeed);
        this.distanceNorm = distanceNorm;
        this.clusters = new ArrayList<>();
    }

    /**
     * Constructs a KMeans object with the specified parameters.
     *
     * @param k              Number of clusters to form.
     * @param dataSet        List of samples representing the dataset.
     * @param usingPP        Whether to initialize centroids using k-means++.
     * @param maxIterations  Maximum number of iterations for the algorithm.
     * @param distanceNorm   Order of the Minkowski norm for distance calculation.
     */
    public KMeans(int k, List<Sample> dataSet, boolean usingPP, int maxIterations, int distanceNorm) {
        this.k = k;
        this.dataSet = dataSet;
        this.usingPP = usingPP;
        this.maxIterations = maxIterations;
        this.random = new Random();
        this.distanceNorm = distanceNorm;
        this.clusters = new ArrayList<>();
    }

    /**
     * Gets the list of clusters formed by the K-means algorithm.
     *
     * @return List of Cluster objects representing the clusters.
     */
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * The Centroid class represents the centroid of a cluster.
     */
    public record Centroid(List<Float> coordinates) {
        /**
         * Constructs a Centroid object with the specified coordinates.
         *
         * @param coordinates List of coordinates for the centroid.
         */
        public Centroid(List<Float> coordinates) {
            this.coordinates = new ArrayList<>(coordinates);
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
         * Checks if the centroid of the cluster is equal to another centroid.
         *
         * @param centroid2 The centroid to compare.
         * @return True if centroids are equal, false otherwise.
         */
        public boolean isCentroidEqualTo(Centroid centroid2) {
            return this.centroid.equals(centroid2);
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
        public boolean hasNotChanged() {
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
            printClusters(iteration, clusters);
            iteration++;
        } while (iteration < maxIterations && !areAllClustersConverged());
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
     * Prints information about the clusters.
     *
     * @param iteration The current iteration number.
     * @param clusters  List of clusters to print.
     */
    private void printClusters(int iteration, List<Cluster> clusters) {
        System.out.println("Iteration " + iteration + ":");
        for (Cluster cluster : clusters) {
            Centroid centroid = cluster.getCentroid();
            List<Sample> samplesInCluster = cluster.getSamples();

            System.out.print("Cluster - Centroid: " + centroid.coordinates() + " Samples: ");
            for (Sample sample : samplesInCluster) {
                System.out.print(sample.getLabelNumber() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
