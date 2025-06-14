import java.sql.*;
import java.util.*;

public class ProductService {

    public void addProduct(Product product) {
        try (Connection conn = DBConnection.connect()) {
            String sql;
            PreparedStatement pstmt;

            if (product.getId() > 0) {
                // Insert with specified ID
                sql = "INSERT INTO products(id, name, quantity, price) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, product.getId());
                pstmt.setString(2, product.getName());
                pstmt.setInt(3, product.getQuantity());
                pstmt.setDouble(4, product.getPrice());
            } else {
                // Insert without ID, let DB auto-generate
                sql = "INSERT INTO products(name, quantity, price) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, product.getName());
                pstmt.setInt(2, product.getQuantity());
                pstmt.setDouble(3, product.getPrice());
            }

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateProduct(Product product) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "UPDATE products SET name = ?, quantity = ?, price = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "DELETE FROM products WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
