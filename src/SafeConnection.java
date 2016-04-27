import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SafeConnection {
    private Connection connection;
    private Map<String, PreparedStatement> queryToStatement = new HashMap<String, PreparedStatement>(); // Our use case does not warrant adding the complexity to cap the number of entries stored

    public SafeConnection(String url, String username, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        PreparedStatement statement;
        if(queryToStatement.containsKey(query)) {
            statement = queryToStatement.get(query);
            if(statement.isClosed()) {
                queryToStatement.remove(query);
                queryToStatement.put(query, connection.prepareStatement(query));
            }
        } else {
            statement = connection.prepareStatement(query);
            queryToStatement.put(query, statement);
        }

        return queryToStatement.get(query);
    }

    public void close() throws SQLException {
        for(Map.Entry<String, PreparedStatement> entry : queryToStatement.entrySet()) {
            entry.getValue().close();
        }
        connection.close();
    }
}
