import java.sql.*;

public class DatabaseManager {
    private Connection connect() {
        String url = "jdbc:sqlite:inventory.db"; // or your database path
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void addColumnIfNotExists(String tableName, String columnName, String columnType) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {

            // Check if column already exists
            ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, columnName);
            if (!rs.next()) {
                String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
                stmt.execute(sql);
                System.out.println("Added column " + columnName + " to table " + tableName);
            }
        } catch (SQLException e) {
            System.out.println("Could not add column " + columnName + ": " + e.getMessage());
        }
    }

    // Method to insert a product (example)
    public void insertProduct(String name, double price, int quantity, String category) {
        String sql = "INSERT INTO inventory(name, price, quantity, category) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, category);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting product: " + e.getMessage());
        }
    }

    // Method to display all products (example)
    public void displayAllProducts() {
        String sql = "SELECT * FROM inventory";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Price: " + rs.getDouble("price") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Category: " + rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying products: " + e.getMessage());
        }
    }
}