import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

private static final int TAILLE_BUFFER = 5;
private final Integer[] buffer = new Integer[TAILLE_BUFFER];

// TODO: Déclarer 3 sémaphores avec les bonnes valeurs initiales
private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
private final Semaphore elementsDisponibles = new Semaphore(0);  
private final Semaphore mutexBuffer = new Semaphore(1); // quel erreur ici ? vous pouvez le résoudre plus tard

class Producteur implements Runnable {
    public void run() {
        for (int i = 0; i < 10; i++) {
            int produit = genererProduit();

            try {
				// Vérifie s'il y a une place libre (bloque si buffer plein)
				placesLibres.acquire();

				// Récupère l'accès exclusif au buffer  
				mutexBuffer.acquire();

				// Ajoute l'élément dans le buffer
				ajoutElementAuBuffer(produit);

				// Libère l'accès au buffer
				mutexBuffer.release();

				// Signale qu'un élément est disponible pour les consommateurs
				elementsDisponibles.release();

			} catch (InterruptedException e) {
				return; //arrêt simple
			}
        }
    }
	
	/**
	 * Génère un nombre aléatoire entre 0 et 99
	 */
	private int genererProduit() {
		double aleatoire;
		aleatoire = Math.random() * 100;
        return (int) aleatoire;
    }

	private int index = 0;

	private void ajoutElementAuBuffer(int item) {
		buffer[index] = item;
		index = (index + 1) % TAILLE_BUFFER;
		System.out.println("Le produit ajouté est : " + item);
	}
