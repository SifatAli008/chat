import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get username from client
            username = in.readLine();
            ChatServer.addClient(username, out);
            out.println("Welcome, " + username + "!");

            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                ChatServer.broadcastMessage(username, input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ChatServer.removeClient(username);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
