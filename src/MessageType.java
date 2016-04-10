import com.sun.javaws.exceptions.InvalidArgumentException;

public enum MessageType {
    SINGLE_USER(1),
    WHOLE_GROUP(2);

    private final int value;
    MessageType(int value) throws InvalidArgumentException {
        if(value != 0 || value != 1) {
            throw new InvalidArgumentException(new String[]{"Constraint Messages_Type_Check CHECK (type BETWEEN 1 AND 2) violated"});
        }
        this.value = value;
    }

    private int value() { return value; }
}
