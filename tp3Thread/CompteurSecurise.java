public class CompteurSecurise {

    private static int compteurGlobal = 0;

    //Objet verrou explicite (Mutex)
    private static final Object verrou = new Object();

    static class Incrementeur implements Runnable {
        private final String nom;
        private final int nombreIncrements;

        public Incrementeur(String nom, int nombreIncrements) {
            this.nom = nom;
            this.nombreIncrements = nombreIncrements;
        }

        @Override
        public void run() {
            for (int i = 0; i < nombreIncrements; i++) {
                //Protection de la section critique avec synchronized
                synchronized (verrou) {
                    compteurGlobal++;
                }
            }
            System.out.println(nom + " finis. Compteur vu : " + getCompteur());
        }
    }

    //Protection des méthodes de lecture et de réinitialisation
    public static int getCompteur() {
        synchronized (verrou) {
            return compteurGlobal;
        }
    }

    public static void resetCompteur() {
        synchronized (verrou) {
            compteurGlobal = 0;
        }
    }
}