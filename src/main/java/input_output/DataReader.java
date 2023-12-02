package input_output;

import data.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DataReader} class is responsible for reading pattern measures from files and creating a dataset.
 * It provides methods to read features from files, retrieve the dataset, and print features.
 *
 * <p>The class uses constants for the number of classes and patterns, and it requires a file path and extension
 * for reading data from files.
 * It utilizes the {@code Pattern} class to represent the image patterns which will constitute the dataset.
 *
 * @author [Latif Yaya, Kentaro Sauce]
 * @version 1.0
 */
public class DataReader {

    /** The constant AMOUNT_OF_CLASSES representing the number of classes in the dataset. */
    static final int AMOUNT_OF_CLASSES = 9;

    /** The constant AMOUNT_OF_PATTERNS representing the number of patterns in each class. */
    static final int AMOUNT_OF_PATTERNS = 11;

    /** The file path to the pattern data. */
    private final String filePath;

    /** The file extension representing the applied image analysis method. For example, ".E34", ".F0"... */
    private final String fileExtension;

    /**
     * Instantiates a new Data reader with the specified file path and extension.
     *
     * @param filePath      The file path to the pattern data.
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
     * Retrieves the dataset by reading features from files and creating the corresponding Pattern objects.
     *
     * @return A List of Pattern objects representing the dataset.
     * @see Pattern
     */
    public List<Pattern> getDataSet() {
        List<Pattern> patterns = new ArrayList<>();

        for (int classIndex = 1; classIndex <= AMOUNT_OF_CLASSES; classIndex++) {
            for (int patternIndex = 1; patternIndex <= AMOUNT_OF_PATTERNS; patternIndex++) {
                List<Float> features = readFeaturesFromFile(
                        this.filePath
                                + "S" + String.format("%02d", classIndex)
                                + "N" + String.format("%03d", patternIndex)
                                + this.fileExtension);
                patterns.add(new Pattern(features, classIndex));
            }
        }

        return patterns;
    }

    /**
     * Prints the features of the given patterns grouped into their corresponding image classes.
     *
     * @param patterns The List of Pattern objects whose features need to be printed.
     * @see Pattern
     */
    public static void printFeatures(List<Pattern> patterns) {
        System.out.println("Affichage du tableau de mesures :");

        int totalAmountOfPatterns = AMOUNT_OF_PATTERNS * AMOUNT_OF_CLASSES;
        for (int patternIndex = 0; patternIndex < totalAmountOfPatterns; patternIndex++) {
            if (patternIndex % AMOUNT_OF_PATTERNS == 0) {
                System.out.println("\n- Class " + (patternIndex / AMOUNT_OF_PATTERNS + 1) + ":");
            }

            Pattern pattern = patterns.get(patternIndex);
            System.out.println(pattern.toString());
        }
    }
}
