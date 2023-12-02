import data.Sample;
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
        List<Sample> dataSetE34 = readerE34.getDataSet();
        List<Sample> dataSetF0 = readerF0.getDataSet();
        List<Sample> dataSetGFD = readerGFD.getDataSet();
        List<Sample> dataSetSA = readerSA.getDataSet();


        /*--------------------------------------- tests ---------------------------------------*/

        DataReader.printFeatures(dataSetGFD);

        Sample sample1 = dataSetGFD.get(0);
        Sample sample2 = dataSetGFD.get(1);

        System.out.println(sample1.isLabelEqualTo(sample2));

        System.out.println("Minkowski distance between sample 1 and sample 2 for p=2: "
                + sample1.calculateMinkowskiDistance(sample2,2));

        Sample sampleN = dataSetGFD.get(dataSetGFD.size() - 1);

        System.out.println(sample1.isLabelEqualTo(sampleN));

        System.out.println("Sample 1's image class: " + sample1.getLabelNumber()
                + "\nSample 99's image class: " + sampleN.getLabelNumber());
    }
}
