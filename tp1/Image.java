import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
    public void save_txt(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
		writer.write("P3\n");
		writer.write("3 2\n");
		writer.write("255\n");
		
		for(int x = 0; x < height; x++){
			for(int y = 0; y < width; y++){
				
				int a = pixels[x][y][0];
				int b = pixels[x][y][1];
				int c = pixels[x][y][2];
				
				writer.write(a + " " + b + " " + c + " ");
			}
			writer.write("\n");
		}
		writer.close();
    }

    /**
     * Lit un fichier texte PPM (P3)
     */
    public static Image read_txt(String filename) throws IOException {
        Scanner lecture = new Scanner(new File(filename));

        String formatDuFichier = lecture.next();
        if (!formatDuFichier.equals("P3")) {
            lecture.close();
            throw new IllegalArgumentException("Format non conforme");
        }

        int width = lecture.nextInt(); // la largeur
        int height = lecture.nextInt(); // la hauteur
        int color = lecture.nextInt(); // le "255"

        Image imageLu = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = lecture.nextInt();
                int b = lecture.nextInt();
                int c = lecture.nextInt();
                imageLu.setPixel(x, y, a, b, c);
            }
        }
        lecture.close();
        return imageLu;
    }

    /**
     * Lit un fichier binaire PPM (P6)
     */
    public static Image read_bin(String filename) throws IOException {
        FileInputStream fichierLu = new FileInputStream(filename);
        Scanner lecture = new Scanner(fichierLu);

        String formatDuFichier = lecture.next();
        if (!formatDuFichier.equals("P6")) {
            lecture.close();
            fichierLu.close();
            throw new IllegalArgumentException("Format non conforme, attendu: P6");
        }

        int width = lecture.nextInt();
        int height = lecture.nextInt();
        int color = lecture.nextInt();

        lecture.nextLine();

        Image imageLu = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = fichierLu.read();
                int b = fichierLu.read();
                int c = fichierLu.read();
                imageLu.setPixel(x, y, a, b, c);
            }
        }

        lecture.close();
        fichierLu.close();
        return imageLu;
    }

    /**
     * Sauvegarde l'image au format binaire PPM (P6)
     */
    public void write_bin(String filename) throws IOException {
        FileOutputStream fichierEcrit = new FileOutputStream(filename);

        String header = "P6\n" + width + " " + height + "\n255\n";
        fichierEcrit.write(header.getBytes());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fichierEcrit.write((byte) pixels[y][x][0]); // R
                fichierEcrit.write((byte) pixels[y][x][1]); // G
                fichierEcrit.write((byte) pixels[y][x][2]); // B
            }
        }
        fichierEcrit.close();
    }
}
