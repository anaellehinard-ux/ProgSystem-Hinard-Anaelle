import java.net.*;
import java.io.*;

public class ClientAsync {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connecté au serveur");

        // Thread de lecture (messages du serveur)
        ReceptionHandler reception = new ReceptionHandler(socket);
        Thread lectureThread = new Thread(reception);
        lectureThread.setDaemon(true);
        lectureThread.start();

        // Thread principal
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader clavier = new BufferedReader(
            new InputStreamReader(System.in)
        );

        String texte;
        while ((texte = clavier.readLine()) != null) {
            out.println(texte);
        }

        reception.arreter();
        socket.close();
    }
}

class ReceptionHandler implements Runnable {
    private Socket socket;
    private volatile boolean running = true; // visible entre threads

    public ReceptionHandler(Socket socket) {
        this.socket = socket;
    }

    public void arreter() {
        running = false;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            String ligne;
            while (running && (ligne = in.readLine()) != null) {
                System.out.println(ligne);
            }

            if (running) {
                System.out.println("Connexion fermée par le serveur");
            }
        } catch (IOException e) {
            if (running) {
                System.out.println("Erreur de lecture : " + e.getMessage());
            }
        }
    }
}