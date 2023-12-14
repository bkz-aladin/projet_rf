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
            (List<Sample> trainingSet, int k, int p, int maxIterations) {
        List<Centroid> centroids = getRandomCentroids(trainingSet, k);
        Map<Centroid, List<Sample>> clusters = new HashMap<>();

        for (int i = 0; i < maxIterations; i++) {

        }

        return null;
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

        List<Float> centroidCoordinates = new ArrayList<>();

        for (int clusterIndex = 0; clusterIndex < k; clusterIndex++) {
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
        for (Map.Entry<Centroid, List<Sample>> cluster : clusters.entrySet()) {
            cluster.getValue().remove(sample);
        }

        clusters.get(centroid).add(sample);
    }

    private static void relocateCentroidToCenterOfCluster(Centroid centroid, List<Sample> samples) {
        if (samples.isEmpty()) {
            return;
        }

        List<Float> averageCoordinates = centroid.getCoordinates();

        for (int featureIndex = 0; featureIndex < averageCoordinates.size(); featureIndex++) {
            float sum = 0.0f;

            for (Sample sample : samples) {
                sum += sample.getFeature(featureIndex);
            }

            float attributeAverage = sum / samples.size();
            averageCoordinates.add(attributeAverage);
        }

        centroid.setCoordinates(averageCoordinates);
    }
}
