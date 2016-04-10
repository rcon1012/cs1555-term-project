import com.sun.javaws.exceptions.InvalidArgumentException;

public class Group {
    private int groupId;
    private String name;
    private String description;
    private int capacity;

    public Group() {}

    public Group(int groupId, String name, String description, int capacity) throws InvalidArgumentException {
        setGroupId(groupId);
        setName(name);
        setDescription(description);
        setCapacity(capacity);
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
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
}
