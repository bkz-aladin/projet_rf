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
    public Boolean isLabelEqualTo(Sample sample) {
        return this.label == sample.label;
    }
}
