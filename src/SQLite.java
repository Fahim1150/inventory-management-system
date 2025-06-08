import java.sql.*;

public class SQLite {
    public static void main(String[] args) {
        String dbURL = "jdbc:sqlite:mydatabase.db"; // Replace with your database URL
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            // Your database operations here
            System.out.println("Connected to SQLite database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
