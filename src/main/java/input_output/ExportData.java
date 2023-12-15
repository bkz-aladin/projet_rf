package input_output;

import data.Sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportData {
    public static void exportToCSV(List<Sample> dataset, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Sample sample: dataset) {

                //System.out.println(dataset.get(sampleIndex));
                //System.exit(0);
                //for (Float measure : sample.getMeasures()) {

                //List<> features = sample.getFeatures();
                String data = String.valueOf(sample.getFeatures());
                // Concaténation des éléments de la liste interne avec des virgules et écriture dans le fichier
                writer.write(String.join(",", data));
                writer.newLine();
                //}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void exportLabelsToCSV(List<Sample> samples, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Écrire l'en-tête CSV (si nécessaire)
            writer.write("Label"); // Ajoutez le nom de votre colonne de labels
            writer.newLine();

            // Écrire les labels des échantillons dans le fichier CSV
            for (Sample sample : samples) {
                int label = sample.getLabelNumber();
                writer.write(String.valueOf(label));
                writer.newLine();
            }

            System.out.println("Exportation réussie vers " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
