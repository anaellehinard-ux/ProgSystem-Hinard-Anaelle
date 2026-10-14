public class CalculParallele {

    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;

    Calcule[] threads = new Calcule[DONNEES.length];

    
    for (int index = 0; index < DONNEES.length; i++) {
        //appel du constructeur de la classe Calcule
        // stockage dans threads
        threads[i] = new Calcule(DONNEES[i], MULTIPLICATEUR, index);
    }

    for (int i = 0; i < threads.length; i++){
        threads[i].start();
    }

    for (int i = 0; i < threads.length; i++) {
        try {
            threads[i].join();
        } catch (Exception e) {
            System.out.print("Error du Threads");
        }
        int sommeTotal;
        sommeTotal += threads[i].getResultat();
    }
}