package input_output;

import data.Sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DataReader} class is responsible for reading sample measures from files and creating a dataset.
 * It provides methods to read features from files, retrieve the dataset, and print features.
 *
 * <p>The class uses constants for the number of classes and samples, and it requires a file path and extension
 * for reading data from files.
 * It utilizes the {@code Sample} class to represent the image samples which will constitute the dataset.
 *
 * @author [Latif Yaya, Kentaro Sauce]
 * @version 1.0
 */
public class DataReader {

    /** The constant AMOUNT_OF_CLASSES representing the number of image classes in the dataset. */
    static final int AMOUNT_OF_CLASSES = 9;

    /** The constant AMOUNT_OF_SAMPLES representing the number of samples in each image class. */
    static final int AMOUNT_OF_SAMPLES = 11;

    /** The file path to the sample data. */
    private final String filePath;

    /** The file extension representing the applied image analysis method. For example, ".E34", ".F0"... */
    private final String fileExtension;

    /**
     * Instantiates a new Data reader with the specified file path and extension.
     *
     * @param filePath      The file path to the sample data.
     * @param fileExtension The file extension representing the applied image analysis method.
     */
    public DataReader(String filePath, String fileExtension) {
        this.filePath = filePath;
        this.fileExtension = fileExtension;
    }

    /**
     * Reads sample measures from the specified file.
     *
     * @param fileName The name of the file to read from.
     * @return A List of Float values representing the features read from the file.
     */
    public List<Float> readFeaturesFromFile(String fileName) {
        List<Float> features = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                features.add(Float.parseFloat(line));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return features;
    }

    /**
     * Retrieves the dataset by reading features from files and creating the corresponding Sample objects.
     *
     * @return A List of Sample objects representing the dataset.
     * @see Sample
     */
    public List<Sample> getDataSet() {
        List<Sample> samples = new ArrayList<>();

        for (int classIndex = 1; classIndex <= AMOUNT_OF_CLASSES; classIndex++) {
            for (int sampleIndex = 1; sampleIndex <= AMOUNT_OF_SAMPLES; sampleIndex++) {
                List<Float> features = readFeaturesFromFile(
                        this.filePath
                                + "S" + String.format("%02d", classIndex)
                                + "N" + String.format("%03d", sampleIndex)
                                + this.fileExtension);
                samples.add(new Sample(features, classIndex));
            }
        }

        return samples;
    }

    /**
     * Prints the features of the given samples grouped into their corresponding image classes.
     *
     * @param samples The List of Sample objects whose features need to be printed.
     * @see Sample
     */
    public static void printFeatures(List<Sample> samples) {
        System.out.println("Affichage du tableau de mesures :");

        int totalAmountOfSamples = AMOUNT_OF_SAMPLES * AMOUNT_OF_CLASSES;
        for (int sampleIndex = 0; sampleIndex < totalAmountOfSamples; sampleIndex++) {
            if (sampleIndex % AMOUNT_OF_SAMPLES == 0) {
                System.out.println("\n- Class " + (sampleIndex / AMOUNT_OF_SAMPLES + 1) + ":");
            }

            Sample sample = samples.get(sampleIndex);
            System.out.println(sample.toString());
        }
    }
}
