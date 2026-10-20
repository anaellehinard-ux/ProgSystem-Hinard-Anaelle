import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

private static final int TAILLE_BUFFER = 5;
private final Integer[] buffer = new Integer[TAILLE_BUFFER];

// TODO: Déclarer 3 sémaphores avec les bonnes valeurs initiales
private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
private final Semaphore elementsDisponibles = new Semaphore(0);  
private final Semaphore mutexBuffer = new Semaphore(1); // quel erreur ici ? vous pouvez le résoudre plus tard

private int indexDeProd = 0;
private int indexDeConso = 0;

public class ProducteurConsommateur {
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


		private void ajoutElementAuBuffer(int item) {
			buffer[indexDeProd] = item;
			indexDeProd = (indexDeProd + 1) % TAILLE_BUFFER;
			System.out.println("Le produit ajouté est : " + item);
		}
	}

	class Consommateur implements Runnable {
		public void run() {
			for (int i = 0; i < 7; i++) {
				try {
					// Vérifie qu'un élément soit disponible (bloque si vide)
					elementsDisponibles.acquire();

					// Récupère l'accès exclusif au buffer 
					mutexBuffer.acquire();

					// Retire l'élément dans le buffer
					int produit = retirerElementAuBuffer();

					// Libère l'accès au buffer
					mutexBuffer.release();

					// Signale aux producteurs qu'une place s'est libérée
					placesLibres.release();
					
				} catch (InterruptedException e) {
					return; //arrêt simple
				}
			}
		}
		
		private int retirerElementAuBuffer() {
			int item = buffer[indexDeConso];
			indexDeConso = (indexDeConso + 1) % TAILLE_BUFFER;
			System.out.println("Produit consommé : " + item);
			return item;
		}
	}
}

