package classifiers;

import data.Sample;

import java.util.*;

/**
 * The {@code KNNClassifier} class implements the KNN classification algorithm.
 *
 * <p>It is used to classify samples by comparing their features to that of their k-nearest neighbors.
 *
 * @author [Latif Yaya]
 * @version 1.0
 */
public class KNNClassifier {

//    List<Sample> trainingSet;
    int k;
    int p;

    /**
     * Default constructor initializing k to 5 and p to 2.
     */
    public KNNClassifier()
    {
        k = 5;
        p = 2;
    }

    /**
     * Constructor with parameters to set the classifier's k and p values.
     *
     * @param k_neighbors Number of neighbors to consider in the classification.
     * @param p_dist      Order of the Minkowski norm for distance calculation.
     */
    public KNNClassifier(int k_neighbors, int p_dist)
    {
        k = k_neighbors;
        p = p_dist;
    }

    /**
     * Classifies a test sample based on the k-nearest neighbors in the training set.
     *
     * @param trainingSet The training set used for classification.
     * @param testSample  The sample to be classified.
     * @return The predicted label for the test sample.
     */
    private int classify(List<Sample> trainingSet,Sample testSample) {
        // Trier les échantillons en fonction de leur distance par rapport à l'échantillon de test

        trainingSet.sort(Comparator.comparingDouble
                (a -> ClassifierUtilities.calculateMinkowskiDistance
                        (a.getFeatures(), testSample.getFeatures(), p)));

        // Compter les occurrences de chaque classe parmi les k plus proches voisins
        Map<Integer, Integer> classCounts = new HashMap<>();
        for (int i = 0; i < this.k; i++) {
            int label = trainingSet.get(i).getLabel();
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        }

        // Retourner la classe majoritaire parmi les k plus proches voisins
        return Collections.max(classCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /**
     * Computes the F1-score of the classifier on a test set.
     *
     * @param trainingSet The training dataset used for the classifier.
     * @param testSet     The test dataset to evaluate the F1-score of the classifier.
     * @return The F1-score of the classifier on the test set.
     */
    public double score(List<Sample> trainingSet, List<Sample> testSet) {
        if (trainingSet.isEmpty() || testSet.isEmpty()) {
            // Gérer le cas où l'un des ensembles est vide
            return -1.0;
        }

        int truePositives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;

        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet, testSample);
            int trueLabel = testSample.getLabel();

            if (predictedLabel == trueLabel) {
                truePositives++;
            } else {
                // Si la prédiction est incorrecte, déterminer s'il s'agit d'un faux positif ou d'un faux négatif
                if (predictedLabel != trueLabel && trueLabel >= 0) {
                    falseNegatives++;
                } else {
                    falsePositives++;
                }
            }
        }

        double precision = (double) truePositives / (truePositives + falsePositives);
        double recall = (double) truePositives / (truePositives + falseNegatives);

        // Calculer la F1-score
        return 2 * (precision * recall) / (precision + recall);
    }

    /**
     * Performs k-fold cross-validation on the dataset.
     *
     * @param dataSet The dataset to perform cross-validation on.
     * @param folds   The number of folds for cross-validation.
     * @return The average accuracy across all folds.
     */
    public double crossValidation(List<Sample> dataSet, int folds) {
        // Mélanger l'ensemble de données
        List<Sample> shuffledData = new ArrayList<>(dataSet);
        Collections.shuffle(shuffledData);

        // Diviser l'ensemble de données en 'folds' sous-ensembles
        int foldSize = shuffledData.size() / folds;
        List<List<Sample>> foldsList = new ArrayList<>();
        for (int i = 0; i < folds; i++) {
            int start = i * foldSize;
            int end = (i + 1) * foldSize;
            List<Sample> fold = shuffledData.subList(start, end);
            foldsList.add(fold);
        }

        // Effectuer la validation croisée
        double totalAccuracy = 0.0;
        for (int i = 0; i < folds; i++) {
            List<Sample> validationSet = foldsList.get(i);
            List<Sample> trainingSet = new ArrayList<>();

            // Construire l'ensemble d'entraînement en utilisant les autres folds
            for (int j = 0; j < folds; j++) {
                if (j != i) {
                    trainingSet.addAll(foldsList.get(j));
                }
            }

            // Créer un classificateur avec l'ensemble d'entraînement

            // Évaluer le classificateur sur l'ensemble de validation
            double accuracy = this.score(trainingSet, validationSet);
            totalAccuracy += accuracy;
        }

        // Calculer la précision moyenne sur tous les plis
        return totalAccuracy / folds;
    }

    /**
     * Finds the best hyperparameters for the classifier using cross-validation.
     *
     * @param trainingSet The training set used for hyperparameter tuning.
     * @param pValues     Array of p values to consider in the search.
     * @return A map containing the best hyperparameters and their corresponding accuracy.
     */
    public Map<String, Double> findBestHyperparameters(List<Sample> trainingSet, int[] pValues) {
        int bestK = -1;
        int bestP = -1;
        double bestAccuracy = Double.MIN_VALUE;


        for (int k = 1; k <= 10; k++) {
            for (int p : pValues) {

                List<Double> accuracyValues = new ArrayList<>();
                for (int i=1 ; i <= 15; i++)
                {

                    KNNClassifier knnClassifier = new KNNClassifier(k, p);
                    double accuracy = knnClassifier.crossValidation(trainingSet, 5);

                    accuracyValues.add(accuracy);
                    // Mettre à jour les meilleurs hyperparamètres si la précision est améliorée

                }

                double averageAccuracy = accuracyValues.stream()
                        .mapToDouble(d -> d)
                        .average()
                        .orElse(0.0);

                if (averageAccuracy >= bestAccuracy) {
                    bestAccuracy = averageAccuracy;
                    bestK = k;
                    bestP = p;
                }
            }
        }

        // Stocker les meilleurs hyperparamètres dans un dictionnaire
        Map<String, Double> bestHyperparameters = new HashMap<>();
        bestHyperparameters.put("k", (double) bestK);
        bestHyperparameters.put("p", (double) bestP);
        bestHyperparameters.put("accuracy", bestAccuracy);

        return bestHyperparameters;
    }

    /**
     * Computes the confusion matrix for the KNN classification.
     *
     * @param trainingSet The training set used for classification.
     * @param testSet     The test set for which the confusion matrix is computed.
     * @param numClasses  The number of classes in the classification task.
     * @return The confusion matrix.
     */
    public int[][] confusionMatrix(List<Sample> trainingSet, List<Sample> testSet, int numClasses) {
        int[][] matrix = new int[numClasses][numClasses];

        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet ,testSample);
            int actualLabel = testSample.getLabel();

            actualLabel -= 1;
            predictedLabel -= 1;

            // Vérifier que les étiquettes sont valides avant d'accéder à la matrice
            if (actualLabel >= 0 && actualLabel < numClasses && predictedLabel >= 0 && predictedLabel < numClasses) {
                matrix[actualLabel][predictedLabel]++;
            } else {
                System.out.println("Étiquette invalide : actual=" + (actualLabel + 1) + ", predicted=" + (predictedLabel + 1));
            }
        }

        return matrix;
    }

    /**
     * Computes the precision for a specific class in the KNN classification.
     *
     * @param knnClassifier    The KNN classifier used for prediction.
     * @param trainingSet      The training set used for classification.
     * @param testSet          The test set for which precision is computed.
     * @param classToEvaluate  The class label to evaluate precision.
     * @return The precision for the specified class.
     */
    public static double precision(KNNClassifier knnClassifier, List<Sample> trainingSet, List<Sample> testSet, int classToEvaluate) {
        int truePositives = 0;
        int falsePositives = 0;

        for (Sample testSample : testSet) {
            int predictedLabel = knnClassifier.classify(trainingSet, testSample);
            int actualLabel = testSample.getLabel();

            if (predictedLabel == classToEvaluate) {
                if (actualLabel == classToEvaluate) {
                    truePositives++;
                } else {
                    falsePositives++;
                }
            }
        }

        return (double) truePositives / (truePositives + falsePositives);
    }

    /**
     * Computes the recall for a specific class in the KNN classification.
     *
     * @param trainingSet      The training set used for classification.
     * @param testSet          The test set for which recall is computed.
     * @param classToEvaluate  The class label to evaluate recall.
     * @return The recall for the specified class.
     */
    public double recall(List<Sample> trainingSet, List<Sample> testSet, int classToEvaluate) {
        int truePositives = 0;
        int falseNegatives = 0;


        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet, testSample);
            int actualLabel = testSample.getLabel();


            if (actualLabel == classToEvaluate) {
                if (predictedLabel == classToEvaluate) {
                    truePositives++;
                } else {
                    falseNegatives++;
                }
            }
        }

        return (double) truePositives / (truePositives + falseNegatives);
    }

    /**
     * Computes the F1-score for a specific class in the KNN classification.
     *
     * @param trainingSet      The training set used for classification.
     * @param testSet          The test set for which F1-score is computed.
     * @param classToEvaluate  The class label to evaluate F1-score.
     * @return The F1-score for the specified class.
     */
    public double f1Score(List<Sample> trainingSet, List<Sample> testSet, int classToEvaluate) {
        double precision = precision(this, trainingSet, testSet, classToEvaluate);
        double recall = recall(trainingSet, testSet, classToEvaluate);

        return 2 * (precision * recall) / (precision + recall);
    }

    /**
     * Evaluates the model on the provided test set using the best hyperparameters.
     *
     * @param trainingSet       The training set used for the model.
     * @param testSet           The test set on which the model is evaluated.
     * @param bestHyperparameters The best hyperparameters obtained from hyperparameter tuning.
     */
    public static void evaluateModelOnTestSet(List<Sample> trainingSet, List<Sample> testSet, Map<String, Double> bestHyperparameters) {
        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        int bestK = bestHyperparameters.get("k").intValue();
        int bestP = bestHyperparameters.get("p").intValue();

        KNNClassifier classifier = new KNNClassifier(bestK, bestP);
        String realScore = String.format("%.2f", classifier.score(trainingSet, testSet)*100);
        System.out.println("Précision du modèle sur le testSet : " + realScore);
        int[][] matrix = classifier.confusionMatrix(trainingSet, testSet, 9);
        ClassifierUtilities.printConfusionMatrix(matrix);

        // Calcul et affichage de la précision et du rappel pour chaque classe
        for (int j = 1; j <= 9; j++) {
            String precision = String.format("%.2f", precision(classifier, trainingSet, testSet, j)*100);
            String rappel = String.format("%.2f", classifier.recall(trainingSet, testSet, j)*100);
            String f1_score = String.format("%.2f", classifier.f1Score(trainingSet, testSet, j)*100);
            System.out.printf("Classe " + j + ": P = " +  precision + " R = " + rappel +" FM = " + f1_score + "\n");
        }
        System.out.println();
    }
}