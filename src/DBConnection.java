import java.sql.*;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:inventory.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found");
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void initializeDatabase() {
        String productsTable = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "quantity INTEGER, "
                + "price REAL"
                + ")";

        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "role TEXT NOT NULL"
                + ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(productsTable);
            stmt.execute(usersTable);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO users(username, password, role) VALUES " +
                        "('admin', 'admin123', 'admin')");
            }
        } catch (SQLException e) {
            System.err.println("Database Initialization Failed!");
            e.printStackTrace();
        }
    }
}