import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Friend {
    private long friend1Id;
    private long friend2Id;
    private Timestamp established;

    public Friend() {}

    public Friend(long friend1Id, long friend2Id, Timestamp established) {
        setFriend1Id(friend1Id);
        setFriend2Id(friend2Id);
        setEstablished(established);
    }
    
    public Friend(long friend1Id, long friend2Id) {
        setFriend1Id(friend1Id);
        setFriend2Id(friend2Id);
    }

    public Friend(ResultSet resultSet) throws SQLException {
        this(ResultSetWrapper.getLong(resultSet, 1),
             ResultSetWrapper.getLong(resultSet, 2),
             ResultSetWrapper.getNullableTimestamp(resultSet, 3));
    }

    public long getFriend1Id() {
        return friend1Id;
    }

    public void setFriend1Id(long friend1Id) {
        this.friend1Id = friend1Id;
    }

    public long getFriend2Id() {
        return friend2Id;
    }

    public void setFriend2Id(long friend2Id) {
        this.friend2Id = friend2Id;
    }

    public Timestamp getEstablished() {
        return established;
    }

    public void setEstablished(Timestamp established) {
        this.established = established;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("FRIEND 1 ID:\t");
        sb.append(getFriend1Id());
        sb.append("\n");

        sb.append("FRIEND 2 ID:\t");
        sb.append(getFriend2Id());
        sb.append("\n");

        sb.append("ESTABLISHED:\t");
        sb.append(getEstablished());
        sb.append("\n");

        return sb.toString();
    }
}
