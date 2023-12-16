package classifiers;

import data.Sample;

import java.util.*;

public class KNNClassifier {

//    List<Sample> trainingSet;
    int k;
    int p;

    public KNNClassifier()
    {
        k = 5;
        p = 2;
    }
    public KNNClassifier(int n_neighbors, int p_dist)
    {
        k = n_neighbors;
        p = p_dist;
    }

    private int classify(List<Sample> trainingSet,Sample testSample) {
        // Trier les échantillons en fonction de leur distance par rapport à l'échantillon de test

        trainingSet.sort(Comparator.comparingDouble
                (a -> ClassifierUtilities.calculateMinkowskiDistance
                        (a.getFeatures(), testSample.getFeatures(), p)));

        // Compter les occurrences de chaque classe parmi les k plus proches voisins
        Map<Integer, Integer> classCounts = new HashMap<>();
        for (int i = 0; i < this.k; i++) {
            int label = trainingSet.get(i).getLabelNumber();
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        }

        // Retourner la classe majoritaire parmi les k plus proches voisins
        return Collections.max(classCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public double score(List<Sample> trainingSet, List<Sample> testSet) {
        int correctPredictions = 0;

        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet ,testSample);
            if (predictedLabel == testSample.getLabelNumber()) {
                correctPredictions++;
            }
        }

        // Calculer le taux de précision
        return (double) correctPredictions / testSet.size();
    }

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

    public Map<String, Double> findBestHyperparameters(List<Sample> trainingSet, int[] pValues) {
        int bestK = -1;
        int bestP = -1;
        double bestAccuracy = Double.MIN_VALUE;
        List<Double> accuracyValues = new ArrayList<>();

        for (int k = 1; k <= 20; k++) {
            for (int p : pValues) {

                for (int i=1 ; i <= 15; i++)
                {

                    KNNClassifier knnClassifier = new KNNClassifier(k, p);
                    double accuracy = knnClassifier.crossValidation(trainingSet, 6);

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

    public int[][] confusionMatrix(List<Sample> trainingSet, List<Sample> testSet, int numClasses) {
        int[][] matrix = new int[numClasses][numClasses];

        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet ,testSample);
            int actualLabel = testSample.getLabelNumber();
            matrix[actualLabel][predictedLabel]++;
        }

        return matrix;
    }

    public double precision(List<Sample> trainingSet, List<Sample> testSet, int classToEvaluate) {
        int truePositives = 0;
        int falsePositives = 0;

        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet, testSample);
            int actualLabel = testSample.getLabelNumber();

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

    public double recall(List<Sample> trainingSet, List<Sample> testSet, int classToEvaluate) {
        int truePositives = 0;
        int falseNegatives = 0;

        for (Sample testSample : testSet) {
            int predictedLabel = classify(trainingSet, testSample);
            int actualLabel = testSample.getLabelNumber();

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

    public double f1Score(List<Sample> trainingSet, List<Sample> testSet, int classToEvaluate) {
        double precision = precision(trainingSet, testSet, classToEvaluate);
        double recall = recall(trainingSet, testSet, classToEvaluate);

        return 2 * (precision * recall) / (precision + recall);
    }

}