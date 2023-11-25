import input_output.DataReader;

import java.util.ArrayList;

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
        ArrayList<ArrayList<ArrayList<Double>>> measuresE34 = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> measuresF0 = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> measuresGFD = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> measuresSA = new ArrayList<>();

        // Création d'une instance de LecteurDonnees pour les mesures E34
        DataReader readerE34 = new DataReader("../data/E34/", ".e34");
        DataReader readerF0 = new DataReader("../data/F0/", ".f0");
        DataReader readerGFD = new DataReader("../data/GFD/", ".gfd");
        DataReader readerSA = new DataReader("../data/SA/", ".sa");

        // Utilisation de l'instance pour lire les mesures E34
        measuresE34 = readerE34.readMeasures();
        measuresF0 = readerF0.readMeasures();
        measuresGFD = readerGFD.readMeasures();
        measuresSA = readerSA.readMeasures();

        // System.out.println(measuresE34);
        // readerE34.afficherMesures(measuresE34);
        // readerF0.afficherMesures(measuresF0);
        readerGFD.printMeasures(measuresGFD);
        // readerSA.afficherMesures(measuresSA);

        // Affichage du tableau
        // afficherTableau(measuresE34);
    }

    
}
