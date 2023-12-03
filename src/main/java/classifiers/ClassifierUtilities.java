package classifiers;

import data.Pattern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 */
public final class ClassifierUtilities {

    public static List<Pattern> generateTrainingSet() {
        // TODO:

        return null;
    }

    public static List<Pattern> generateTestSet() {
        // TODO:

        return null;
    }


    public static List<Pattern>[] splitData(List<Pattern> dataSet, float trainSetRatio) {
        // Mélanger les échantillons de manière aléatoire
        Collections.shuffle(dataSet, new Random(456));

        // Calculer les indices pour la division
        int totalSize = dataSet.size();
        int trainingSize = (int) (totalSize * trainSetRatio);

        // Diviser l'ensemble de données
        List<Pattern> trainingSet = new ArrayList<>(dataSet.subList(0, trainingSize));
        List<Pattern> testSet = new ArrayList<>(dataSet.subList(trainingSize, totalSize));

        // Retourner un tableau de deux listes
        List<Pattern>[] result = new List[]{trainingSet, testSet};
        return result;
    }

    private static void exportToCSV(List<Pattern> dataset, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Pattern sample: dataset) {

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
