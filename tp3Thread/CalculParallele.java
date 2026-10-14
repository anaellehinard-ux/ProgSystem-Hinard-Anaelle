public class CalculParallele {

    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;

    public static void main(String[] args) {

        long debut = System.nanoTime();

        Calcule[] threads = new Calcule[DONNEES.length];
        for (int i = 0; i < DONNEES.length; i++) {
            //appel du constructeur de la classe Calcule
            // stockage dans threads
            threads[i] = new Calcule(DONNEES[i], MULTIPLICATEUR, i);
        }

        for (int i = 0; i < threads.length; i++){
            threads[i].start();
        }

        int sommeTotal = 0;
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                System.out.print("Error du Threads");
            }
            
            sommeTotal += threads[i].getResultat();
        }

        long duree = (System.nanoTime() - debut) / 1_000_000;
        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
}