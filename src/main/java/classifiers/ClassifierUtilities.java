package classifiers;

import data.Sample;

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

    public static List<Sample> generateTrainingSet() {
        // TODO:

        return null;
    }

    public static List<Sample> generateTestSet() {
        // TODO:

        return null;
    }


    public static List<Sample>[] splitData(List<Sample> dataSet, float trainSetRatio) {
        // Mélanger les échantillons de manière aléatoire
        Collections.shuffle(dataSet, new Random(123));

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

    public static void exportToCSV(List<Sample> dataset, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Sample sample: dataset) {

                //System.out.println(dataset.get(sampleIndex));
                //System.exit(0);
                //for (Float measure : sample.getMeasures()) {

                //List<> features = sample.getFeatures();
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
