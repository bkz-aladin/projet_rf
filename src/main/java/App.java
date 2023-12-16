import classifiers.ClassifierUtilities;
import classifiers.KMeans;
import classifiers.KNNClassifier;
import data.Sample;
import input_output.DataReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {

        // Initializing a DataReader for each image analysis method applied on BDshape.
        DataReader readerE34 = new DataReader("../data/E34/", ".e34");
        DataReader readerF0 = new DataReader("../data/F0/", ".f0");
        DataReader readerGFD = new DataReader("../data/GFD/", ".gfd");
        DataReader readerSA = new DataReader("../data/SA/", ".sa");

        // Initializing datasets for each image analysis method.
        List<Sample> dataSetE34 = readerE34.getDataSet();
        List<Sample> dataSetF0 = readerF0.getDataSet();
        List<Sample> dataSetGFD = readerGFD.getDataSet();
        List<Sample> dataSetSA = readerSA.getDataSet();


        /*--------------------------------------- tests ---------------------------------------*/

        System.out.println("================================== KNN CLASSIFICATION ============================================\n");

        System.out.println("Résultats de la Méthode E34");

        List<Sample>[] splitedDataSetE34 = ClassifierUtilities.splitData(dataSetE34, 0.8f);
        List<Sample> trainingSetE34 = splitedDataSetE34[0];
        List<Sample> testSetE34 = splitedDataSetE34[1];

        Map<String, Double> bestHyperparametersE34 = getHyperparameters(trainingSetE34);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        evaluateModelOnTestSet(trainingSetE34, testSetE34, bestHyperparametersE34);

        System.out.println("Résultats de la Méthode F0");

        List<Sample>[] splitedDataSetF0 = ClassifierUtilities.splitData(dataSetF0, 0.8f);
        List<Sample> trainingSetF0 = splitedDataSetF0[0];
        List<Sample> testSetF0 = splitedDataSetF0[1];

        Map<String, Double> bestHyperparametersF0 = getHyperparameters(trainingSetF0);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        evaluateModelOnTestSet(trainingSetF0, testSetF0, bestHyperparametersF0);

        System.out.println("Résultats de la Méthode GFD");

        List<Sample>[] splitedDataSetGFD = ClassifierUtilities.splitData(dataSetGFD, 0.8f);
        List<Sample> trainingSetGFD = splitedDataSetGFD[0];
        List<Sample> testSetGFD = splitedDataSetGFD[1];

        Map<String, Double> bestHyperparametersGFD = getHyperparameters(trainingSetGFD);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        evaluateModelOnTestSet(trainingSetGFD, testSetGFD, bestHyperparametersGFD);

        System.out.println("Résultats de la Méthode SA");

        List<Sample>[] splitedDataSetSA = ClassifierUtilities.splitData(dataSetSA, 0.8f);
        List<Sample> trainingSetSA = splitedDataSetSA[0];
        List<Sample> testSetSA = splitedDataSetSA[1];

        Map<String, Double> bestHyperparametersSA = getHyperparameters(trainingSetSA);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        evaluateModelOnTestSet(trainingSetSA, testSetSA, bestHyperparametersSA);


        System.out.println("================================== K-Means Classifier ==============================================");
        Map<KMeans.Centroid, List<Sample>> clusters = KMeans.getClustersOfSamples
                (trainingSetE34, 9, 2, 500);
        for (Map.Entry<KMeans.Centroid, List<Sample>> entry : clusters.entrySet()) {
            for (Sample sample : entry.getValue()) {
                System.out.print(sample.getLabelNumber());
            }
            System.out.println();
        }


        int k = 9;
        int maxIterations = 100;
        boolean usingPP = true;
        int distanceNorm = 2;
        int randomSeed = 123; // optional

        KMeans kMeans = new KMeans(k, dataSetGFD, usingPP, maxIterations, distanceNorm, randomSeed);
        kMeans.runKMeans();

        List<KMeans.Cluster> clusters = kMeans.getClusters();

//        for (int i = 0; i < clusters.size(); i++) {
//            System.out.println("Cluster " + (i + 1) + ":");
//            for (Sample sample : clusters.get(i).getSamples()) {
//                System.out.println(sample);
//            }
//            System.out.println();
//        }


    }

    private static Map<String, Double> getHyperparameters(List<Sample> trainingSet){
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

    private static void evaluateModelOnTestSet(List<Sample> trainingSet, List<Sample> testSet, Map<String, Double> bestHyperparameters) {
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
