import java.sql.Timestamp;

public class Profile {
    private int userId;
    private String fName;
    private String lName;
    private String email;
    private Timestamp dob;
    private Timestamp lastOn;

    public Profile() {}

    public Profile(int userId, String fName, String lName, String email, Timestamp dob, Timestamp lastOn) {
        setUserId(userId);
        setFName(fName);
        setLName(lName);
        setEmail(email);
        setDob(dob);
        setLastOn(lastOn);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
}
