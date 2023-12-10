package classifiers;

import data.Sample;

import java.util.*;

public class KNNClassifier {

//    List<Sample> trainingSet;

    public KNNClassifier(List<Sample> trainingSet)
    {
        this.trainingSet = trainingSet;
    }

    private double calculateDistance(List<Float> measures1, List<Float> measures2)
    {
        // Assurez-vous que les deux listes ont la même taille
        if (measures1.size() != measures2.size())
        {
            throw new IllegalArgumentException("Les listes de mesures doivent avoir la même taille.");
        }

        double distance = 0.0;
        for (int i = 0; i < measures1.size(); i++)
        {
            double diff = measures1.get(i) - measures2.get(i);
            distance += diff * diff;
        }

        return Math.sqrt(distance);
    }

    public int classify(Sample testSample, int k, int p) {
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

    public double score(List<Sample> testSet, int k , int p) {
        int correctPredictions = 0;

        for (Sample testSample : testSet) {
            int predictedLabel = classify(testSample, k, p);
            if (predictedLabel == testSample.getLabelNumber()) {
                correctPredictions++;
            }
        }

        // Calculer le taux de précision
        return (double) correctPredictions / testSet.size();
    }

    public double crossValidation(List<Sample> dataSet, int folds, int k, int p) {
        // Mélanger l'ensemble de données
        List<Sample> shuffledData = new ArrayList<>(dataSet);
        Collections.shuffle(shuffledData, new Random(123));

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
            KNNClassifier knnClassifier = new KNNClassifier(trainingSet);

            // Évaluer le classificateur sur l'ensemble de validation
            double accuracy = knnClassifier.score(validationSet, k, p);
            totalAccuracy += accuracy;
        }

        // Calculer la précision moyenne sur tous les plis
        return totalAccuracy / folds;
    }

}