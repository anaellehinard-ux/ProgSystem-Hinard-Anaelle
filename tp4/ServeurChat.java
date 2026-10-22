import java.net.*;
import java.io.*;
import java.util.*;

public class ServeurChat {
    // Liste thread-safe des flux de sortie
    private static final List<PrintWriter> clients =
        Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur Chat démarré");

        while (true) {
            Socket client = serverSocket.accept();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            // Enregistrement du client dans la liste partagée
            synchronized (clients) {
                clients.add(out);
            }

            System.out.println("Client connecté. Total : " + clients.size());
            new Thread(new ChatHandler(client, out)).start();
        }
    }

    static class ChatHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ChatHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            String nomThread = Thread.currentThread().getName();

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))) {

                String ligne;
                while ((ligne = in.readLine()) != null) {
                    System.out.println("[" + nomThread + "] Message reçu : " + ligne);
                    broadcast(ligne);
                }
                System.out.println("[" + nomThread + "] Client déconnecté");
            } catch (IOException e) {
                System.out.println("[" + nomThread + "] Erreur : " + e.getMessage());
            } finally {
                synchronized (clients) {
                    clients.remove(out);
                }
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        // Diffusion à tous les clients connectés
        private void broadcast(String message) {
            synchronized (clients) {
                for (PrintWriter writer : clients) {
                    writer.println(">>> " + message);
                }
            }
        }

        // Variante : diffusion à tous sauf l'émetteur
        private void broadcastExcept(String message) {
            synchronized (clients) {
                for (PrintWriter writer : clients) {
                    if (writer != out) {
                        writer.println(">>> " + message);
                    }
                }
            }
        }
    }
}