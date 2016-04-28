import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class FaceSpace {
    private static final Scanner consoleScanner = new Scanner(System.in);
    private static final PrettyPrinter pp = new PrettyPrinter();
    private static final String dfString = "yyyy-MM-dd";
    private static final java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(dfString);
    private static SafeConnection connection;
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
            connection = new SafeConnection(url, username, password);

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
            } finally {
                try {
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

    private void runChoice(int choice) throws SQLException {
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
                searchForUserMultiplePrompt(readInt("Number of Search Terms: "));
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
			case 15:
				stressTest();
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
						"15)\tStress Test\n" +
                        "-1)\tHelp\n");
                break;
        }
    }

    private Profile readProfile() {
        return new Profile(-1, readString("First Name: "), readString("Last Name: "), readString("Email: "), readTimestamp("Date of Birth: "), currentTimestamp());
    }

    private Group readGroup() {
        return new Group(-1, readString("Name: "), readString("Description: "), readInt("Capacity: "));
    }

    private Message readMessage() {
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
        prepStatement.setTimestamp(5, currentTimestamp());

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

        prepStatement.setTimestamp(1, currentTimestamp());
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
        ArrayList<Friend> friends = new ArrayList<Friend>(getFriends(user_id));
        pp.displayBoxed("Friends of id: " + user_id);
        for(int i = 0; i < friends.size(); i++) {
            System.out.println(friends.get(i));
        }
        System.out.println("\nNOTE: No more users found.\n");
    }

    private void createGroup(Group group) throws SQLException {
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

    private void displayMessages(long user_id) throws SQLException {
        displayMessages(user_id, false);
    }

    private Profile getProfileFromId(long user_id) throws SQLException {
        query = "SELECT * FROM Profiles WHERE user_id = ?";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setLong(1, user_id);
        resultSet = prepStatement.executeQuery();
        resultSet.next();
        return (new Profile(resultSet));
    }

    private void displayMessages(long user_id, boolean new_only) throws SQLException {
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
            query = "SELECT * FROM Messages INNER JOIN Profiles ON Messages.sender_id = Profiles.user_id WHERE recip_id = ? AND type = ? AND time_sent > ?";
        } else {
            query = "SELECT * FROM Messages INNER JOIN Profiles ON Messages.sender_id = Profiles.user_id WHERE recip_id = ? AND type = ?";
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

    private void displayNewMessages(long user_id) throws SQLException {
        displayMessages(user_id, true);
    }

    private void searchForUserMultiplePrompt(int prompts) throws SQLException {
        Collection<String> searchTerms = new ArrayList<String>();
        for(int i = 0; i < prompts; i++) {
            searchTerms.add(readString("Search Term: "));
        }
        searchForUser(searchTerms);
    }

    private void searchForUser(Iterable<String> searchTerms) throws SQLException {
        query = "SELECT * FROM Profiles WHERE fname=? OR lname=? OR email=?";
        prepStatement = connection.prepareStatement(query);

        for(String searchTerm : searchTerms) {
            prepStatement.setString(1, searchTerm);
            prepStatement.setString(2, searchTerm);
            prepStatement.setString(3, searchTerm);
            resultSet = prepStatement.executeQuery();

            while(resultSet.next()) {
                Profile user = new Profile(resultSet);
                System.out.println(user);
            }
        }
    }

    private void threeDegrees(long user_id1, long user_id2) throws SQLException {
        Stack<Long> path = new Stack<Long>();
        boolean pathExists = threeDegrees(user_id1, user_id2, path);
        pp.displayBoxed("Three Degrees of Separation");
        pp.displayUnderlined("Path: ");

        if(pathExists) {
            int friend_count = path.size();
            System.out.println("\nFound path in " + (friend_count - 1) + " connections.");
            for(Long fid : path) {
                System.out.println(fid);
            }
        } else {
            System.out.println("No path exists with a maximum of 3 connections between ");
            System.out.println(user_id1 + " and " + user_id2);
        }
    }

    // Recursively searches through friends of friends until the target friend is found.
    // Returns true if a path exists, false otherwise.
    private boolean threeDegrees(long subject_id, long target_id, Stack<Long> path) throws SQLException {
        if(path.size() > 3) {
            return false;
        } else if(subject_id == target_id) {
            path.push(target_id);
            return true;
        } else {
            ArrayList<Long> friend_ids = new ArrayList<Long>(getEstablishedFriends(subject_id));
            for(int i=0; i < friend_ids.size(); i++) {
                if(path.contains(friend_ids.get(i))) {
                    continue;
                }
                long friend_id = friend_ids.get(i);
                path.push(subject_id);
                if(threeDegrees(friend_id, target_id, path)) {
                    return true;
                }
                path.pop();
            }
        }
        return false;
    }

    /**
     * displays top numUsers who sent or received messsages in the past numMonths
     * @param numUsers
     * @param numMonths
     * @throws SQLException
     */
    private void topMessagers(int numUsers, int numMonths) throws SQLException {
        /*
        For each profile (user), count the number of messages they sent, the number of messages the received
        from a single user, and the amount of messages they received through membership in a group,
        then sum these three quantities to get the total number of messages. Limit these results
        to the number of months <= numMonths and to the top numUsers
        */
        query = "SELECT user_id, fname, lname, email, dob, last_on, " +
        		"((SELECT COUNT(*) FROM Messages WHERE Messages.sender_id = P.user_id AND MONTHS_BETWEEN(SYSDATE, Messages.time_sent) <= ?)" +
        		" + (SELECT COUNT(*) FROM Messages WHERE Messages.recip_id = P.user_id AND Messages.type = ? AND MONTHS_BETWEEN(SYSDATE, Messages.time_sent) <= ?)" +
        		" + (SELECT COUNT(*) FROM Messages INNER JOIN Members ON Messages.recip_id = Members.group_id WHERE Members.user_id = P.user_id AND Messages.type = ? AND MONTHS_BETWEEN(SYSDATE, Messages.time_sent) <= ?))" +
        		" AS num_msgs FROM Profiles P ORDER BY num_msgs DESC ";

        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, numMonths);
        prepStatement.setInt(2, 1);
        prepStatement.setInt(3, numMonths);
        prepStatement.setInt(4, 2);
        prepStatement.setInt(5, numMonths);

        resultSet = prepStatement.executeQuery();
        while(resultSet.next() && numUsers-- > 0) {
            Profile profile = new Profile(resultSet);
            System.out.print(profile.toString());
            System.out.println("NUMBER OF MESSAGES:\t" + ResultSetWrapper.getInt(resultSet, 7) + "\n");
        }
    }

    private void dropUser(long user_id) throws SQLException {
        query = "DELETE Profiles WHERE user_id=?";
        prepStatement = connection.prepareStatement(query);

        prepStatement.setLong(1, user_id);
        prepStatement.executeUpdate();

        System.out.println("SUCCESS: User is no longer present in database.\n");
    }

	private void stressTest() throws SQLException {
        // NOTE: We are ignoring SQLIntegrityConstraintViolationExceptions. They are only thrown because data is
        //  randomly generated without regard for constraints. If this exception is thrown, just generate new random
        //  data and try again.
        final int insertions = 3000;    // number of new entries to be inserted to database
        final int maxGroupCapacity = 20;	// max group capacity
        final int topUsers = 10;	// number of top users for topMessagers

		// test create user
		System.out.println("Creating " + insertions + " user profiles...");
		for(int i = 0; i < insertions; i++) {
            try {
                long offset = Timestamp.valueOf("1950-01-01 00:00:00").getTime();
                long end = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
                long diff = end - offset + 1;
                Timestamp dob = new Timestamp(offset + (long)(Math.random() * diff));
                offset = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
                end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
                diff = end - offset + 1;
                Timestamp lastOn = new Timestamp(offset + (long)(Math.random() * diff));
                Profile profile = new Profile(-1, "fname " + i, "lname " + i, "email" + i + "@gmail.com", dob, lastOn);
                System.out.println(profile.toString());
                createUser(profile);
            } catch(SQLIntegrityConstraintViolationException ignored) {}
		}

		System.out.println(insertions + " user profiles created");
		readString("Enter any key to display users");

		// display users
		query = "SELECT * FROM Profiles";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Profile p = new Profile(resultSet);
			System.out.println(p.toString());
		}

		readString("Enter any key to stress test initiateFriendship");

		// test initiate friendship
		int friend1[] = new int[insertions];
		int friend2[] = new int[insertions];
		System.out.println("Attempt initiating" + insertions + "random friendships...");
		Random rand = new Random();
		for(int i = 0; i < insertions; i++) {
            try {
                int friendid_1 = rand.nextInt(insertions) + 1;
                int friendid_2 = rand.nextInt(insertions) + 1;
                friend1[i] = friendid_1;
                friend2[i] = friendid_2;
                System.out.println("Attempt initiating friendship between " + friendid_1 + " and " + friendid_2);
                initiateFriendship(friendid_1, friendid_2);
            } catch(SQLIntegrityConstraintViolationException ignored) {}

		}

		readString("Enter any key to display created friendships (likely less than " + insertions + " due to attempted initiation of existing friendship)");

		// display friends
		query = "SELECT * FROM Friends";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Friend f = new Friend(resultSet);
			System.out.println(f.toString());
		}

		readString("Enter any key to stress test establishFriendship");

		// test establish friendship
		System.out.println("Attempt establishing " + insertions + " friendships...");
		for(int i = 0; i < insertions; i++) {
            try {
                System.out.println("Attempt establishing friendship between " + friend1[i] + " and " + friend2[i]);
                establishFriendship(friend1[i], friend2[i]);
            } catch (SQLIntegrityConstraintViolationException ignored) {}
		}

		readString("Enter any key to view established friends (will fail on the same instances that initiateFriendship did)");

		// display friends
		query = "SELECT * FROM Friends";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Friend f = new Friend(resultSet);
			System.out.println(f.toString());
		}

		readString("Enter any key to stress test createGroup");

		// create groups
		for(int i = 0; i < insertions; i++) {
			try {
                Group group = new Group(-1, "group " + i, "description for group " + i, insertions);
                System.out.println(group.toString());
                createGroup(group);
            } catch (SQLIntegrityConstraintViolationException ignored) {}
		}

		readString("Enter any key to view created groups");

		// display groups
		query = "SELECT * FROM Groups";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Group g = new Group(resultSet);
			System.out.println(g.toString());
		}

		readString("Enter any key to stress test addToGroup");

		// add to group
		for(int i = 0; i < insertions; i++) {
            try {
                int groupid = rand.nextInt(insertions) + 1;
                int userid = rand.nextInt(insertions) + 1;
                System.out.println("Attempt to add user " + userid + " to group " + groupid);
                addToGroup(groupid, userid);
            } catch (SQLIntegrityConstraintViolationException ignored) {}
		}

		readString("Enter any key to view group members (addToGroup will fail in the case of a user already being a member of the group " +
		"or in the case that the max capacity has been reached)");

		// display members
		query = "SELECT * FROM Members";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Member m = new Member(resultSet);
			System.out.println(m.toString());
		}

		readString("Enter any key to stressTest sendMessageToUser");
		
		for(int i = 0; i < insertions; i++) {
			long offset = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
			long end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
			long diff = end - offset + 1;
			Timestamp timesent = new Timestamp(offset + (long)(Math.random() * diff));
			Message message = new Message(-1, "subject " + i, rand.nextInt(insertions) + 1, rand.nextInt(insertions) + 1, timesent, "text " + i, MessageType.fromInt(1));
			sendMessageToUser(message);
			System.out.println(message);
		}
		
		readString("Enter any key to view sent messages to users");
		
		// display messages to users
		query = "SELECT * FROM Messages WHERE type = 1";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Message m = new Message(resultSet);
			System.out.println(m.toString());
		}
        
		readString("Enter any key to stressTest sendMessageToGroup");
		
		for(int i = 0; i < insertions; i++) {
			long offset = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
			long end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
			long diff = end - offset + 1;
			Timestamp timesent = new Timestamp(offset + (long)(Math.random() * diff));
			Message message = new Message(-1, "subject " + i, rand.nextInt(insertions) + 1, rand.nextInt(insertions) + 1, timesent, "text " + i, MessageType.fromInt(2));
			sendMessageToGroup(message);
			System.out.println(message);
		}
		
		readString("Enter any key to view sent messages to groups");
		
		// display messages to groups
		query = "SELECT * FROM Messages WHERE type = 2";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Message m = new Message(resultSet);
			System.out.println(m.toString());
		}
        
        readString("Enter any key to stress test displayMessages");
        
        // displayMessages
        for(int i = 1; i <= insertions; i++) {
        	displayMessages(i);
        }
        
        readString("Enter any key to stress test displayNewMessages (should be the same output as displayMessages " +
        "since all messages were sent after the user's last login (profile creation in this case)");
        
        // displayMessages
        for(int i = 1; i <= insertions; i++) {
        	displayNewMessages(i);
        }
        
        readString("Enter any key to stress test searchForUser (matches on first name, last name, and email) "
        		+ " \nSearching for each user by first name in order by id ascending.");
        
        // searchForUser
        for(int i = 1; i <= insertions; i++) {
        	searchForUser("fname " + i);
        }
        
        readString("Enter any key to stress test threeDegrees" +
        "\nWill first add a user with id " + insertions+1 + " and a user with id " + insertions+2 +
        "\n" + insertions+1 + " will initiate a friendship with user " + insertions + " and user " +
        insertions+2 + "." +
        "\n threeDegrees will then be invoked to find a path between user " + insertions + " and " +
        "user " + insertions+2);
        
        // threeDegrees
        // create user insertions+1
        long offset = Timestamp.valueOf("1950-01-01 00:00:00").getTime();
		long end = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
		long diff = end - offset + 1;
		Timestamp dob = new Timestamp(offset + (long)(Math.random() * diff));
		offset = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
		end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
		diff = end - offset + 1;
		Timestamp lastOn = new Timestamp(offset + (long)(Math.random() * diff));
        createUser(new Profile(-1, "fname " + insertions+1, "lname " + insertions+1, "email" + insertions+1 + "@gmail.com", dob, lastOn));
        // create user insertions + 2
        offset = Timestamp.valueOf("1950-01-01 00:00:00").getTime();
		end = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
		diff = end - offset + 1;
		dob = new Timestamp(offset + (long)(Math.random() * diff));
		offset = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
		end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
		diff = end - offset + 1;
		lastOn = new Timestamp(offset + (long)(Math.random() * diff));
        createUser(new Profile(-1, "fname " + insertions+2, "lname " + insertions+2, "email" + insertions+2 + "@gmail.com", dob, lastOn));
        // initiate friendship between insertions+1 and insertions
        initiateFriendship(insertions+1, insertions);
        // initiate friendship between insertions+1 and insertions+2
        initiateFriendship(insertions+1, insertions+2);
        // call threeDegrees on insertions and insertions+2
        threeDegrees(insertions, insertions+2);
        
        readString("Enter any key to stressTest topMessagers." +
        "\n Will first insert " + insertions + " messages to be sent by user " + insertions + " to random users." +
        "\nThey will then be the top messager for the queries testing on the last year, last two years, and last three years");
        
        for(int i = 0; i < insertions; i++) {
			offset = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
			end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
			diff = end - offset + 1;
			Timestamp timesent = new Timestamp(offset + (long)(Math.random() * diff));
			Message message = new Message(-1, "subject " + i, insertions, rand.nextInt(insertions) + 1, timesent, "text " + i, MessageType.fromInt(1));
			sendMessageToUser(message);
			System.out.println(message);
        }
        
        System.out.println("Top 10 messagers for past year.");
        topMessagers(topUsers, 12);
        System.out.println("Top 10 messagers for past 2 years.");
        topMessagers(topUsers, 24);
        System.out.println("Top 10 messagers for past 3 years.");
        topMessagers(topUsers, 36);
        
        readString("Enter any key to stress test dropUsers." +
        "\nWill drop all users and then display all tables");
        for(int i = 1; i <= insertions+2; i++) {
        	dropUser(i);
        }
        
        // display profiles
        readString("Enter any key to display users");

		query = "SELECT * FROM Profiles";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Profile p = new Profile(resultSet);
			System.out.println(p.toString());
		}
        // display friends
        readString("Enter any key to display friendships");

		query = "SELECT * FROM Friends";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Friend f = new Friend(resultSet);
			System.out.println(f.toString());
		}
        // display groups
        readString("Enter any key to display groups");

		query = "SELECT * FROM Groups";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Group g = new Group(resultSet);
			System.out.println(g.toString());
		}
        // display members
        readString("Enter any key to display members");

		query = "SELECT * FROM Members";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
            Member m = new Member(resultSet);
			System.out.println(m.toString());
		}
        // display messages
        readString("Enter any key to display messages");

		query = "SELECT * FROM Messages";
        prepStatement = connection.prepareStatement(query);
        resultSet = prepStatement.executeQuery();
        while(resultSet.next()) {
        	Message m = new Message(resultSet);
			System.out.println(m.toString());
		}
	}
}
