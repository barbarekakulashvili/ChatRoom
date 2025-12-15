import java.io.Serializable;

public class Message implements Serializable {

    private MessageType type;
    private String messageSender;
    private String messageReceiver;
    private String message;


    public Message(MessageType type, String messageSender, String messageReceiver, String message) {
        this.type = type;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
