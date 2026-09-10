import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Image {
    private int width;
    private int height;
    // pixels[y][x][0=R, 1=G, 2=B]
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

    // --- Getters de couleur par pixel ---

    public int getPixelRed(int x, int y) {
        return pixels[y][x][0];
    }

    public int getPixelGreen(int x, int y) {
        return pixels[y][x][1];
    }

    public int getPixelBlue(int x, int y) {
        return pixels[y][x][2];
    }

    // --- Format Texte (P3) ---

    /**
     * Sauvegarde l'image au format texte PPM (P3)
     */
    public void save_txt(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("P3\n");
            writer.write(width + " " + height + "\n");
            writer.write("255\n");

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
     * Lit un fichier PPM texte (P3)
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

    // --- Format Binaire (P6) ---

    /**
     * Sauvegarde l'image au format binaire PPM (P6)
     */
    public void write_bin(String filename) {
        try (OutputStream out = new FileOutputStream(filename)) {
            String header = "P6\n" + width + " " + height + "\n255\n";
            out.write(header.getBytes());

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    out.write((byte) pixels[y][x][0]);
                    out.write((byte) pixels[y][x][1]);
                    out.write((byte) pixels[y][x][2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde binaire : " + e.getMessage());
        }
    }

    /**
     * Lit un fichier PPM binaire (P6)
     */
    public static Image read_bin(String filename) {
        try (InputStream in = new FileInputStream(filename)) {
            String entete = lireProchainMotASCII(in);
            if (!entete.equals("P6")) {
                System.err.println("Format non supporté (seul P6 est accepté) : " + entete);
                return null;
            }

            int largeur = Integer.parseInt(lireProchainMotASCII(in));
            int hauteur = Integer.parseInt(lireProchainMotASCII(in));
            int valeurMax = Integer.parseInt(lireProchainMotASCII(in));

            Image image = new Image(largeur, hauteur);

            for (int y = 0; y < hauteur; y++) {
                for (int x = 0; x < largeur; x++) {
                    int r = in.read();
                    int g = in.read();
                    int b = in.read();

                    if (r == -1 || g == -1 || b == -1) {
                        throw new IOException("Fin de fichier binaire inattendue");
                    }

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
            System.err.println("Erreur de lecture binaire : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement binaire : " + e.getMessage());
            return null;
        }
    }

    private static String lireProchainMotASCII(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;

        while ((b = in.read()) != -1) {
            char c = (char) b;

            if (c == '#') { // Ignorer les commentaires
                while ((b = in.read()) != -1 && (char) b != '\n');
                continue;
            }

            if (Character.isWhitespace(c)) {
                if (sb.length() > 0) {
                    return sb.toString();
                }
            } else {
                sb.append(c);
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }
}