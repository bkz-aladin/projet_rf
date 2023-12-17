import classifiers.ClassifierUtilities;
import classifiers.KMeans;
import classifiers.KNNClassifier;
import data.Sample;
import input_output.DataReader;

import java.util.*;

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

        Map<String, Double> bestHyperparametersE34 = ClassifierUtilities.getHyperparameters(trainingSetE34);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        ClassifierUtilities.evaluateModelOnTestSet(trainingSetE34, testSetE34, bestHyperparametersE34);

        System.out.println("Résultats de la Méthode F0");

        List<Sample>[] splitedDataSetF0 = ClassifierUtilities.splitData(dataSetF0, 0.8f);
        List<Sample> trainingSetF0 = splitedDataSetF0[0];
        List<Sample> testSetF0 = splitedDataSetF0[1];

        Map<String, Double> bestHyperparametersF0 = ClassifierUtilities.getHyperparameters(trainingSetF0);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        ClassifierUtilities.evaluateModelOnTestSet(trainingSetF0, testSetF0, bestHyperparametersF0);

        System.out.println("Résultats de la Méthode GFD");

        List<Sample>[] splitedDataSetGFD = ClassifierUtilities.splitData(dataSetGFD, 0.8f);
        List<Sample> trainingSetGFD = splitedDataSetGFD[0];
        List<Sample> testSetGFD = splitedDataSetGFD[1];

        Map<String, Double> bestHyperparametersGFD = ClassifierUtilities.getHyperparameters(trainingSetGFD);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        ClassifierUtilities.evaluateModelOnTestSet(trainingSetGFD, testSetGFD, bestHyperparametersGFD);

        System.out.println("Résultats de la Méthode SA");

        List<Sample>[] splitedDataSetSA = ClassifierUtilities.splitData(dataSetSA, 0.8f);
        List<Sample> trainingSetSA = splitedDataSetSA[0];
        List<Sample> testSetSA = splitedDataSetSA[1];

        Map<String, Double> bestHyperparametersSA = ClassifierUtilities.getHyperparameters(trainingSetSA);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        ClassifierUtilities.evaluateModelOnTestSet(trainingSetSA, testSetSA, bestHyperparametersSA);


        System.out.println("================================== K-Means Classifier ==============================================");

        int k = 9;
        int maxIterations = 100;
        boolean usingPP = true;
        int distanceNorm = 2;
        int randomSeed = 123; // optional

        KMeans kMeans = new KMeans(k, dataSetGFD, usingPP, maxIterations, distanceNorm, randomSeed);
        kMeans.runKMeans();

        List<KMeans.Cluster> clusters = kMeans.getClusters();

        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + ":");
            for (Sample sample : clusters.get(i).getSamples()) {
                System.out.println(sample);
            }
            System.out.println();
        }
    }

}
