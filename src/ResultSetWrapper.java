import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

// TODO: Maybe this should be injected with ResultSet and instantiated
public class ResultSetWrapper {
    public static int getInt(ResultSet result, int index) throws SQLException {
        return result.getInt(index);
    }

    public static double getDouble(ResultSet result, int index) throws SQLException {
        return result.getDouble(index);
    }

    public static long getLong(ResultSet result, int index) throws SQLException {
        return result.getLong(index);
    }

    public static byte getByte(ResultSet result, int index) throws SQLException {
        return result.getByte(index);
    }

    public static String getString(ResultSet result, int index) throws SQLException {
        return result.getString(index);
    }

    public static Timestamp getTimestamp(ResultSet result, int index) throws SQLException {
        return result.getTimestamp(index);
    }

    // These functions exists because ResultSet.wasNull() is used to determine if a value is null,
    //  meaning we can't use constructor chaining in ResultSet constructor for table classes.
    public static Integer getNullableInt(ResultSet result, int index) throws SQLException {
        Integer data = result.getInt(index);
        if(result.wasNull()) {
            data = null;
        }
        return data;
    }

    public static Double getNullableDouble(ResultSet result, int index) throws SQLException {
        Double data = result.getDouble(index);
        if(result.wasNull()) {
            data = null;
        }
        return data;
    }

    public static Long getNullableLong(ResultSet result, int index) throws SQLException {
        Long data = result.getLong(index);
        if(result.wasNull()) {
            data = null;
        }
        return data;
    }

    public static Byte getNullableByte(ResultSet result, int index) throws SQLException {
        Byte data = result.getByte(index);
        if(result.wasNull()) {
            data = null;
        }
        return data;
    }

    public static String getNullableString(ResultSet result, int index) throws SQLException {
        String data = result.getString(index);
        if(result.wasNull()) {
            data = null;
        }
        return data;
    }

    public static Timestamp getNullableTimestamp(ResultSet result, int index) throws SQLException {
        Timestamp data = result.getTimestamp(index);
        if(result.wasNull()) {
            data = null;
        }
        return data;
    }
}
