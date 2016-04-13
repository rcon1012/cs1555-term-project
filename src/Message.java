import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Message {
    private long id;
    private String subject;
    private long senderId;
    private long recipientId;
    private Timestamp timeSent;
    private String text;
    private MessageType type;

    public Message() {}

    public Message(long id, String subject, long senderId, long recipientId, Timestamp timeSent, String text, MessageType type) throws InvalidArgumentException {
        setId(id);
        setSubject(subject);
        setSenderId(senderId);
        setRecipientId(recipientId);
        setTimeSent(timeSent);
        setText(text);
        setType(type);
    }

    public Message(ResultSet resultSet) throws SQLException, InvalidArgumentException {
        this(ResultSetWrapper.getLong(resultSet, 1),
             ResultSetWrapper.getNullableString(resultSet, 2),
             ResultSetWrapper.getLong(resultSet, 3),
             ResultSetWrapper.getLong(resultSet, 4),
             ResultSetWrapper.getNullableTimestamp(resultSet, 5),
             ResultSetWrapper.getNullableString(resultSet, 6),
             MessageType.fromInt(ResultSetWrapper.getInt(resultSet, 7)));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MESSAGE ID:\t");
        sb.append(getId());
        sb.append("\n");

        sb.append("SUBJECT:\t");
        sb.append(getSubject());
        sb.append("\n");

        sb.append("SENDER ID:\t");
        sb.append(getSenderId());
        sb.append("\n");

        sb.append("RECIPIENT ID:\t");
        sb.append(getRecipientId());
        sb.append("\n");

        sb.append("TIME SENT:\t");
        sb.append(getTimeSent());
        sb.append("\n");

        sb.append("TEXT:\t");
        sb.append(getText());
        sb.append("\n");

        sb.append("MESSAGE TYPE:\t");
        sb.append(getType());
        sb.append("\n");

        return sb.toString();
    }
}
