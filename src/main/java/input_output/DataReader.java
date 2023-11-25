package input_output;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {

    // Constants
    static final int AMOUNT_OF_CLASSES = 9;
    static final int AMOUNT_OF_SAMPLES = 11;

    // Class variables
    private final String filePath;
    private final String fileExtension;

    public DataReader(String filePath, String fileExtension) {
        this.filePath = filePath;
        this.fileExtension = fileExtension;
    }

    // Méthode pour lire toutes les mesures à partir d'un fichier
    public ArrayList<Double> readMeasuresOfMethod(String fileName) {
        ArrayList<Double> measures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                measures.add(Double.parseDouble(line));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return measures;
    }

    // Méthode pour lire les mesures à partir des fichiers
    public ArrayList<ArrayList<ArrayList<Double>>> readMeasures() {
        ArrayList<ArrayList<ArrayList<Double>>> measures = new ArrayList<>();

        for (int classIndex = 0; classIndex < AMOUNT_OF_CLASSES; classIndex++) {
            measures.add(new ArrayList<>());
            for (int sampleIndex = 0; sampleIndex < AMOUNT_OF_SAMPLES; sampleIndex++) {
                measures.get(classIndex).add(new ArrayList<>(readMeasuresOfMethod(
                        this.filePath
                                + "S" + String.format("%02d", classIndex + 1)
                                + "N" + String.format("%03d", sampleIndex + 1)
                                + this.fileExtension)));
            }
        }

        return measures;
    }

    // Méthode pour afficher le tableau
    public void printMeasures(ArrayList<ArrayList<ArrayList<Double>>> measures) {
        System.out.println("Affichage du tableau de mesures :");

        int classIndex = 1;
        for (ArrayList<ArrayList<Double>> imageClass : measures) {
            System.out.println("classe " + classIndex++);

            for (ArrayList<Double> imageSample : imageClass) {
                for (Double measure : imageSample) {
                    System.out.print(measure + " ");
                }
                System.out.println(); // Passer à la ligne après chaque ligne d'image
            }
            System.out.println(); // Passer à la ligne après chaque classe
        }
    }
}
