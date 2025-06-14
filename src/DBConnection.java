import java.sql.*;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:inventory.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found");
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "quantity INTEGER, "
                + "price REAL"
                + ")";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database Initialization Failed!");
            e.printStackTrace();
        }
    }
}
