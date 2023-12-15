package classifiers;

import data.Sample;

import java.util.*;

/**
 *
 */
public class KMeans {

    private static final Random random = new Random();

    public static class Centroid {
        private List<Float> coordinates;

        public Centroid(List<Float> coordinates) {
            setCoordinates(coordinates);
        }

        public List<Float> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Float> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static Map<Centroid, List<Sample>> getClustersOfSamples
            (List<Sample> samples, int k, int p, int maxIterations) {

        List<Centroid> centroids = getRandomCentroids(samples, k);

        Map<Centroid, List<Sample>> clusters = new HashMap<>();
        Map<Centroid, List<Sample>> previousClustersState = new HashMap<>();

        // iterate for a pre-defined number of times
        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = (i == maxIterations - 1);

            // in each iteration, we should find the nearest centroid for each sample
            for (Sample sample : samples) {
                Centroid centroid = nearestCentroid(sample, centroids, p);
                assignSampleToNewCluster(clusters, sample, centroid);
            }

            // if the assignments do not change, then the algorithm terminates
            boolean shouldTerminate = isLastIteration || clusters.equals(previousClustersState);
            previousClustersState = clusters;
            if (shouldTerminate) {
                break;
            }

            // at the end of each iteration, we should relocate the centroids
            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        return previousClustersState;
    }

    private static List<Map.Entry<Float, Float>> getPairsOfMinimumAndMaximumAttributes(List<Sample> samples) {
        List<Map.Entry<Float, Float>> minimumAndMaximumAttributes = new ArrayList<>();

        int totalSize = samples.getFirst().getFeatures().size();

        for (int attributeIndex = 0; attributeIndex < totalSize; attributeIndex++) {
            List<Float> currentAttributes = new ArrayList<>();

            for (Sample sample : samples) {
                currentAttributes.add(sample.getFeature(attributeIndex));
            }

            float minimumAttribute = Collections.min(currentAttributes);
            float maximumAttribute = Collections.max(currentAttributes);

            Map.Entry<Float, Float> attributePair = new AbstractMap.SimpleImmutableEntry<>
                    (minimumAttribute,maximumAttribute);
            minimumAndMaximumAttributes.add(attributePair);
        }

        return minimumAndMaximumAttributes;
    }

    private static List<Centroid> getRandomCentroids(List<Sample> trainingSet, int k) {
        List<Centroid> centroids = new ArrayList<>();

        List<Map.Entry<Float, Float>> minimumAndMaximumAttributes
                = getPairsOfMinimumAndMaximumAttributes(trainingSet);

        for (int clusterIndex = 0; clusterIndex < k; clusterIndex++) {
            List<Float> centroidCoordinates = new ArrayList<>();

            for (Map.Entry<Float, Float> attributePair : minimumAndMaximumAttributes) {
                float minimum = attributePair.getKey();
                float maximum = attributePair.getValue();

                float randomCoordinate = minimum + random.nextFloat() * (maximum - minimum);
                centroidCoordinates.add(randomCoordinate);
            }

            centroids.add(new Centroid(centroidCoordinates));
        }

        return centroids;
    }

    private static Centroid nearestCentroid(Sample sample, List<Centroid> centroids, int p) {
        Centroid nearestCentroid = null;
        float minimumDistance = Float.MAX_VALUE;

        for (Centroid centroid : centroids) {
            float currentDistance = ClassifierUtilities.calculateMinkowskiDistance
                    (sample.getFeatures(), centroid.getCoordinates(), p);

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearestCentroid = centroid;
            }
        }

        return nearestCentroid;
    }

    private static void assignSampleToNewCluster
            (Map<Centroid, List<Sample>> clusters, Sample sample, Centroid centroid) {

        clusters.computeIfAbsent(centroid, k -> new ArrayList<>());
        clusters.get(centroid).add(sample);
    }

    private static Centroid relocateCentroidToCenterOfCluster(Centroid centroid, List<Sample> samples) {
        if (samples.isEmpty()) {
            return centroid;
        }

        List<Float> averageCoordinates = new ArrayList<>();

        int totalSize = samples.getFirst().getFeatures().size();
        for (int featureIndex = 0; featureIndex < totalSize; featureIndex++) {
            float sum = 0.0f;

            for (Sample sample : samples) {
                sum += sample.getFeature(featureIndex);
            }

            float attributeAverage = sum / samples.size();
            averageCoordinates.add(attributeAverage);
        }

        return new Centroid(averageCoordinates);
    }

    private static List<Centroid> relocateCentroids(Map<Centroid, List<Sample>> clusters) {
        List<Centroid> relocatedCentroids = new ArrayList<>();

        for (Map.Entry<Centroid, List<Sample>> cluster : clusters.entrySet()) {
            Centroid relocatedCentroid = relocateCentroidToCenterOfCluster
                    (cluster.getKey(), cluster.getValue());
            relocatedCentroids.add(relocatedCentroid);
        }

        return relocatedCentroids;
    }
}
