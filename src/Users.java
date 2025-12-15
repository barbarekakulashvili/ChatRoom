/*import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Users {
    public static void main(String[] args) throws IOException{

        Scanner scanner = new Scanner(System.in);
        String host = scanner.nextLine();
        int port = scanner.nextInt();

        Socket socket = new Socket(host, port);
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

        while (true) {
            try{
            String info = scanner.nextLine();
            if (info.startsWith("/name")) {
                String rename = info.substring(5).trim();
                Message message = new Message(MessageType.RENAME, null, null, rename);
                output.writeObject(message);
            }
            if (info.startsWith("/pm")) {
                String[] infoParts = info.split(" ", 3);
                if (infoParts.length == 3) {
                    String receiver = infoParts[1];
                    String text = infoParts[2];
                    Message message = new Message(MessageType.PRIVATE_MESSAGE, null, receiver, text);
                    output.writeObject(message);
                }
            }
            if (info.startsWith("/chat")) {
                String[] infoParts = info.split(" ", 2);
                if (infoParts.length == 2) {
                    String text = infoParts[1];
                    Message message = new Message(MessageType.CHAT, null, null, text);
                    output.writeObject(message);
                }
            }
            if(info.startsWith("/exit")){
                Message message = new Message(MessageType.LEAVE_CHAT, null, null, null);
                output.writeObject(message);
                socket.close();
                break;
            }
        }catch(IOException | NumberFormatException e){
                System.out.println("Connection failed :(");
            }
        }
    }
}*/

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Users {
    static void main(String[] args) {
        while(true){
            try {
                Scanner scanner = new Scanner(System.in);

                System.out.print("Enter server host: ");
                String host = scanner.nextLine();
                System.out.print("Enter server port: ");
                int port = scanner.nextInt();

                Socket socket = new Socket(host, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                System.out.println("Connected to server.");

                Thread listener = new Thread(new Listener(input));

                listener.start();

                while(true){
                    String info = scanner.nextLine();

                    if (info.startsWith("/name ")) {
                        String rename = info.substring(6).trim();
                        output.writeObject(new Message(MessageType.RENAME, null, null, rename));
                    } else if (info.startsWith("/pm")) {
                        String[] parts = info.split(" ", 3);
                        if (parts.length == 3) {
                            String receiver = parts[1];
                            String text = parts[2];
                            output.writeObject(new Message(MessageType.PRIVATE_MESSAGE, null, receiver, text));
                        }
                    } else if (info.equals("/exit")) {
                        output.writeObject(new Message(MessageType.LEAVE_CHAT, null, null, null));
                        socket.close();
                        System.out.println("You left the chat.");
                        break;
                    } else {
                        output.writeObject(new Message(MessageType.CHAT, null, null, info));
                    }
                }
            } catch (IOException | NumberFormatException | InputMismatchException e) {
                System.out.println("Connection failed");
            }
        }
    }

}

