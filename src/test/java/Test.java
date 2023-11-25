import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {

    public double lireMesure(String nomFichier) {
        double mesure = 0.0;

        try (BufferedReader lecteur = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            if ((ligne = lecteur.readLine()) != null) {
                // Supposons que la mesure est sur la première ligne du fichier
                mesure = Double.parseDouble(ligne);
                
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return mesure;
    }

    public static void main(String[] args) {
        
        double tableau[] = new double[16];

        for (int i = 0; i < tableau.length; i++) {
            
        }

        


    }
}