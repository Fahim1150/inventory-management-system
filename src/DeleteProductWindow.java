import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteProductWindow {
    private JFrame frame;
    private JComboBox<String> nameBox;
    private JButton deleteButton, backButton;
    private ProductForm productForm;

    public DeleteProductWindow(ProductForm productForm) {
        this.productForm = productForm;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Delete Product");
        frame.setSize(500, 200);
        frame.setLayout(new GridLayout(3, 2, 10, 10)); // Added row for back button
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(135, 206, 235));

        nameBox = new JComboBox<>();
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");

        frame.add(new JLabel("Select Product:"));
        frame.add(nameBox);
        frame.add(deleteButton);
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadProductNames();

        deleteButton.addActionListener(e -> deleteProduct());
        backButton.addActionListener(e -> goBack());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    private void loadProductNames() {
        nameBox.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM inventory")) {

            while (rs.next()) {
                nameBox.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load products.");
        }
    }

    private void deleteProduct() {
        String name = (String) nameBox.getSelectedItem();
        if (name == null) {
            JOptionPane.showMessageDialog(frame, "Please select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete '" + name + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM inventory WHERE name = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name.trim());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Product deleted successfully.");
                nameBox.removeItem(name);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            }
        }
    }

    private void goBack() {
        frame.dispose();
        productForm.show();
    }
}