import classifiers.ClassifierUtilities;
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

        List<Sample>[] dataSet = ClassifierUtilities.splitData(dataSetE34, 0.8f);
        List<Sample> trainingSet = dataSet[0];
        List<Sample> testSet = dataSet[1];

        KNNClassifier knnClassifier = new KNNClassifier(5, 2);
        Map<String, Double> bestHyperparameters = knnClassifier.findBestHyperparameters(trainingSet, new int[]{1, 2});

        int bestK = bestHyperparameters.get("k").intValue();
        int bestP = bestHyperparameters.get("p").intValue();
        double bestAccuracy = bestHyperparameters.get("accuracy");

        System.out.println("Meilleurs hyperparamètres : k = " + bestK + ", p = " + bestP);
        System.out.println("Précision moyenne correspondante : " + bestAccuracy);

//        Evaluation du modèle sur le testSet
        KNNClassifier classifier = new KNNClassifier(bestK, bestP);
        double realScore = classifier.score(trainingSet, testSet);
        System.out.println("Précision du modèle sur le testSet :" + realScore);
        for (int j=1; j<=9; j++){
            System.out.println(classifier.precision(trainingSet, testSet, j));
        }


    }
}
