import classifiers.ClassifierUtilities;
import classifiers.KNNClassifier;
import data.Pattern;
import input_output.DataReader;

import java.util.List;

public class App {

    public static void main(String[] args) {

        // Initializing a DataReader for each image analysis method applied on BDshape.
        DataReader readerE34 = new DataReader("../data/E34/", ".e34");
        DataReader readerF0 = new DataReader("../data/F0/", ".f0");
        DataReader readerGFD = new DataReader("../data/GFD/", ".gfd");
        DataReader readerSA = new DataReader("../data/SA/", ".sa");

        // Initializing datasets for each image analysis method.
        List<Pattern> dataSetE34 = readerE34.getDataSet();
        List<Pattern> dataSetF0 = readerF0.getDataSet();
        List<Pattern> dataSetGFD = readerGFD.getDataSet();
        List<Pattern> dataSetSA = readerSA.getDataSet();


        /*--------------------------------------- tests ---------------------------------------*/

        //DataReader.printFeatures(dataSetGFD);

        Pattern pattern1 = dataSetGFD.get(0);
        Pattern pattern2 = dataSetGFD.get(1);

        List<Pattern> trainSet = ClassifierUtilities.splitData(dataSetGFD, 0.8f)[0];
        List<Pattern> testSet = ClassifierUtilities.splitData(dataSetGFD, 0.8f)[1];

        KNNClassifier classifier = new KNNClassifier(trainSet);
        for(Pattern testSample : testSet)
        {
            int labelGuessed = classifier.classify(testSample, 10);
            System.out.println("Classe des knn : " +labelGuessed + ". Classe réelle : " + testSample.getLabelNumber());
        }

//        System.out.println(pattern1.isLabelEqualTo(pattern2));
//
//        System.out.println("Minkowski distance between pattern 1 and pattern 2 for p=2: "
//                + pattern1.calculateMinkowskiDistance(pattern2,2));
//
//        Pattern patternN = dataSetGFD.get(dataSetGFD.size() - 1);
//
//        System.out.println(pattern1.isLabelEqualTo(patternN));
//
//        System.out.println("Pattern 1's image class: " + pattern1.getLabelNumber()
//                + "\nPattern 99's image class: " + patternN.getLabelNumber());
    }
}
