import input_output.LecteurDonnees;

public class App {

    public static void main(String[] args) {
        // Initialisation des variables nécessaires à la définition de BDshape et de ses mesures
        int nombreClasses = 9;
        int nombreEchantillons = 11;
        int nombreMesuresE34 = 16;
        int nombreMesuresF0 = 128;
        int nombreMesuresGFD = 100;
        int nombreMesuresSA = 90;

        // Déclaration et initialisation d'un tableau à trois dimensions
        double[][][] mesuresE34 = new double[nombreClasses][nombreEchantillons][nombreMesuresE34];
        double[][][] mesuresF0 = new double[nombreClasses][nombreEchantillons][nombreMesuresF0];
        double[][][] mesuresGFD = new double[nombreClasses][nombreEchantillons][nombreMesuresGFD];
        double[][][] mesuresSA = new double[nombreClasses][nombreEchantillons][nombreMesuresSA];

        // Création d'une instance de LecteurDonnees pour les mesures E34
        LecteurDonnees lecteurE34 = new LecteurDonnees("../data/E34/", nombreMesuresE34, ".e34");
        LecteurDonnees lecteurF0 = new LecteurDonnees("../data/F0/", nombreMesuresF0, ".f0");
        LecteurDonnees lecteurGFD = new LecteurDonnees("../data/GFD/", nombreMesuresGFD, ".gfd");
        LecteurDonnees lecteurSA = new LecteurDonnees("../data/SA/", nombreMesuresSA, ".sa");

        // Utilisation de l'instance pour lire les mesures E34
        lecteurE34.lireMesures(mesuresE34);
        lecteurF0.lireMesures(mesuresF0);
        lecteurGFD.lireMesures(mesuresGFD);
        lecteurSA.lireMesures(mesuresSA);

        // System.out.println(mesuresE34);
        // lecteurE34.afficherMesures(mesuresE34);
        // lecteurF0.afficherMesures(mesuresF0);
        lecteurGFD.afficherMesures(mesuresGFD);
        // lecteurSA.afficherMesures(mesuresSA);

        // Affichage du tableau
        // afficherTableau(mesuresE34);
    }

    
}
