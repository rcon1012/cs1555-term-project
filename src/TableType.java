
public enum TableType {
    PROFILES(1, "Profiles"),
    FRIENDS(2, "Friends"),
    GROUPS(3, "Groups"),
    MEMBERS(4, "Members"),
    MESSAGES(5, "Messages");

    public int value() { return value; }

    public static TableType fromInt(int type) throws IllegalArgumentException {
        switch (type) {
            case 1:
                return PROFILES;
            case 2:
                return FRIENDS;
            case 3:
                return GROUPS;
            case 4:
                return MEMBERS;
            case 5:
                return MESSAGES;
            default:
                throw new IllegalArgumentException("No such TableType corresponds to that integer code");
        }
    }

    @Override
    public String toString() {
        return canonical;
    }

    private final int value;
    private final String canonical;

    TableType(int value, String canonical) {
        this.value = value;
        this.canonical = canonical;
    }
}
