package data;

import java.util.List;

/**
 * The {@code Pattern} class represents TODO:
 *
 * <p>This class includes methods to access and manipulate the features and label of a pattern.
 *
 * @author [Latif Yaya, Kentaro Sauce]
 * @version 1.0
 */
public class Pattern {

    /** The list of floating-point values representing the features of the pattern. */
    private List<Float> features;

    /**
     * Enum representing possible label names associated with a pattern.
     * Labels include UNLABELED, POISSON, LAPIN, SILHOUETTE, AVION, MAIN, OUTIL, BIDULE, ANIMAL, and RAIE.
     */
    private enum LabelName {
        UNLABELED,
        POISSON,
        LAPIN,
        SILHOUETTE,
        AVION,
        MAIN,
        OUTIL,
        BIDULE,
        ANIMAL,
        RAIE
    }

    /** The label associated with the pattern, represented by a LabelName enum. */
    private LabelName label;

    /**
     * Constructs a Pattern with the specified features and label number.
     * The label number corresponds to the index of the desired label inside the LabelName constant group:
     * 1 -> POISSON, 2 -> LAPIN, 3 -> SILHOUETTE, 4 -> AVION, 5 -> MAIN, 6 -> OUTIL,
     * 7 -> BIDULE, 8 -> ANIMAL, 9 -> RAIE, 0 -> UNLABELED.
     *
     * @param features    A list of Float values representing the features of the pattern.
     * @param labelNumber An integer indicating the label number of the desired label associated with the pattern.
     */
    public Pattern(List<Float> features, int labelNumber) {
        setFeatures(features);
        setLabel(labelNumber);
    }

    /**
     * Constructs a Pattern with the specified features while associating it the default label: UNLABELED.
     *
     * @param features A list of Float values representing the features of the pattern.
     */
    public Pattern(List<Float> features) {
        this(features, 0);
    }

    /**
     * Retrieves the list of features associated with this pattern.
     *
     * @return A List of Float values representing the features of the pattern.
     */
    public List<Float> getFeatures() {
        return features;
    }

    /**
     * Sets the list of features for this pattern.
     *
     * @param features A List of Float values representing the features to be set for the pattern.
     */
    public void setFeatures(List<Float> features) {
        this.features = features;
    }

    /**
     * Retrieves a feature of this pattern from a given index.
     *
     * @param index An integer indicating the index of the feature from this pattern.
     * @return A float value representing the desired feature.
     */
    public float getFeature(int index) {
        return getFeatures().get(index);
    }

    /**
     * Retrieves the label number of the label associated with this pattern.
     * The label number corresponds to the index of the desired label inside the LabelName constant group:
     * 1 -> POISSON, 2 -> LAPIN, 3 -> SILHOUETTE, 4 -> AVION, 5 -> MAIN, 6 -> OUTIL,
     * 7 -> BIDULE, 8 -> ANIMAL, 9 -> RAIE, 0 -> UNLABELED.
     *
     * @return An integer representing the index of the label.
     */
    public int getLabelNumber() {
        return this.label.ordinal();
    }

    /**
     * Sets the label associated with this pattern from the label number.
     * The label number corresponds to the index of the desired label inside the LabelName constant group:
     * 1 -> POISSON, 2 -> LAPIN, 3 -> SILHOUETTE, 4 -> AVION, 5 -> MAIN, 6 -> OUTIL,
     * 7 -> BIDULE, 8 -> ANIMAL, 9 -> RAIE, 0 -> UNLABELED.
     *
     * @param labelNumber An integer representing the index of the label.
     */
    public void setLabel(int labelNumber) {
        this.label = LabelName.values()[labelNumber];
    }

    /**
     * Checks if the label of this pattern is equal to the label of the specified pattern.
     *
     * @param pattern The pattern to compare the label with.
     * @return {@code true} if the labels are equal, {@code false} otherwise.
     */
    public boolean isLabelEqualTo(Pattern pattern) {
        return this.label == pattern.label;
    }

    /**
     * Returns a string representation of this pattern.
     * The string includes the features of the pattern separated by spaces.
     *
     * @return A string representing the features of the pattern.
     */
    @Override
    public String toString() {
        List<Float> features = getFeatures();
        StringBuilder stringBuilder = new StringBuilder();
        for (Float feature : features) {
            if (!feature.equals(features.getFirst())) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(feature);
        }

        return stringBuilder.toString();
    }

    /**
     * Calculates the Minkowski distance (Lp norm) between this pattern and another pattern.
     * The Minkowski metric is a general class of metrics for d-dimensional patterns.
     * It includes the Euclidean distance (p=2) and the Manhattan distance (p=1).
     *
     * @param pattern2 The second pattern to calculate the distance to.
     * @param p The order of the Minkowski norm, influencing the calculation.
     * @return The Minkowski distance between the two patterns.
     * @throws IllegalArgumentException If p is less than 1.
     * @throws IllegalArgumentException If the dimensions of the patterns are not equal.
     */
    public float calculateMinkowskiDistance(Pattern pattern2, int p) {
        int dimensionsOfPattern1 = this.getFeatures().size();
        int dimensionsOfPattern2 = pattern2.getFeatures().size();
        if (dimensionsOfPattern1 != dimensionsOfPattern2) {
            throw new IllegalArgumentException("The two patterns must be of equal dimensions.");
        }
        if (p < 1) {
            throw new IllegalArgumentException("The order of the Minkowski norm p must be greater or equal to 1.");
        }

        float sum = 0.0f;
        float coordinate1, coordinate2;
        for (int dimension = 1; dimension <= dimensionsOfPattern2; dimension++) {
            coordinate1 = this.getFeature(dimension - 1);
            coordinate2 = pattern2.getFeature(dimension - 1);
            sum += (float) Math.pow(Math.abs(coordinate1 - coordinate2), p);
        }

        return (float) Math.pow(sum, 1.0 / p);
    }
}
