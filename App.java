public class App {

    public static void main(String[] args) {
        // Supposons que vous avez 9 classes, chaque classe ayant 11 images et chaque image a 16 mesures E34
        int nombreClasses = 9;
        int nombreImagesParClasse = 11;
        int nombreMesuresE34 = 16;
        int nombreMesuresF0 = 128;
        int nombreMesuresGFD = 100;
        int nombreMesuresSA = 90;

        // Déclaration et initialisation d'un tableau à trois dimensions
        double[][][] mesuresE34 = new double[nombreClasses][nombreImagesParClasse][nombreMesuresE34];
        double[][][] mesuresF0 = new double[nombreClasses][nombreImagesParClasse][nombreMesuresF0];
        double[][][] mesuresGFD = new double[nombreClasses][nombreImagesParClasse][nombreMesuresGFD];
        double[][][] mesuresSA = new double[nombreClasses][nombreImagesParClasse][nombreMesuresSA];

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
