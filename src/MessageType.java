
public enum MessageType {
    SINGLE_USER(1, "Single User"),
    WHOLE_GROUP(2, "Whole Group");

    public int value() { return value; }

    public static MessageType fromInt(int type) throws IllegalArgumentException {
        switch (type) {
            case 1:
                return SINGLE_USER;
            case 2:
                return WHOLE_GROUP;
            default:
                throw new IllegalArgumentException("No such MessageType corresponds to that integer code");
        }
    }

    @Override
    public String toString() {
        return description;
    }

    private final int value;
    private final String description;

    private MessageType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
