import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSemaphore {

	static final AtomicInteger produits = new AtomicInteger();
	static final AtomicInteger consommes = new AtomicInteger();

	static void demarrer(Runnable r, String nom) {
		Thread t = new Thread(r, nom);
		t.setDaemon(true);
		t.start();
	}

	static void scenario(String nom, int nbProd, int nbCons) throws InterruptedException {
		System.out.println("=== " + nom + " ===");
		produits.set(0);
		consommes.set(0);
		ProducteurConsommateur pc = new ProducteurConsommateur();

		for (int i = 1; i <= nbProd; i++) demarrer(pc.new Producteur(), "Producteur-" + i);
		for (int i = 1; i <= nbCons; i++) demarrer(pc.new Consommateur(), "Consommateur-" + i);

		for (int i = 0; i < 10; i++) {
			Thread.sleep(300);
			int dansBuffer = produits.get() - consommes.get();
			System.out.println("[MONITORING] produits=" + produits + " consommés=" + consommes
					+ " buffer=" + dansBuffer + "/5");
		}
		int diff = produits.get() - consommes.get();
		System.out.println("Cohérent : " + (diff >= 0 && diff <= 5) + "\n");
	}

	public static void main(String[] args) throws InterruptedException {
		// Les affichages de ProducteurConsommateur sont comptés (et ralentis) au lieu d'être imprimés
		System.setOut(new PrintStream(System.out) {
			@Override
			public void println(String s) {
				if (s.startsWith("Le produit")) produits.incrementAndGet();
				else if (s.startsWith("Produit cons")) consommes.incrementAndGet();
				else { super.println(s); return; }
				try { Thread.sleep(30); } catch (InterruptedException e) { }
			}
		});

		scenario("ÉQUILIBRÉ (2 prod, 3 cons)", 2, 3);
		scenario("DÉSÉQUILIBRÉ (5 prod, 2 cons)", 5, 2);
	}
}
