public class App {

    public static void main(String[] args) {
        // Supposons que vous avez 9 classes, chaque classe ayant 11 images et chaque image a 16 mesures E34
        int nombreClasses = 9;
        int nombreImagesParClasse = 11;
        int nombreMesuresE34 = 16;

        // Déclaration et initialisation d'un tableau à trois dimensions
        double[][][] mesuresE34 = new double[nombreClasses][nombreImagesParClasse][nombreMesuresE34];

        // Création d'une instance de LecteurDonnees pour les mesures E34
        LecteurDonnees lecteurE34 = new LecteurDonnees("../data/E34/", nombreMesuresE34);

        // Utilisation de l'instance pour lire les mesures E34
        lecteurE34.lireMesures(mesuresE34);

        // System.out.println(mesuresE34);
        // Affichage des mesures E34
        // lecteurE34.lireMesures(mesuresE34);

        // Affichage du tableau
        afficherTableau(mesuresE34);
    }

    private static void afficherTableau(double[][][] tableau) {
        System.out.println("Affichage du tableau de mesures :");
        for (int classe = 0; classe < tableau.length; classe++) {
            for (int image = 0; image < tableau[classe].length; image++) {
                for (int mesure = 0; mesure < tableau[classe][image].length; mesure++) {
                    System.out.print(tableau[classe][image][mesure] + " ");
                }
                System.out.println(); // Passer à la ligne après chaque ligne d'image
            }
            System.out.println(); // Passer à la ligne après chaque classe
        }
    }
}
