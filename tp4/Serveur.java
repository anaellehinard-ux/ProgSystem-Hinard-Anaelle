import java.net.*;
import java.io.*;

public class Serveur {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur démarré sur le port 5000");

        while(true) {
            // 2. Acceptation d'une connexion cliente
            // 3. Création des canaux input/output
            // 4. Boucle d'écho (lecture/écriture)
            // 5. Fermeture de la connexion client
        }
    }
}
