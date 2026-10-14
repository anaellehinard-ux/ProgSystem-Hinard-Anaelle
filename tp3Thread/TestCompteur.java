public class TestCompteur {

    public static void main(String[] args) {
        int nbTests = 50;                  // 50 répétitions
        int nbThreads = 9;                 // 9 threads
        int incrementsParThread = 1111111;  //1 111 111 incréments par thread

        int calculAttendu = nbThreads * incrementsParThread; // 9 999 999

        // Variables pour Étape 4
        int reussites = 0;
        int minObtenu = Integer.MAX_VALUE;
        int maxObtenu = Integer.MIN_VALUE;

        System.out.println("=== DÉBUT DES TESTS (" + nbTests + " répétitions) ===");
        System.out.println("Configuration : " + nbThreads + " threads x " + incrementsParThread + " incréments");
        System.out.println("Valeur attendue à chaque test : " + calculAttendu + "\n");

        for (int test = 1; test <= nbTests; test++) {
            //Réinitialisation du compteur
            CompteurDangereux.resetCompteur();

            //Création des threads
            Thread[] threads = new Thread[nbThreads];
            for (int i = 0; i < nbThreads; i++) {
                CompteurDangereux.Incrementeur worker = 
                    new CompteurDangereux.Incrementeur("Thread-" + i, incrementsParThread);
                threads[i] = new Thread(worker);
            }

            // Démarrage des threads
            for (int i = 0; i < nbThreads; i++) {
                threads[i].start();
            }

            //Attente avec join()
            for (int i = 0; i < nbThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Étape 3 : Récupération et comparaison du résultat
            int resultat = CompteurDangereux.getCompteur();

            // Mise à jour des statistiques pour Étape 4
            if (resultat == calculAttendu) {
                reussites++;
            }
            if (resultat < minObtenu) {
                minObtenu = resultat;
            }
            if (resultat > maxObtenu) {
                maxObtenu = resultat;
            }

            System.out.println("Test #" + test + " -> Obtenu : " + resultat + 
                               " | Écart : " + (calculAttendu - resultat));
        }

        // Étape 4 : Affichage des statistiques finales
        double tauxReussite = ((double) reussites / nbTests) * 100;

        System.out.println("\n================ STATISTIQUES FINALES ================");
        System.out.println("Nombre de réussites : " + reussites + " / " + nbTests);
        System.out.println("Taux de réussite   : " + String.format("%.2f", tauxReussite) + " %");
        System.out.println("Valeur minimale    : " + minObtenu);
        System.out.println("Valeur maximale    : " + maxObtenu);
        System.out.println("======================================================");
    }
}