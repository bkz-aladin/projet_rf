package classifiers;

import data.Sample;

import java.util.*;

public class KNNClassifier {

    List<Sample> trainingSet;

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
        trainingSet.sort(Comparator.comparingDouble
                (a -> ClassifierUtilities.calculateMinkowskiDistance
                        (a.getFeatures(), testSample.getFeatures(), p)));

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
}