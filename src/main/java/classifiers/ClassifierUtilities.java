package classifiers;

import data.Sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public final class ClassifierUtilities {

    /**
     * Calculates the Minkowski distance (Lp norm) between two coordinates.
     * The Minkowski metric is a general class of metrics for d-dimensional coordinates.
     * It includes the Euclidean distance (p=2) and the Manhattan distance (p=1).
     *
     * @param coordinates1 The list of the first coordinates to calculate the distance from.
     * @param coordinates2 The list of the second coordinates to calculate the distance to.
     * @param p The order of the Minkowski norm, influencing the calculation.
     * @return The Minkowski distance between the two coordinates.
     * @throws IllegalArgumentException If p is less than 1.
     * @throws IllegalArgumentException If the dimensions of the coordinates are not equal.
     */
    public static float calculateMinkowskiDistance(List<Float> coordinates1, List<Float> coordinates2, int p) {
        if (coordinates1.size() != coordinates2.size()) {
            throw new IllegalArgumentException("The two coordinates must be of equal dimensions.");
        }
        if (p < 1) {
            throw new IllegalArgumentException("The order of the Minkowski norm p must be greater or equal to 1.");
        }

        float sum = 0.0f;
        for (int dimension = 1; dimension <= coordinates2.size(); dimension++) {
            float coordinate1 = coordinates1.get(dimension - 1);
            float coordinate2 = coordinates2.get(dimension - 1);
            sum += (float) Math.pow(Math.abs(coordinate1 - coordinate2), p);
        }

        return (float) Math.pow(sum, 1.0 / p);
    }

    public static List<Sample>[] splitData(List<Sample> dataSet, float trainSetRatio) {
        // Mélanger les échantillons de manière aléatoire
        Collections.shuffle(dataSet, new Random());

        // Calculer les indices pour la division
        int totalSize = dataSet.size();
        int trainingSize = (int) (totalSize * trainSetRatio);

        // Diviser l'ensemble de données
        List<Sample> trainingSet = new ArrayList<>(dataSet.subList(0, trainingSize));
        List<Sample> testSet = new ArrayList<>(dataSet.subList(trainingSize, totalSize));

        // Retourner un tableau de deux listes
        List<Sample>[] result = new List[]{trainingSet, testSet};
        return result;
    }

    private static void exportToCSV(List<Sample> dataset, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Sample sample: dataset) {

                //System.out.println(dataset.get(sampleIndex));
                //System.exit(0);
                //for (Float measure : sample.getMeasures()) {

                String data = String.valueOf(sample.getFeatures());
                // Concaténation des éléments de la liste interne avec des virgules et écriture dans le fichier
                writer.write(String.join(",", data));
                writer.newLine();
                //}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
