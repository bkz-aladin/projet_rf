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
        Collections.shuffle(dataSet, new Random(5));

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
        String bestAccuracy = String.format("%.2f", bestHyperparameters.get("accuracy")*100);

        System.out.println("Meilleurs hyperparamètres : k = " + bestK + ", p = " + bestP);
        System.out.println("Précision moyenne correspondante : " + bestAccuracy);

        return bestHyperparameters;
    }

    public static void evaluateModelOnTestSet(List<Sample> trainingSet, List<Sample> testSet, Map<String, Double> bestHyperparameters) {
        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        int bestK = bestHyperparameters.get("k").intValue();
        int bestP = bestHyperparameters.get("p").intValue();

        KNNClassifier classifier = new KNNClassifier(bestK, bestP);
        String realScore = String.format("%.2f", classifier.score(trainingSet, testSet)*100);
        System.out.println("Précision du modèle sur le testSet : " + realScore);
        int[][] matrix = classifier.confusionMatrix(trainingSet, testSet, 9);
        printConfusionMatrix(matrix);

//         Calcul et affichage de la précision et du rappel pour chaque classe
        for (int j = 1; j <= 9; j++) {
            String precision = String.format("%.2f", classifier.precision(trainingSet, testSet, j)*100);
            String rappel = String.format("%.2f", classifier.recall(trainingSet, testSet, j)*100);
            String f1_score = String.format("%.2f", classifier.f1Score(trainingSet, testSet, j)*100);
            System.out.printf("Classe " + j + ": P = " +  precision + " R = " + rappel +" FM = " + f1_score + "\n");
        }
        System.out.println();
    }

    public static void printConfusionMatrix(int[][] confusionMatrix) {
        System.out.println("MATRICE DE CONFUSION");
        int numRows = confusionMatrix.length;
        int numCols = confusionMatrix[0].length;

        // Afficher les étiquettes de colonnes
        System.out.print("Actual \\ Predicted  ");
        for (int i = 1; i <= numCols; i++) {
            System.out.printf("%-12d", i);
        }
        System.out.println();

        // Afficher une ligne de séparation
        for (int i = 0; i <= numCols * 13; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Afficher les valeurs de la matrice de confusion
        for (int i = 1; i <= numRows; i++) {
            System.out.printf("%-17d|", i);
            for (int j = 1; j <= numCols; j++) {
                System.out.printf("%-12d", confusionMatrix[i - 1][j - 1]);
            }
            System.out.println();
        }
    }

}
