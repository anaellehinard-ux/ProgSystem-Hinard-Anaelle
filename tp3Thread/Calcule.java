public class Calcule extends Thread {
    
    private int valeur;
    private int multiplicateur;
    private int index;
    private int resultat;

    public Calcule (int val, int mult, int indx) {
        this.valeur = val;
        this.multiplicateur = mult;
        this.index = indx;
    }

    @Override
    public void run() {
        int res = 0;
        for (int i = 0; i < multiplicateur; i++) {
            res += valeur * valeur + valeur;
        }

        this.resultat = res;

        System.out.println("Traitement de " + valeur + " terminé : " + resultat);
    }


    public int getResultat(){
        return this.resultat;
    }
}