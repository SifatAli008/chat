import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int PORT = 9806;
    private static final Map<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static synchronized void addClient(String username, PrintWriter writer) {
        clients.put(username, writer);
    }

    static synchronized void removeClient(String username) {
        clients.remove(username);
    }

    static synchronized void broadcastMessage(String sender, String message) {
        for (PrintWriter writer : clients.values()) {
            writer.println(sender + ": " + message);
        }
    }
}
