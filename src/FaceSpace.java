import java.sql.*;
import java.text.ParseException;
import oracle.jdbc.*;

public class FaceSpace {
    private static Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet resultSet;
    private String query;
	private java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

    public static void main(String args[]) throws SQLException {
        String username, password;
        username = "SYSTEM";
        password = "password";

        try {
            System.out.println("Registering DB..");
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

            System.out.println("Set url..");
            String url = "jdbc:oracle:thin:@localhost:1521:xe";

            System.out.println("Connect to DB..");
            //create a connection to DB on class3.cs.pitt.edu
            connection = DriverManager.getConnection(url, username, password);
            FaceSpace demo = new FaceSpace(Integer.parseInt(args[0]));

        }
        catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " +
                    Ex.toString());
        }
        finally {
            connection.close();
        }
    }

    public FaceSpace(int example_no) {
        switch (example_no) {
            case 1:
                createUser(new Profile());
                break;
            case 2:
                initiateFriendship(0, 0);
                break;
            case 3:
                establishFriendship(0, 0);
                break;
            case 4:
                displayFriends(0);
                break;
            case 5:
                createGroup(new Group());
                break;
            case 6:
                addToGroup(0, 0);
                break;
            case 7:
                sendMessageToUser(new Message());
                break;
            case 8:
                sendMessageToGroup(new Message());
                break;
            case 9:
                displayMessages(0);
                break;
            case 10:
                displayNewMessages(0);
                break;
            case 11:
                searchForUser(new String());
                break;
            case 12:
                threeDegrees(0, 0);
                break;
            case 13:
                topMessagers(0, 0);
                break;
            case 14:
                dropUser(0);
                break;
            default:
                System.out.println("Example not found for your entry: " + example_no);
                try {
                    connection.close();
                }
                catch(Exception Ex)  {
                    System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
                }
                break;
        }
    }

    private void createUser(Profile profile) {
		try {
			// prepare the statement
			query = "INSERT INTO Profiles(fname, lname, email, dob, last_on) Values(?, ?, ?, ?, ?);";
			prepStatement = connection.prepareStatement(query);

			// insert the profile values
			prepStatement.setString(1, profile.getFName());
			prepStatement.setString(2, profile.getLName());
			prepStatement.setString(3, profile.getEmail());
			prepStatement.setTimestamp(4, new java.sql.Timestamp(profile.getDob().getTime()));
			// last_on set to profile creation
			prepStatement.setTimestamp(5, new java.sql.Timestamp((new java.util.Date()).getTime()));

			// execute query
			prepStatement.executeUpdate();
		}
        catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                    Ex.toString());
        }
        finally {
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }
    }

    private void initiateFriendship(long friend_id1, long friend_id2) {

    }

    private void establishFriendship(long friend_id1, long friend_id2) {

    }

    private void displayFriends(long user_id) {

    }

    private void createGroup(Group group) {

    }

    private void addToGroup(long group_id, long user_id) {

    }

    private void sendMessageToUser(Message message) {

    }

    private void sendMessageToGroup(Message message) {

    }

    private void displayMessages(long user_id) {

    }

    private void displayNewMessages(long user_id) {

    }

    private void searchForUser(String searchTerm) {

    }

    private void threeDegrees(long user_id1, long user_id2) {

    }

    private void topMessagers(int numUsers, int numMonths) {

    }

    private void dropUser(long user_id) {

    }
}
