package classifiers;

import data.Sample;

import java.util.*;

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

        double sum = 0.0;
        for (int dimension = 1; dimension <= coordinates2.size(); dimension++) {
            double coordinate1 = coordinates1.get(dimension - 1);
            double coordinate2 = coordinates2.get(dimension - 1);
            sum += Math.pow(Math.abs(coordinate1 - coordinate2), p);
        }

        return (float) Math.pow(sum, 1.0 / p);
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

    public static Map<String, Double> getHyperparameters(List<Sample> trainingSet){
        KNNClassifier knnClassifier = new KNNClassifier();

        // Recherche des meilleurs hyperparamètres
        Map<String, Double> bestHyperparameters = knnClassifier.findBestHyperparameters(trainingSet, new int[]{1, 2});

        // Affichage des meilleurs hyperparamètres
        int bestK = bestHyperparameters.get("k").intValue();
        int bestP = bestHyperparameters.get("p").intValue();
        double bestAccuracy = bestHyperparameters.get("accuracy");

        System.out.println("Meilleurs hyperparamètres : k = " + bestK + ", p = " + bestP);
        System.out.println("Précision moyenne correspondante : " + bestAccuracy);

        return bestHyperparameters;
    }

    public static void evaluateModelOnTestSet(List<Sample> trainingSet, List<Sample> testSet, Map<String, Double> bestHyperparameters) {
        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        int bestK = bestHyperparameters.get("k").intValue();
        int bestP = bestHyperparameters.get("p").intValue();

        KNNClassifier classifier = new KNNClassifier(bestK, bestP);
        double realScore = classifier.score(trainingSet, testSet);
        System.out.println("Précision du modèle sur le testSet : " + realScore);

        // Calcul et affichage de la précision et du rappel pour chaque classe
        for (int j = 1; j <= 9; j++) {
            double precision = classifier.precision(trainingSet, testSet, j);
            double recall = classifier.recall(trainingSet, testSet, j);
            System.out.println("Taux de reconnaissance de la classe " + j + " est de " + precision + " et le rappel : " + recall);
        }
        System.out.println();
    }
}
