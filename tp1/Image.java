import java.io.FileWriter;
import java.io.IOException;

public class Image {
    private int width;
    private int height;
    // pixels[y][x][0=R,1=G,2=B]
    private int[][][] pixels;

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /**
     * Constructeur : initialise une image vide.
     */
    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height][width][3];
    }

    /**
     * Définit la couleur d'un pixel à la position (x, y)
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[y][x][0] = r;
            pixels[y][x][1] = g;
            pixels[y][x][2] = b;
        }
    }

    /**
	 * Sauvegarde l'image au format texte PPM (P3)
	 */
	public void save_txt(String filename) {
		try (FileWriter writer = new FileWriter(filename)) {
			// En-tête PPM (P3) : Nombre magique, dimensions, et valeur max de couleur
			writer.write("P3\n");
			writer.write(width + " " + height + "\n");
			writer.write("255\n");

			// Écriture des données RGB pixel par pixel
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					writer.write(pixels[y][x][0] + " " + 
								 pixels[y][x][1] + " " + 
								 pixels[y][x][2] + " ");
				}
				writer.write("\n");
			}
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde du fichier : " + e.getMessage());
		}
	}
	
	/**
	 * Lit un fichier PPM (P3) et retourne un objet Image correspondant.
	 * Gère les exceptions en interne et renvoie null en cas d'erreur.
	 */
	public static Image read_txt(String nomFichier) {
		File fichier = new File(nomFichier);
		
		try (Scanner scanner = new Scanner(fichier)) {
			
			String entete = prochainToken(scanner);
			if (!entete.equals("P3")) {
				System.err.println("Format non supporté (seul P3 est accepté) : " + entete);
				return null;
			}

			int largeur = Integer.parseInt(prochainToken(scanner));
			int hauteur = Integer.parseInt(prochainToken(scanner));
			int valeurMax = Integer.parseInt(prochainToken(scanner));

			Image image = new Image(largeur, hauteur);

			for (int y = 0; y < hauteur; y++) {
				for (int x = 0; x < largeur; x++) {
					int r = Integer.parseInt(prochainToken(scanner));
					int g = Integer.parseInt(prochainToken(scanner));
					int b = Integer.parseInt(prochainToken(scanner));

					// Normalisation si la valeur max est différente de 255
					if (valeurMax != 255) {
						r = (r * 255) / valeurMax;
						g = (g * 255) / valeurMax;
						b = (b * 255) / valeurMax;
					}

					image.setPixel(x, y, r, g, b);
				}
			}

			return image;

		} catch (IOException e) {
			System.err.println("Erreur de lecture du fichier : " + e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println("Erreur lors du traitement du fichier PPM : " + e.getMessage());
			return null;
		}
	}

	/**
	 * Méthode utilitaire pour lire le prochain élément en ignorant les commentaires
	 */
	private static String prochainToken(Scanner scanner) {
		while (scanner.hasNext()) {
			String token = scanner.next();
			if (token.startsWith("#")) {
				scanner.nextLine(); // Ignore la ligne de commentaire
			} else {
				return token;
			}
		}
		throw new IllegalArgumentException("Fin de fichier inattendue");
	}
}