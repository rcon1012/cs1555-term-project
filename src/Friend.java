import java.sql.Timestamp;

public class Friend {
    private int friend1Id;
    private int friend2Id;
    private Timestamp established;

    public Friend() {}

    public Friend(int friend1Id, int friend2Id, Timestamp established) {
        setFriend1Id(friend1Id);
        setFriend2Id(friend2Id);
        setEstablished(established);
    }

    public int getFriend1Id() {
        return friend1Id;
    }

    public void setFriend1Id(int friend1Id) {
        this.friend1Id = friend1Id;
    }

    public int getFriend2Id() {
        return friend2Id;
    }

    public void setFriend2Id(int friend2Id) {
        this.friend2Id = friend2Id;
    }

    public Timestamp getEstablished() {
        return established;
    }

    public void setEstablished(Timestamp established) {
        this.established = established;
    }
}
