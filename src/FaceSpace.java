import java.sql.*;
import java.text.ParseException;
import oracle.jdbc.*;

public class FaceSpace {
    private static Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet resultSet;
    private String query;

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
                createUser();
                break;
            case 2:
                initiateFriendship();
                break;
            case 3:
                establishFriendship();
                break;
            case 4:
                displayFriends();
                break;
            case 5:
                createGroup();
                break;
            case 6:
                addToGroup();
                break;
            case 7:
                sendMessageToUser();
                break;
            case 8:
                sendMessageToGroup();
                break;
            case 9:
                displayMessages();
                break;
            case 10:
                displayNewMessages();
                break;
            case 11:
                searchForUser();
                break;
            case 12:
                threeDegrees();
                break;
            case 13:
                topMessagers();
                break;
            case 14:
                dropUser();
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
