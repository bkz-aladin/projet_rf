package data;

import java.util.List;

/**
 *
 */
public class Sample {
    private List<Float> measures;
    private LabelName label;

    private enum LabelName {
        UNLABELED, POISSON, LAPIN, SILHOUETTE, AVION, MAIN, OUTIL, BIDULE, ANIMAL, RAIE
    }

    /**
     *
     * @param measures
     * @param labelNumber
     */
    public Sample(List<Float> measures, int labelNumber) {
        setMeasures(measures);
        setLabel(labelNumber);
    }

    /**
     *
     * @param measures
     */
    public Sample(List<Float> measures) {
        this(measures, 0);
    }

    /**
     *
     * @return
     */
    public List<Float> getMeasures() {
        return measures;
    }

    /**
     *
     * @param measures
     */
    public void setMeasures(List<Float> measures) {
        this.measures = measures;
    }

    /**
     *
     * @return
     */
    public int getLabelNumber() {
        return this.label.ordinal();
    }

    /**
     *
     * @param labelNumber
     */
    public void setLabel(int labelNumber) {
        this.label = LabelName.values()[labelNumber];
    }

    /**
     *
     * @param sample
     * @return
     */
    public boolean isLabelEqualTo(Sample sample) {
        return this.label == sample.label;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        List<Float> measures = getMeasures();
        StringBuilder stringBuilder = new StringBuilder(measures.getFirst().toString());
        for (Float measure : measures) {
            if (measure.equals(measures.getFirst())) {
                continue;
            }
            stringBuilder.append(" ").append(measure);
        }

        return stringBuilder.toString();
    }

    /**
     *
     * @param sample2
     * @param p
     * @return
     */
    public float calculateMinkowskiDistance(Sample sample2, int p) {
        int sample1Dimensions = this.getMeasures().size();
        int sample2Dimensions = sample2.getMeasures().size();
        if (sample1Dimensions != sample2Dimensions) {
            throw new IllegalArgumentException("Les deux échantillons doivent être de même dimension.");
        }

        float sum = 0.0f;
        float coordinate1, coordinate2;
        for (int dimension = 1; dimension <= sample2Dimensions; dimension++) {
            coordinate1 = this.getMeasures().get(dimension);
            coordinate2 = sample2.getMeasures().get(dimension);
            sum += (float) Math.pow(Math.abs(coordinate1 - coordinate2), p);
        }

        return (float) Math.pow(sum, 1.0 / p);
    }
}
