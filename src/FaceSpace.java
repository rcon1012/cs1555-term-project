import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FaceSpace {
    private static final Scanner consoleScanner = new Scanner(System.in);
    private static final PrettyPrinter pp = new PrettyPrinter();
    private static final String dfString = "yyyy-MM-dd";
    private static final java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(dfString);
    private static Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet resultSet;
    private String query;

    public static void main(String args[]) {
        String url, username, password;
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        username = "SYSTEM";
        password = "password";
        if(args.length >= 1) { url = args[0]; }
        if(args.length >= 2) { username = args[1]; }
        if(args.length >= 3) { password = args[2]; }

        try {
            System.out.println("Registering DB..");
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

            System.out.println("Connect to DB..");
            connection = DriverManager.getConnection(url, username, password);

            FaceSpace demo = new FaceSpace();
            demo.runExample();
        } catch(Exception e)  {
            System.out.println("Error connecting to database.  Machine Error: " + e.toString());
        } finally {
            try {
                connection.close();
            } catch(Exception e)  {
                System.out.println("Error closing database connection.  Machine Error: " + e.toString());
            }
        }
    }

    public void runExample() {
        int choice = -1;
        do {
            try {
                runChoice(choice);
            } catch (SQLException e) {
                System.out.println("Error running the sample queries. Machine Error: " + e.toString());
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(statement != null) { statement.close(); }
                    if(prepStatement != null) { prepStatement.close(); }
                } catch (SQLException e) {
                    System.out.println("Cannot close statement. Machine error: " + e.toString());
                }

                choice = readInt("Choice: ");
            }
        } while(choice != 0);

        try {
            connection.close();
        } catch(Exception e)  {
            System.out.println("Error closing database connection.  Machine Error: " + e.toString());
        }
    }

    private void runChoice(int choice) throws SQLException, InvalidArgumentException {
        switch (choice) {
            case 1:
                createUser(readProfile());
                break;
            case 2:
                initiateFriendship(readLong("Friend 1 ID: "), readLong("Friend 2 ID: "));
                break;
            case 3:
                establishFriendship(readLong("Friend 1 ID: "), readLong("Friend 2 ID: "));
                break;
            case 4:
                displayFriends(readLong("User ID: "));
                break;
            case 5:
                createGroup(readGroup());
                break;
            case 6:
                addToGroup(readLong("Group ID: "), readLong("User ID: "));
                break;
            case 7:
                sendMessageToUser(readMessage());
                break;
            case 8:
                sendMessageToGroup(readMessage());
                break;
            case 9:
                displayMessages(readLong("User ID: "));
                break;
            case 10:
                displayNewMessages(readLong("User ID: "));
                break;
            case 11:
                searchForUser(readString("Search Term: "));
                break;
            case 12:
                threeDegrees(readLong("User 1 ID: "), readLong("User ID: "));
                break;
            case 13:
                topMessagers(readInt("Num Users: "), readInt("Num Months: "));
                break;
            case 14:
                dropUser(readLong("User ID: "));
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

    private Profile readProfile() {
        return new Profile(readLong("User ID: "), readString("First Name: "), readString("Last Name: "), readString("Email: "), readTimestamp("Date of Birth: "), currentTimestamp());
    }

    private Group readGroup() throws InvalidArgumentException {
        return new Group(-1, readString("Name: "), readString("Description: "), readInt("Capacity: "));
    }

    private Message readMessage() throws InvalidArgumentException {
        return new Message(-1, readString("Subject: "), readLong("Sender ID: "), readLong("Recipient ID: "), currentTimestamp(), readString("Text: "), MessageType.SINGLE_USER);
    }

    private static int readInt(String prompt) {
        String consoleInput = readString(prompt);
        while(true) {
            try {
                return Integer.parseInt(consoleInput);
            } catch(NumberFormatException e) {
                System.out.println("ERROR: Input must be a base 10 integer. Please try again.\n");
                consoleInput = readString(prompt);
            }
        }
    }

    private static long readLong(String prompt) {
        String consoleInput = readString(prompt);
        while(true) {
            try {
                return Long.parseLong(consoleInput);
            } catch(NumberFormatException e) {
                System.out.println("ERROR: Input must be a base 10 integer. Please try again.\n");
                consoleInput = readString(prompt);
            }
        }
    }

    private static Timestamp readTimestamp(String prompt) {
        String consoleInput = readString(prompt);
        while(true) {
            try {
                return new Timestamp(df.parse(consoleInput).getTime());
            }
            catch(ParseException e) {
                System.out.println("ERROR: Input must be of format \"" + dfString + "\". Please try again.\n");
                consoleInput = readString(prompt);
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return consoleScanner.nextLine();
    }

    private static Timestamp currentTimestamp() {
        return new Timestamp((new java.util.Date()).getTime());
    }

    private void createUser(Profile profile) throws SQLException {
        // prepare the statement
        query = "INSERT INTO Profiles(fname, lname, email, dob, last_on) Values(?, ?, ?, ?, ?)";
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
            Friend friendship = new Friend(friend_id1, friend_id2);

            query = "INSERT INTO Friends(friend1_id, friend2_id)  VALUES(?, ?)";
            prepStatement = connection.prepareStatement(query);

            prepStatement.setLong(1, friendship.getFriend1Id());
            prepStatement.setLong(2, friendship.getFriend2Id());

            prepStatement.executeUpdate();

            System.out.println("SUCCESS: Friendship has been successfully inserted.\n");
            System.out.println(friendship);
        }
        else {
            System.out.println("ERROR: Friendship has already been initiated\n");
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

    private void establishFriendship(long friend_id1, long friend_id2) throws SQLException {
        query = "UPDATE Friends SET established = ? WHERE (friend1_id = ? AND friend2_id = ?) OR (friend1_id = ? AND friend2_id = ?)";
        prepStatement = connection.prepareStatement(query);
        
        prepStatement.setTimestamp(1, new java.sql.Timestamp((new java.util.Date()).getTime()));
        prepStatement.setLong(2, friend_id1);
        prepStatement.setLong(3, friend_id2);
        prepStatement.setLong(4, friend_id2);
        prepStatement.setLong(5, friend_id1);
        
        prepStatement.executeUpdate();
    }

    // I assume the unique friendship constraint is upheld as intended on insert/update
    private List<Friend> getFriends(long user_id) throws SQLException {
        List<Friend> friends = new ArrayList<Friend>();
        query = "SELECT * FROM Friends WHERE friend1_id=? AND established IS NOT NULL";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setLong(1, user_id);
        resultSet = prepStatement.executeQuery();

        while(resultSet.next()) {
            Friend f = new Friend(resultSet);
            friends.add(f);
        }

        query = "SELECT * FROM Friends WHERE friend2_id=? AND established IS NOT NULL";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setLong(1, user_id);
        resultSet = prepStatement.executeQuery();

        while(resultSet.next()) {
            Friend f = new Friend(resultSet);
            friends.add(f);
        }
        return friends;
    }

    private List<Long> getEstablishedFriends(long user_id) throws SQLException {
        List<Long> friend_ids = new ArrayList<Long>();
        ArrayList<Friend> friends = new ArrayList<Friend>(getFriends(user_id));
        for(Friend subject : friends) {
            long friend_id1 = subject.getFriend1Id();
            long friend_id2 = subject.getFriend2Id();
            if(friend_id1 == user_id) {
                friend_ids.add(friend_id2);
            } else if(friend_id2 == user_id) {
                friend_ids.add(friend_id1);
            } else {
                return null;
            }
        }
        return friend_ids;

    }

    private void displayFriends(long user_id) throws SQLException {
        // TODO: NOT DONE! Just example of how to read using ResultSet constructor
        ArrayList<Friend> friends = new ArrayList<Friend>(getFriends(user_id));
        pp.displayBoxed("Friends of id: " + user_id);
        for(int i = 0; i < friends.size(); i++) {
            System.out.println(friends.get(i));
        }
        System.out.println("\nNOTE: No more users found.\n");
    }

    private void createGroup(Group group) throws SQLException, InvalidArgumentException {
        query = "INSERT INTO Groups(name, description, capacity) VALUES(?, ?, ?)";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setString(1, group.getName());
        prepStatement.setString(2, group.getDescription());
        prepStatement.setInt(3, group.getCapacity());

        prepStatement.executeUpdate();

        System.out.println("SUCCESS: Group has been successfully inserted.\n");
        System.out.println(group);
    }

    private void addToGroup(long group_id, long user_id) throws SQLException {
        query = "INSERT INTO Members VALUES(?, ?)";
        prepStatement = connection.prepareStatement(query);
        
        prepStatement.setLong(1, group_id);
        prepStatement.setLong(2, user_id);
        
        prepStatement.executeUpdate();
    }

    private void sendMessageToUser(Message message) throws SQLException {
        message.setType(MessageType.SINGLE_USER);

        query = "INSERT INTO Messages(subject, sender_id, recip_id, time_sent, msg_text, type) VALUES(?, ?, ?, ?, ?, ?)";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setString(1, message.getSubject());
        prepStatement.setLong(2, message.getSenderId());    // e.g. 28
        prepStatement.setLong(3, message.getRecipientId()); // e.g. 7
        prepStatement.setTimestamp(4, message.getTimeSent());
        prepStatement.setString(5, message.getText());
        prepStatement.setInt(6, message.getType().value());

        prepStatement.executeUpdate();

        System.out.println("SUCCESS: Message has been successfully inserted.\n");
        System.out.println(message);
    }

    private void sendMessageToGroup(Message message) throws SQLException {
        message.setType(MessageType.WHOLE_GROUP);

        query = "INSERT INTO Messages(subject, sender_id, recip_id, time_sent, msg_text, type) VALUES(?, ?, ?, ?, ?, ?)";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setString(1, message.getSubject());
        prepStatement.setLong(2, message.getSenderId());    // e.g. 28
        prepStatement.setLong(3, message.getRecipientId()); // e.g. 7
        prepStatement.setTimestamp(4, message.getTimeSent());
        prepStatement.setString(5, message.getText());
        prepStatement.setInt(6, message.getType().value());

        prepStatement.executeUpdate();

        System.out.println("SUCCESS: Message has been successfully inserted.\n");
        System.out.println(message);
    }

    private void displayMessages(long user_id) throws SQLException, InvalidArgumentException {
        displayMessages(user_id, false);
    }

    private Profile getProfileFromId(long user_id) throws SQLException, InvalidArgumentException {
        query = "SELECT * FROM Profiles WHERE user_id = ?";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setLong(1, user_id);
        resultSet = prepStatement.executeQuery();

        resultSet.next();
        return (new Profile(resultSet));
    }

    private void displayMessages(long user_id, boolean new_only) throws SQLException, InvalidArgumentException {
        // TODO: format output
        String motd;
        Profile subject = getProfileFromId(user_id);

        if(new_only) {
            motd = "(NEW) Inbox for id: ";
        } else {
            motd = "Inbox for id: ";
        }
        pp.displayBoxed(motd + user_id);

        // display messages from a single user
        if(new_only){
            query = "SELECT * FROM Messages WHERE recip_id = ? AND type = ? AND time_sent > ? INNER JOIN Profiles ON Messages.sender_id = Profiles.user_id";
        } else {
            query = "SELECT * FROM Messages WHERE recip_id = ? AND type = ? INNER JOIN Profiles ON Messages.sender_id = Profiles.user_id";
        }
        prepStatement = connection.prepareStatement(query);

        prepStatement.setLong(1, user_id);
        prepStatement.setInt(2, 1); // Prev: MessagesType.SINGLE_USER, error
        if(new_only) {
            prepStatement.setTimestamp(3, subject.getLastOn());
        }

        resultSet = prepStatement.executeQuery();

        pp.displayUnderlined("FROM: Single User Conversations");
        while(resultSet.next()) {
            Message message = new Message(resultSet);
            System.out.println(message);
        }
        
        // display messages from a group the user belongs to
        if(new_only) {
            query = ("SELECT * FROM Messages WHERE recip_id = ? AND type = ? AND " +
                    "sender_id IN (SELECT group_id AS g_id FROM Members WHERE user_id = ?) AND time_sent > ?");
        } else {
            query = ("SELECT * FROM Messages WHERE recip_id = ? AND type = ? AND " +
                    "sender_id IN (SELECT group_id AS g_id FROM Members WHERE user_id = ?)");
        }
        prepStatement = connection.prepareStatement(query);
        
        prepStatement.setLong(1, user_id);
        prepStatement.setInt(2, 2); // Prev: MessageType.WHOLE_GROUP, error
        prepStatement.setLong(3, user_id);
        if(new_only) {
            prepStatement.setTimestamp(4, subject.getLastOn());
        }
        
        resultSet = prepStatement.executeQuery();

        pp.displayUnderlined("FROM: Group Conversations");
        while(resultSet.next()) {
            Message message = new Message(resultSet);
            System.out.println(message);
        }
    }

    private void displayNewMessages(long user_id) throws SQLException, InvalidArgumentException {
        displayMessages(user_id, true);
    }

    private void searchForUser(String searchTerm) throws SQLException {
        query = "SELECT * FROM Profiles WHERE fname=? OR lname=? OR email=?";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setString(1, searchTerm);
        prepStatement.setString(2, searchTerm);
        prepStatement.setString(3, searchTerm);
        resultSet = prepStatement.executeQuery();

        while(resultSet.next()) {
            Profile user = new Profile(resultSet);
            System.out.println(user);
        }
        System.out.println("\nNOTE: No more users found.\n");
    }

    private void threeDegrees(long user_id1, long user_id2) throws SQLException {
        int hops = 0;
        ArrayList<Long> friends = new ArrayList<Long>(threeDegrees(user_id1, user_id2, hops));
        pp.displayBoxed("Three Degrees of Separation");
        pp.displayUnderlined("Path: ");

        if(friends != null) {
            int friend_count = friends.size();
            for(int i = friend_count - 1; i >= 0; i--) {
                if(i == 0) {
                    System.out.print("Target id: ");
                } else if(i == friend_count) {
                    System.out.print("Start id: ");
                }
                System.out.println(friends.get(i));
            }
            System.out.println("\nFound path in " + (friend_count - 2) + " connections.");
        } else {
            System.out.println("No path exists with a maximum of 3 connections between ");
            System.out.println(user_id1 + " and " + user_id2);
        }

        for(int i=0; i < friends.size(); i++) {
            System.out.println(friends.get(i));
        }
    }

    // Recursively searches through friends of friends until the target friend is found.
    // Returns List of user_ids in reverse order from target to subject
    private List<Long> threeDegrees(long subject_id, long target_id, int hops) throws SQLException {
        hops++;
        if(hops > 3) {
            return null;
        } else if(subject_id == target_id) {
            return Arrays.asList(target_id);
        } else {
            ArrayList<Long> friend_ids = new ArrayList<Long>(getEstablishedFriends(subject_id));
            for(int i=0; i < friend_ids.size(); i++) {
                long friend_id = friend_ids.get(i);
                List<Long> path = new ArrayList<Long>(threeDegrees(friend_id, target_id, hops));
                if(path != null) {
                    path.add(subject_id);
                    return path;
                }
            }
        }
        return null;
    }

    private void topMessagers(int numUsers, int numMonths) throws SQLException {
        //TO-DO: only outlining the logic
        /*
        For the last k most recent months:
        Join the number of messages sent by a user with the number of messages received
        by a user and sum their counts resulting in the number of messages sent or received by a user
        to or from another user.
        Join this result with the number of messages received by a user's group and sum this count with the previous
        count to yield the total number of messages sent or received by a user in the last k months
        via individual messaging or group messaging.
        */
        query = "SELECT usr_id, (num_usr_msgs + num_group_msgs) AS num_msgs FROM " +
            "(" +
            "SELECT sender_id AS usr_id, (num_sender + num_recip) AS num_usr_msgs FROM" +
                "(" +
                "SELECT sender_id, COUNT(sender_id) as num_sender FROM Messages WHERE MONTHS_BETWEEN(SYSDATE, Messages.time_sent) <= ? AND Messages.type = ? GROUP BY sender_id" +
                "INNER JOIN " +
                "(" +
                "SELECT recip_id, COUNT(recip_id) as num_recip FROM Messages WHERE MONTHS_BETWEEN(SYSDATE, Messages.time_sent) <= ? AND Messages.type = ? GROUP BY recip_id" +
                ")" +
                "ON sender_id = recip_id" +
            ")" +
            "INNER JOIN " +
            "(" +
                "SELECT user_id, COUNT(*) AS num_group_msgs FROM" +
                "(" +
                "SELECT * FROM Messages INNER JOIN ON Messages.recip_id = Members.group_id WHERE Messages.type = ? AND MONTHS_BETWEEN(SYSDATE, Messages.time_sent) <= ?" +
                ") GROUP BY user_id" +
            ")" +
            "ON usr_id = user_id";
    }

    private void dropUser(long user_id) throws SQLException {
        query = "DELETE Profiles WHERE user_id=?";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setLong(1, user_id);
        prepStatement.executeUpdate();

        System.out.println("SUCCESS: User is no longer present in database.\n");
    }
}
