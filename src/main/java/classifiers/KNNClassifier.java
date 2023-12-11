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
        trainingSet.sort(Comparator.comparingDouble(a -> a.calculateMinkowskiDistance(testSample, p)));

        // Compter les occurrences de chaque classe parmi les k plus proches voisins
        Map<Integer, Integer> classCounts = new HashMap<>();
        for (int i = 0; i < k; i++) {
            int label = trainingSet.get(i).getLabelNumber();
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        }

//        System.out.println(classCounts);
//        System.out.println(testSample.getLabelNumber());
//        System.exit(0);

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



}