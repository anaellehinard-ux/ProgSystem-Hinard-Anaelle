import java.net.*;
import java.io.*;

public class ServeurMulti {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur multi-clients démarré");

        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("Nouveau client : " + client.getInetAddress());

            // Délégation à un thread dédié
            Thread t = new Thread(new ClientHandler(client));
            t.start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket client;

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        String nomThread = Thread.currentThread().getName();

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            String ligne;
            // Lecture des messages jusqu'à la déconnexion du client (readLine() == null)
            while ((ligne = in.readLine()) != null) {
                System.out.println("[" + nomThread + "] Reçu : " + ligne);
                out.println("ECHO: " + ligne);
            }
            System.out.println("[" + nomThread + "] Client déconnecté");
        } catch (IOException e) {
            System.out.println("[" + nomThread + "] Erreur avec le client : " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }
}