import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom {
    private static ConcurrentHashMap<String, ClientHandler> map = new ConcurrentHashMap<>();
    static int counter = 0;

    static synchronized String generateUsername() {
        return "Anonymous" + counter++;
    }

    static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter server port: ");
        int port = scanner.nextInt();

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("server created");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void display(Message message, String excludeUser) throws IOException {
        for (ClientHandler handler : map.values()) {
            if (!handler.getUsername().equals(excludeUser)) {
                handler.sendMessage(message);
            }
        }
    }

    public static void addClient(String name, ClientHandler handler) {
        map.put(name, handler);
    }

    public static void removeClient(String name) {
        map.remove(name);
    }

    public static ClientHandler getClient(String name) {
        return map.get(name);
    }

    public static int getUserCount() {
        return map.size();
    }
}

