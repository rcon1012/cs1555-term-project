import java.sql.ResultSet;
import java.sql.SQLException;

public class Member {
    private Long groupId;
    private Long userId;

    public Member() {}

    public Member(Long groupId, Long userId) {
        setGroupId(groupId);
        setUserId(userId);
    }

    public Member(ResultSet resultSet) throws SQLException {
        this(ResultSetWrapper.getNullableLong(resultSet, 1),
             ResultSetWrapper.getNullableLong(resultSet, 2));
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("GROUP ID:\t");
        sb.append(getGroupId());
        sb.append("\n");

        sb.append("USER ID:\t");
        sb.append(getUserId());
        sb.append("\n");

        return sb.toString();
    }
}
