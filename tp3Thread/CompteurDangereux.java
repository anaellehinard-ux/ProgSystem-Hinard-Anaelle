public class CompteurDangereux {
    private static int compteurGlobal = 0;  // Variable partagée MODIFIABLE

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
                /**
                 * TODO: Expliquer pourquoi cette ligne pose problème
                 * 
                 * L'instruction compteurGlobal++ se décompose en 3 (assembleur) : 
                 *   1) Lecture de la valeur = LOAD
                 *   2) Ajout de 1 = ADD
                 *   3) Réécriture de la valeur = STORE
                 * 
                 * Si 2 threads exécutent cette séquence en même temps, ils peuvent lire
                 * la même valeur initiale avant d'avoir pu écrire le résultat.
                 * 
                 * L'une des incrémentations sera alors écrasée, donnant
                 * une race condition et un résultat final cassé.
                 */
                compteurGlobal++;  // RACE CONDITION ICI !
            }
            System.out.println(nom + " terminé. Compteur vu : " + compteurGlobal);
        }
    }

    public static int getCompteur() {
        return compteurGlobal;
    }

    public static void resetCompteur() {
        compteurGlobal = 0;
    }
}
