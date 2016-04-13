import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {
    private long groupId;
    private String name;
    private String description;
    private int capacity;

    public Group() {}

    public Group(long groupId, String name, String description, int capacity) throws InvalidArgumentException {
        setGroupId(groupId);
        setName(name);
        setDescription(description);
        setCapacity(capacity);
    }

    public Group(ResultSet resultSet) throws SQLException, InvalidArgumentException {
        this(ResultSetWrapper.getLong(resultSet, 1),
             ResultSetWrapper.getNullableString(resultSet, 2),
             ResultSetWrapper.getNullableString(resultSet, 3),
             ResultSetWrapper.getInt(resultSet, 4));
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidArgumentException {
        if(name == null) {
            throw new InvalidArgumentException(new String[]{"Constraint name NOT NULL violated"});
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("GROUP ID:\t");
        sb.append(getGroupId());
        sb.append("\n");

        sb.append("NAME:\t");
        sb.append(getName());
        sb.append("\n");

        sb.append("DESCRIPTION:\t");
        sb.append(getDescription());
        sb.append("\n");

        sb.append("CAPACITY:\t");
        sb.append(getCapacity());
        sb.append("\n");

        return sb.toString();
    }
}
