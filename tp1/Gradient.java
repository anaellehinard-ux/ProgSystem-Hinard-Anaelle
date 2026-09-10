public class Gradient {
    public static void main(String[] args) {
        Image img = new Image(200, 100);

        int largeur = img.getWidth();
        int hauteur = img.getHeight();

        // Génération du dégradé de bleu
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int bleu = (x * 255) / (largeur - 1);
                img.setPixel(x, y, 0, 0, bleu);
            }
        }

        try {
            img.save_txt("gradient.ppm");
            System.out.println("Dégradé créé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du dégradé : " + e.getMessage());
        }
    }
}