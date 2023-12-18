package data;

import java.util.List;

/**
 * The {@code Sample} class represents an image sample of the BDshape image set.
 *
 * <p>This class includes methods to access and manipulate the features and label of a sample.
 *
 * @author [Latif Yaya, Kentaro Sauce]
 * @version 1.0
 */
public class Sample {

    /** The list of floating-point values representing the features of the sample. */
    protected List<Float> features;

    /** The label associated with the sample */
    private int label;

    /**
     * Constructs a Sample with the specified features and label number.
     *
     * @param features    A list of Float values representing the features of the sample.
     * @param labelNumber An integer indicating the label number of the desired label associated with the sample.
     */
    public Sample(List<Float> features, int labelNumber) {
        setFeatures(features);
        setLabel(labelNumber);
    }

    /**
     * Constructs a Sample with the specified features while associating it the default label: 0.
     *
     * @param features A list of Float values representing the features of the sample.
     */
    public Sample(List<Float> features) {
        this(features, 0);
    }

    /**
     * Retrieves the list of features associated with this sample.
     *
     * @return A List of Float values representing the features of the sample.
     */
    public List<Float> getFeatures() {
        return features;
    }

    /**
     * Sets the list of features for this sample.
     *
     * @param features A List of Float values representing the features to be set for the sample.
     */
    public void setFeatures(List<Float> features) {
        this.features = features;
    }

    /**
     * Retrieves a feature of this sample from a given index.
     *
     * @param index An integer indicating the index of the feature from this sample.
     * @return A float value representing the desired feature.
     */
    public float getFeature(int index) {
        return getFeatures().get(index);
    }

    /**
     * Retrieves the label associated with this sample.
     *
     * @return An integer representing the label.
     */
    public int getLabel() {
        return this.label;
    }

    /**
     * Sets the label associated with this sample.
     *
     * @param labelNumber An integer representing the label.
     */
    public void setLabel(int labelNumber) {
        this.label = labelNumber;
    }

    /**
     * Checks if the label of this sample is equal to the label of the specified sample.
     *
     * @param sample The sample to compare the label with.
     * @return {@code true} if the labels are equal, {@code false} otherwise.
     */
    public boolean isLabelEqualTo(Sample sample) {
        return this.label == sample.label;
    }

    /**
     * Returns a string representation of this sample.
     * The string includes the features of the sample separated by spaces.
     *
     * @return A string representing the features of the sample.
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
}
