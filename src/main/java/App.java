import classifiers.ClassifierUtilities;
import classifiers.KMeansClassifier;
import classifiers.KNNClassifier;
import data.Sample;
import input_output.DataReader;

import java.util.*;

import static classifiers.ClassifierUtilities.getHyperparameters;

public class App {

    public static void main(String[] args) throws Exception {

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

        // Splitting the datasets into their corresponding training set and test set using a split ratio.
        float splitRatio = 0.8f;
        List<Sample>[] splitDataSetSA = ClassifierUtilities.splitData(dataSetSA, splitRatio);
        List<Sample>[] splitDataSetE34 = ClassifierUtilities.splitData(dataSetE34, splitRatio);
        List<Sample>[] splitDataSetGFD = ClassifierUtilities.splitData(dataSetGFD, splitRatio);
        List<Sample>[] splitDataSetF0 = ClassifierUtilities.splitData(dataSetF0, splitRatio);

        // Recovering the training sets.
        List<Sample> trainingSetSA = splitDataSetSA[0];
        List<Sample> trainingSetE34 = splitDataSetE34[0];
        List<Sample> trainingSetGFD = splitDataSetGFD[0];
        List<Sample> trainingSetF0 = splitDataSetF0[0];

        // Recovering the test sets.
        List<Sample> testSetSA = splitDataSetSA[1];
        List<Sample> testSetE34 = splitDataSetE34[1];
        List<Sample> testSetGFD = splitDataSetGFD[1];
        List<Sample> testSetF0 = splitDataSetF0[1];


        /*--------------------------------------- tests ---------------------------------------*/

        System.out.println("================================== KNN CLASSIFICATION ============================================\n");

        System.out.println("Résultats de la Méthode E34");

        Map<String, Double> bestHyperparametersE34 = getHyperparameters(trainingSetE34);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        KNNClassifier.evaluateModelOnTestSet(trainingSetE34, testSetE34, bestHyperparametersE34);

        System.out.println("Résultats de la Méthode F0");

        Map<String, Double> bestHyperparametersF0 = getHyperparameters(trainingSetF0);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        KNNClassifier.evaluateModelOnTestSet(trainingSetF0, testSetF0, bestHyperparametersF0);

        System.out.println("Résultats de la Méthode GFD");

        Map<String, Double> bestHyperparametersGFD = getHyperparameters(trainingSetGFD);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        KNNClassifier.evaluateModelOnTestSet(trainingSetGFD, testSetGFD, bestHyperparametersGFD);

        System.out.println("Résultats de la Méthode SA");

        Map<String, Double> bestHyperparametersSA = getHyperparameters(trainingSetSA);

        // Utilisation des meilleurs hyperparamètres pour évaluer le modèle sur le testSet
        KNNClassifier.evaluateModelOnTestSet(trainingSetSA, testSetSA, bestHyperparametersSA);

        System.out.println("================================== K-Means Classifier ==============================================");

        int k = 9;
        int maxIterations = 100;
        boolean usingPP = true;
        int distanceNorm = 2;
        int randomSeed = 123;
        int maxEvaluationIterations = 100;

        // randomSeed parameter is optional.
        KMeansClassifier kMeansClassifierSA = ClassifierUtilities.computeBestKmeansModel
                (maxEvaluationIterations, k, dataSetSA, usingPP, maxIterations, distanceNorm);
        KMeansClassifier kMeansClassifierE34 = ClassifierUtilities.computeBestKmeansModel
                (maxEvaluationIterations, k, dataSetE34, usingPP, maxIterations, distanceNorm);
        KMeansClassifier kMeansClassifierGFD = ClassifierUtilities.computeBestKmeansModel
                (maxEvaluationIterations, k, dataSetGFD, usingPP, maxIterations, distanceNorm);
        KMeansClassifier kMeansClassifierF0 = ClassifierUtilities.computeBestKmeansModel
                (maxEvaluationIterations, k, dataSetF0, usingPP, maxIterations, distanceNorm);

        kMeansClassifierSA.printEvaluations();
        kMeansClassifierE34.printEvaluations();
        kMeansClassifierGFD.printEvaluations();
        kMeansClassifierF0.printEvaluations();
    }
}