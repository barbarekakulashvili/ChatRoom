import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
   private Socket socket;
   private ObjectInputStream input;
   private ObjectOutputStream output;
   private String username;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.username = ChatRoom.generateUsername();
    }

    @Override
    public void run() {
        try {
            ChatRoom.addClient(username, this);
            sendMessage(new Message(MessageType.SYSTEM, "SERVER", null, "Welcome to chat. Your name is " + username));
            ChatRoom.display(new Message(MessageType.SYSTEM, "SERVER", null, username + " joined the chat!"), username);
            ChatRoom.display(new Message(MessageType.SYSTEM, "SERVER", null, "Users online: " + ChatRoom.getUserCount()), username);
            while(true){
                Message message = (Message) input.readObject();
                handleMessage(message);
            }

        } catch (IOException | ClassNotFoundException e) {
            try {
                disconnect();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void handleMessage(Message message) throws IOException {
        switch(message.getType()){
            case CHAT:
                ChatRoom.display(new Message(MessageType.CHAT, username, null, message.getMessage()), username);
                break;

            case PRIVATE_MESSAGE:
                ClientHandler receiver = ChatRoom.getClient(message.getMessageReceiver());
                if(receiver != null){
                    receiver.sendMessage(new Message(MessageType.PRIVATE_MESSAGE, username, message.getMessageReceiver(), message.getMessage()));
                }else{
                    sendMessage(new Message(MessageType.SYSTEM, null, null, "User not found"));
                }
                break;

            case RENAME:
                String newName = message.getMessage();
                if(ChatRoom.getClient(newName)!=null) {
                    sendMessage(new Message(MessageType.SYSTEM, null, null, "Name already taken"));
                }else{
                    ChatRoom.removeClient(username);
                    String oldName = username;
                    username = newName;
                    ChatRoom.addClient(username, this);
                    ChatRoom.display(
                            new Message(MessageType.SYSTEM, null, null,oldName + " changed name to " + username), null
                    );
                }
                break;

            case LEAVE_CHAT:
                disconnect();
                break;
        }
    }

    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
        output.flush();
    }

    private void disconnect() throws IOException {
        try {
            ChatRoom.removeClient(username);
            ChatRoom.display(
                    new Message(MessageType.SYSTEM, null, null, username + " left the chat"),
                    username
            );

            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();

        } catch (IOException e) {}
    }

    public String getUsername() {
        return username;
    }
}
