import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.Timestamp;

public class Message {
    private int id;
    private String subject;
    private int senderId;
    private int recipientId;
    private Timestamp timeSent;
    private String text;
    private MessageType type;

    public Message() {}

    public Message(int id, String subject, int senderId, int recipientId, Timestamp timeSent, String text, MessageType type) throws InvalidArgumentException {
        setId(id);
        setSubject(subject);
        setSenderId(senderId);
        setRecipientId(recipientId);
        setTimeSent(timeSent);
        setText(text);
        setType(type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public Timestamp getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Timestamp timeSent) throws InvalidArgumentException {
        if(timeSent == null) {
            throw new InvalidArgumentException(new String[]{"Constraint time_sent NOT NULL violated"});
        }
        this.timeSent = timeSent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
