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

        List<Sample>[] dataSet = ClassifierUtilities.splitData(dataSetSA, 0.8f);
        List<Sample> trainingSet = dataSet[0];
        List<Sample> testSet = dataSet[1];

        int[] pValues = {1, 2};
        double bestAccuracy = Double.MIN_VALUE;
        int bestK = -1;
        int bestP = -1;

        for (int k = 2; k<= 20; k++) {
            for (int p : pValues) {
                KNNClassifier knnClassifier = new KNNClassifier(k, p);
                double averageAccuracy = knnClassifier.crossValidation(trainingSet, 5);

                // Mettre à jour les meilleurs hyperparamètres si la précision est améliorée
                if (averageAccuracy > bestAccuracy) {
                    bestAccuracy = averageAccuracy;
                    bestK = k;
                    bestP = p;
                }
            }
        }

        System.out.println("Meilleurs hyperparamètres : k = " + bestK + ", p = " + bestP);
        System.out.println("Précision moyenne correspondante : " + bestAccuracy);

        KNNClassifier knnClassifier = new KNNClassifier(bestK, bestP);
        double realScore = knnClassifier.score(trainingSet, testSet);
        System.out.println(realScore);


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
}
