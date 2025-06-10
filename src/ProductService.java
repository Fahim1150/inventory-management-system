import java.sql.*;

class ProductService {
    public static void addProduct(String name, int quantity, double price, String category) {
        String sql = "INSERT INTO products (name, quantity, price, category) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.setString(4, category);
            pstmt.executeUpdate();
            System.out.println("Product added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
