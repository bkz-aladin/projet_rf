package classifiers;

import data.Sample;

import java.util.*;

/**
 * Utility class for working with {@code KMeansClassifier} and {@code KNNClassifier}.
 * Generally used for common operations related to data splitting, hyperparameter tuning, confusion matrix printing,
 * and KMeans model evaluation.
 *
 * @author [Latif Yaya, Kentaro Sauce]
 * @version 1.0
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

    /**
     * Splits a dataset into shuffled training and test sets based on the given ratio.
     *
     * @param dataSet        The dataset to be split.
     * @param trainSetRatio  The ratio of the dataset to be allocated to the training set.
     * @return An array containing two lists: the training set and the test set.
     */
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

    /**
     * Finds the best hyperparameters for a KNN classifier using the given training set.
     *
     * @param trainingSet The data set of samples used for hyperparameter tuning.
     * @return A map containing the best hyperparameters and their corresponding accuracy.
     */
    public static Map<String, Double> getHyperparameters(List<Sample> trainingSet){
        KNNClassifier knnClassifier = new KNNClassifier();

        // Recherche des meilleurs hyperparamètres
        Map<String, Double> bestHyperparameters = knnClassifier.findBestHyperparameters(trainingSet, new int[]{1, 2});

        // Affichage des meilleurs hyperparamètres
        int bestK = bestHyperparameters.get("k").intValue();
        int bestP = bestHyperparameters.get("p").intValue();
        String bestAccuracy = String.format("%.2f", bestHyperparameters.get("accuracy")*100);

        System.out.println("Meilleurs hyperparamètres : k = " + bestK + ", p = " + bestP);
        System.out.println("Score moyenne correspondante : " + bestAccuracy);

        return bestHyperparameters;
    }


    /**
     * Prints the confusion matrix for either a KNN or a KMeans classification.
     *
     * @param confusionMatrix The confusion matrix to be printed.
     */
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

    /**
     * Computes the best KMeansClassifier model of given parameters based on multiple evaluations.
     * All RNG is decided by the given seed.
     *
     * @param maxEvaluationIterations The maximum number of evaluations to perform before deciding the best model.
     * @param k                       Number of clusters to form.
     * @param dataSet                 List of samples representing the dataset.
     * @param usingPP                 Whether to initialize centroids using the k-means++ strategy or naively.
     * @param maxIterations           Maximum number of iterations for the KMeans algorithm.
     * @param distanceNorm            Order of the Minkowski norm for distance calculation.
     * @param randomSeed              Seed for all RNG in the algorithm.
     * @return The best KMeansClassifier model based on its macro F1 score.
     * @throws IllegalArgumentException If maxEvaluationIterations is not in the range [0, 100].
     */
    public static KMeansClassifier computeBestKmeansModel
            (int maxEvaluationIterations, int k, List<Sample> dataSet, boolean usingPP,
             int maxIterations, int distanceNorm, int randomSeed) {

        // Throw exception if maxEvaluationIterations is not in the range [0, 100].
        if (maxEvaluationIterations < 0 || maxEvaluationIterations > 100) {
            throw new IllegalArgumentException
                    ("Le nombre d'itérations d'évaluation du modèle Kmeans doit être compris entre 0 et 100");
        }

        // Generate a first KMeansClassifier model.
        KMeansClassifier bestModel = new KMeansClassifier
                (k, dataSet, usingPP, maxIterations, distanceNorm, randomSeed);
        bestModel.runKMeans();

        // Generate the remaining models and keep the best performing one using their macro F1 score.
        for (int iteration = 0; iteration < maxEvaluationIterations - 1; iteration++) {
            KMeansClassifier currentModel = new KMeansClassifier
                    (k, dataSet, usingPP, maxIterations, distanceNorm, randomSeed);
            currentModel.runKMeans();
            if (bestModel.macroF1Score <= currentModel.macroF1Score) bestModel = currentModel;
        }

        return bestModel;
    }

    /**
     * Computes the best KMeansClassifier model of given parameters based on multiple evaluations.
     *
     * @param maxEvaluationIterations The maximum number of evaluations to perform before deciding the best model.
     * @param k                       Number of clusters to form.
     * @param dataSet                 List of samples representing the dataset.
     * @param usingPP                 Whether to initialize centroids using the k-means++ strategy or naively.
     * @param maxIterations           Maximum number of iterations for the KMeans algorithm.
     * @param distanceNorm            Order of the Minkowski norm for distance calculation.
     * @return The best KMeansClassifier model based on its macro F1 score.
     * @throws IllegalArgumentException If maxEvaluationIterations is not in the range [0, 100].
     */
    public static KMeansClassifier computeBestKmeansModel
            (int maxEvaluationIterations, int k, List<Sample> dataSet, boolean usingPP,
             int maxIterations, int distanceNorm) {

        // Throw exception if maxEvaluationIterations is not in the range [0, 100].
        if (maxEvaluationIterations < 0 || maxEvaluationIterations > 100) {
            throw new IllegalArgumentException
                    ("Le nombre d'itérations d'évaluation du modèle Kmeans doit être compris entre 0 et 100");
        }

        // Generate a first KMeansClassifier model.
        KMeansClassifier bestModel = new KMeansClassifier
                (k, dataSet, usingPP, maxIterations, distanceNorm);
        bestModel.runKMeans();

        // Generate the remaining models and keep the best performing one using their macro F1 score.
        for (int iteration = 0; iteration < maxEvaluationIterations - 1; iteration++) {
            KMeansClassifier currentModel = new KMeansClassifier
                    (k, dataSet, usingPP, maxIterations, distanceNorm);
            currentModel.runKMeans();
            if (bestModel.macroF1Score <= currentModel.macroF1Score) bestModel = currentModel;
        }

        return bestModel;
    }
}
