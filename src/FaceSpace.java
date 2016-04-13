import java.sql.*;
import java.util.Scanner;

public class FaceSpace {
    private static final Scanner consoleScanner = new Scanner(System.in);
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
            connection = DriverManager.getConnection(url, username, password);

            FaceSpace demo = new FaceSpace();
            demo.runExample();
        }
        catch(Exception Ex)  {
            System.out.println("Error connecting to database.  Machine Error: " +
                    Ex.toString());
        }
        finally {
            connection.close();
        }
    }

    public void runExample() {
        int choice = -1;
        do {
            try {
                runChoice(choice);
                choice = readInt("Choice: ");
            }
            catch (SQLException e) {
                System.out.println("Error running the sample queries. Machine Error: " + e.toString());
            }
            finally {
                closeStatements();
            }
        } while(choice != 0);
    }

    private void runChoice(int choice) throws SQLException {
        switch (choice) {
            case 0:
                try {
                    connection.close();
                }
                catch(Exception e)  {
                    System.out.println("Error closing database connection.  Machine Error: " + e.toString());
                }
                break;
            case 1:
                createUser(new Profile());
                break;
            case 2:
                initiateFriendship(9, 5);
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
                System.out.println("No choice selected. Choices:");
                System.out.println(
                        "0)\tExit\n" +
                        "1)\tCreate User\n" +
                        "2)\tInitiate Friendship\n" +
                        "3)\tEstablish Friendship\n" +
                        "4)\tDisplay Friendship\n" +
                        "5)\tCreate Group\n" +
                        "6)\tAdd To Group\n" +
                        "7)\tSend Message To User\n" +
                        "8)\tSend Message To Group\n" +
                        "9)\tDisplay Messages\n" +
                        "10)\tDisplay New Messages\n" +
                        "11)\tSearch For User\n" +
                        "12)\tThree Degrees\n" +
                        "13)\tTop Messages\n" +
                        "14)\tDrop User\n" +
                        "-1)\tHelp\n");
                break;
        }
    }

    public static int readInt(String prompt) {
        String consoleInput = readString(prompt);
        return forceParseInt(consoleInput, "Input", prompt);
    }

    public static int forceParseInt(String input, String propertyName, String prompt) {
        while(true) {
            try {
                return Integer.parseInt(input);
            }
            catch(NumberFormatException e) {
                System.out.println("ERROR: " + propertyName + " must be a base 10 integer. Please try again.\n");
                input = readString(prompt);
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        String consoleInput = consoleScanner.nextLine();
        return consoleInput;
    }

    private void closeStatements() {
        try {
            if(statement != null) { statement.close(); }
            if(prepStatement != null) { prepStatement.close(); }
        }
        catch (SQLException e) {
            System.out.println("Cannot close statement. Machine error: " + e.toString());
        }
        System.out.println();
    }

    private void createUser(Profile profile) throws SQLException {
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

    private void initiateFriendship(long friend_id1, long friend_id2) throws SQLException {
        if(!friendshipExists(friend_id1, friend_id2) && !friendshipExists(friend_id2, friend_id1)) {
            Friend friendship = new Friend(friend_id1, friend_id2, new Timestamp((new java.util.Date()).getTime()));

            query = "INSERT INTO Friends VALUES(?, ?, ?)";
            prepStatement = connection.prepareStatement(query);

            prepStatement.setLong(1, friendship.getFriend1Id());
            prepStatement.setLong(2, friendship.getFriend2Id());
            prepStatement.setTimestamp(3, friendship.getEstablished());

            prepStatement.executeUpdate();

            System.out.println("SUCCESS: Friendship has been successfully inserted.");
            System.out.println(friendship);
        }
        else {
            System.out.println("ERROR: Friendship has already been initiated");
        }
    }

    private boolean friendshipExists(long friend_id1, long friend_id2) throws SQLException {
        query = "SELECT * FROM Friends WHERE friend1_id=? and friend2_id=?";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setLong(1, friend_id1);
        prepStatement.setLong(2, friend_id2);

        resultSet = prepStatement.executeQuery();

        return resultSet.next();
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
