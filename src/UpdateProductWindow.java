import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateProductWindow {
    private JFrame frame;
    private JComboBox<String> nameBox;
    private JTextField nameField, priceField, quantityField;
    private JComboBox<String> categoryBox;
    private JButton updateButton, backButton;
    private ProductForm productForm;

    public UpdateProductWindow(ProductForm productForm) {
        this.productForm = productForm;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Update Product");
        frame.setSize(600, 300); // Increased height for additional fields
        frame.setLayout(new GridLayout(7, 2, 10, 10)); // Added rows
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameBox = new JComboBox<>();
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        String[] categories = {"Electronics", "Clothing", "Food", "Books", "Others"};
        categoryBox = new JComboBox<>(categories);

        updateButton = new JButton("Update Product");
        backButton = new JButton("Back");

        frame.add(new JLabel("Select Product:"));
        frame.add(nameBox);
        frame.add(new JLabel("New Name:"));
        frame.add(nameField);
        frame.add(new JLabel("New Price:"));
        frame.add(priceField);
        frame.add(new JLabel("New Quantity:"));
        frame.add(quantityField);
        frame.add(new JLabel("New Category:"));
        frame.add(categoryBox);
        frame.add(updateButton);
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadProductNames();
        nameBox.addActionListener(e -> loadProductDetails());
        updateButton.addActionListener(e -> updateProduct());
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

    private void loadProductDetails() {
        String selectedName = (String) nameBox.getSelectedItem();
        if (selectedName != null) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM inventory WHERE name = ?")) {

                pstmt.setString(1, selectedName);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    priceField.setText(String.valueOf(rs.getDouble("price")));
                    quantityField.setText(String.valueOf(rs.getInt("quantity")));
                    categoryBox.setSelectedItem(rs.getString("category"));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to load product details.");
            }
        }
    }

    private void updateProduct() {
        String oldName = (String) nameBox.getSelectedItem();
        String newName = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String category = (String) categoryBox.getSelectedItem();

        if (oldName == null || newName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a product and enter a new name.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(frame, "Price and quantity must be non-negative.");
                return;
            }

            String sql = "UPDATE inventory SET name = ?, price = ?, quantity = ?, category = ? WHERE name = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newName);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);
                pstmt.setString(4, category);
                pstmt.setString(5, oldName);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Product updated successfully.");

                // Refresh the product list
                loadProductNames();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid number format.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    }

    private void goBack() {
        frame.dispose();
        productForm.show();
    }
}