import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LecteurDonnees {

    private String cheminVersFichier;
    private String extension;
    private int nombreMesures;

    public LecteurDonnees(String cheminVersFichier, int nombreMesures, String extension) {
        this.cheminVersFichier = cheminVersFichier;
        this.extension = extension;
        this.nombreMesures = nombreMesures;
    }

    // Méthode pour lire toutes les mesures à partir d'un fichier
    public double[] lireMesure(String nomFichier) {
        List<Double> mesures = new ArrayList<>();

        try (BufferedReader lecteur = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                mesures.add(Double.parseDouble(ligne));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // Convertir la liste en tableau de doubles
        double[] tableauMesures = new double[mesures.size()];
        for (int i = 0; i < mesures.size(); i++) {
            tableauMesures[i] = mesures.get(i);
        }

        return tableauMesures;
    }

    // Méthode pour lire les mesures à partir des fichiers
    public void lireMesures(double[][][] tableauMesures) {
        for (int classe = 0; classe < tableauMesures.length; classe++) {
            for (int image = 0; image < tableauMesures[classe].length; image++) {
                tableauMesures[classe][image] = lireMesure(cheminVersFichier + "S" + String.format("%02d", classe + 1) + "N" + String.format("%03d", image + 1) + this.extension);
            }
        }
    }

    // Méthode pour afficher le tableau
    public void afficherMesures(double[][][] tableau) {
        System.out.println("Affichage du tableau de mesures :");
        for (int classe = 0; classe < tableau.length; classe++) {

            System.out.println("classe " + (classe+1));
            for (int image = 0; image < tableau[classe].length; image++) {

                for (int mesure = 0; mesure < tableau[classe][image].length; mesure++) {

                    System.out.print(tableau[classe][image][mesure] + " ");
                }
                System.out.println(); // Passer à la ligne après chaque ligne d'image
            }
            System.out.println(); // Passer à la ligne après chaque classe
            // System.out.println("classe " + (classe+1)); 
        }
    }
}
