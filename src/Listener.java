import java.io.ObjectInputStream;

public class Listener implements Runnable{
    private ObjectInputStream in;

    public Listener(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message msg = (Message) in.readObject();

                if(msg.getType() == MessageType.PRIVATE_MESSAGE) {
                    System.out.println("PM from [ " + msg.getMessageSender() + " ]" + msg.getMessage());
                }
                else{
                    System.out.println("[ " + msg.getMessageSender() + " ]" + msg.getMessage());
                }
            }
        } catch (Exception ignored) {}
    }
}
