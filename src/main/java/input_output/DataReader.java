package input_output;

import data.Sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataReader {

    // Constants
    static final int AMOUNT_OF_CLASSES = 9;
    static final int AMOUNT_OF_SAMPLES = 11;

    // Class variables
    private final String filePath;
    private final String fileExtension;

    /**
     *
     * @param filePath
     * @param fileExtension
     */
    public DataReader(String filePath, String fileExtension) {
        this.filePath = filePath;
        this.fileExtension = fileExtension;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public List<Float> readSampleMeasuresFromFile(String fileName) {
        List<Float> measures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                measures.add(Float.parseFloat(line));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return measures;
    }

    /**
     *
     * @return
     */
    public List<Sample> getDataSet() {
        List<Sample> samples = new ArrayList<>();

        for (int classIndex = 1; classIndex <= AMOUNT_OF_CLASSES; classIndex++) {
            for (int sampleIndex = 1; sampleIndex <= AMOUNT_OF_SAMPLES; sampleIndex++) {
                List<Float> sampleMeasures = readSampleMeasuresFromFile(
                        this.filePath
                                + "S" + String.format("%02d", classIndex)
                                + "N" + String.format("%03d", sampleIndex)
                                + this.fileExtension);
                samples.add(new Sample(sampleMeasures, classIndex));
            }
        }

        return samples;
    }

    /**
     *
     * @param samples
     */
    public static void printMeasures(List<Sample> samples) {
        System.out.println("Affichage du tableau de mesures :");

        int totalAmountOfSamples = AMOUNT_OF_SAMPLES * AMOUNT_OF_CLASSES;
        for (int sampleIndex = 0; sampleIndex < totalAmountOfSamples; sampleIndex++) {
            if (sampleIndex % AMOUNT_OF_SAMPLES == 0) {
                System.out.println("\n- Classe " + (sampleIndex / AMOUNT_OF_SAMPLES + 1) + " :");
            }

            Sample sample = samples.get(sampleIndex);
            for (Float measure : sample.getMeasures()) {
                System.out.print(measure + " ");
            }

            System.out.println(); // Next line when finished printing current sample measures
        }
    }
}
