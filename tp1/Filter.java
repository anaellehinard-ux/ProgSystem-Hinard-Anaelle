import java.io.*;

public class Filter {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java Filter <input_file> <output_file> <filter_type>");
            System.err.println("You should provide an input and an output filenames and a filter type. Aborting");
            System.exit(-1);
        }

        String inputFilename = args[0];
        String outputFilename = args[1];
        String filterType = args[2];

        Image source = null;

        // Adaptation du nom de la méthode de lecture (ex: read_txt ou read_bin)
        source = Image.read_txt(inputFilename);
        if (source == null) {
            System.err.println("Error reading input file.");
            System.exit(-1);
        }

        int h = source.getHeight();
        int w = source.getWidth();
        Image destination = new Image(w, h);

        // Application du filtre pixel par pixel
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // Remplacement des getters par l'accès au tableau ou méthodes personnalisées
                int rouge = source.getPixelRed(x, y);
                int vert = source.getPixelGreen(x, y);
                int bleu = source.getPixelBlue(x, y);

                switch (filterType.toLowerCase()) {
                    case "copy":
                        destination.setPixel(x, y, rouge, vert, bleu);
                        break;
                    case "dark":
                        // Assombrir : diviser l'intensité par 2
                        destination.setPixel(x, y, rouge / 2, vert / 2, bleu / 2);
                        break;
                    case "bright":
                        // Éclaircir : multiplier par 1.5 en plafonnant à 255
                        int rB = Math.min(255, (int)(rouge * 1.5));
                        int gB = Math.min(255, (int)(vert * 1.5));
                        int bB = Math.min(255, (int)(bleu * 1.5));
                        destination.setPixel(x, y, rB, gB, bB);
                        break;
                    case "grayscale":
                        // Niveau de gris : moyenne des 3 composantes
                        int gris = (rouge + vert + bleu) / 3;
                        destination.setPixel(x, y, gris, gris, gris);
                        break;
                    default:
                        System.err.println("Unknown filter type: " + filterType);
                        System.exit(-1);
                }
            }
        }

        // Adaptation du nom de la méthode de sauvegarde
        destination.save_txt(outputFilename);
        System.out.println("Filtered image saved to " + outputFilename);
    }
}