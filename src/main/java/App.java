import data.Sample;
import input_output.DataReader;

import java.util.List;

public class App {

    public static void main(String[] args) {
        /*
        // Initialisation des variables nécessaires à la définition de BDshape et de ses mesures
        int amountOfClasses = 9;
        int amountOfSamples = 11;
        int amountOfMeasuresE34 = 16;
        int amountOfMeasuresF0 = 128;
        int amountOfMeasuresGFD = 100;
        int amountOfMeasuresSA = 90;
         */

        // Déclaration et initialisation d'un tableau à trois dimensions
        List<Sample> dataSetE34;
        List<Sample> dataSetF0;
        List<Sample> dataSetGFD;
        List<Sample> dataSetSA;

        // Création d'une instance de LecteurDonnees pour les mesures E34
        DataReader readerE34 = new DataReader("../data/E34/", ".e34");
        DataReader readerF0 = new DataReader("../data/F0/", ".f0");
        DataReader readerGFD = new DataReader("../data/GFD/", ".gfd");
        DataReader readerSA = new DataReader("../data/SA/", ".sa");

        // Utilisation de l'instance pour lire les mesures E34
        dataSetF0 = readerE34.getDataSet();
        dataSetF0 = readerF0.getDataSet();
        dataSetGFD = readerGFD.getDataSet();
        dataSetSA = readerSA.getDataSet();

        /***********************************************************************************/
        /** tests **/
        DataReader.printMeasures(dataSetGFD);

        Sample sample1 = dataSetGFD.get(0);
        Sample sample2 = dataSetGFD.get(1);
        System.out.println(sample1.isLabelEqualTo(sample2));

        Sample sampleN = dataSetGFD.get(dataSetGFD.size() - 1);
        System.out.println(sample1.isLabelEqualTo(sampleN));
        System.out.println("Sample 1's class: " + sample1.getLabelNumber()
                + "\nSample 99's class: " + sampleN.getLabelNumber());
    }
}
