import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Profile {
    private long userId;
    private String fName;
    private String lName;
    private String email;
    private Timestamp dob;
    private Timestamp lastOn;

    public Profile() {}

    public Profile(long userId, String fName, String lName, String email, Timestamp dob, Timestamp lastOn) {
        setUserId(userId);
        setFName(fName);
        setLName(lName);
        setEmail(email);
        setDob(dob);
        setLastOn(lastOn);
    }

    public Profile(ResultSet resultSet) throws SQLException {
        this(ResultSetWrapper.getLong(resultSet, 1),
             ResultSetWrapper.getNullableString(resultSet, 2),
             ResultSetWrapper.getNullableString(resultSet, 3),
             ResultSetWrapper.getNullableString(resultSet, 4),
             ResultSetWrapper.getNullableTimestamp(resultSet, 5),
             ResultSetWrapper.getNullableTimestamp(resultSet, 6));
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public Timestamp getLastOn() {
        return lastOn;
    }

    public void setLastOn(Timestamp lastOn) {
        this.lastOn = lastOn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("FIRST NAME:\t");
        sb.append(getFName());
        sb.append("\n");

        sb.append("LAST NAME:\t");
        sb.append(getLName());
        sb.append("\n");

        sb.append("EMAIL:\t");
        sb.append(getEmail());
        sb.append("\n");

        sb.append("DATE OF BIRTH:\t");
        sb.append(getDob());
        sb.append("\n");

        sb.append("LAST ONLINE:\t");
        sb.append(getLastOn());
        sb.append("\n");

        return sb.toString();
    }
}
