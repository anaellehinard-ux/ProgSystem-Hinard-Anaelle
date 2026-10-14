public class TestMutex {

    public static void main(String[] args) throws InterruptedException {
        int nbTests = 50;
        int nbThreads = 9;
        int increments = 1111111;
        int attendu = nbThreads * increments;

        int reussites = 0;
        int minObtenu = Integer.MAX_VALUE;
        int maxObtenu = Integer.MIN_VALUE;

        long debut = System.currentTimeMillis();

        for (int test = 1; test <= nbTests; test++) {
            CompteurSecurise.resetCompteur();

            // Création et lancement des threads
            Thread[] threads = new Thread[nbThreads];
            for (int i = 0; i < nbThreads; i++) {
                threads[i] = new Thread(new CompteurSecurise.Incrementeur("Thread-" + i, increments));
                threads[i].start();
            }

            // Attente de la fin des threads
            for (Thread t : threads) {
                t.join();
            }

            int res = CompteurSecurise.getCompteur();
            if (res == attendu) reussites++;
            if (res < minObtenu) minObtenu = res;
            if (res > maxObtenu) maxObtenu = res;

            System.out.println("Test #" + test + " -> Obtenu : " + res);
        }

        long duree = System.currentTimeMillis() - debut;

        // Statistiques
        System.out.println("\n=== STATISTIQUES FINALES (MUTEX) ===");
        System.out.println("Réussites : " + reussites + " / " + nbTests + " (" + ((reussites * 100.0) / nbTests) + "%)");
        System.out.println("Min : " + minObtenu + " | Max : " + maxObtenu);
        System.out.println("Temps total : " + duree + " ms");
    }
}