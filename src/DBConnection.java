import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:inventory.db?busy_timeout=5000";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();

            boolean recreateInventory = false;
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(inventory);");
            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                if (name.equalsIgnoreCase("id") && !type.toUpperCase().contains("INTEGER")) {
                    recreateInventory = true;
                }
            }

            if (recreateInventory) {
                stmt.executeUpdate("DROP TABLE IF EXISTS inventory;");
            }

            String createInventoryTable = "CREATE TABLE IF NOT EXISTS inventory (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "category TEXT NOT NULL);";
            stmt.executeUpdate(createInventoryTable);

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL);";
            stmt.executeUpdate(createUsersTable);

            insertDefaultUser(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDefaultUser(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM users;")) {

            if (rs.next() && rs.getInt("count") == 0) {
                String insertUserSQL = "INSERT INTO users (username, password, role) VALUES (?, ?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin123");
                    pstmt.setString(3, "admin");
                    pstmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
