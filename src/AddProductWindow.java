import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddProductWindow {
    private JFrame frame;
    private JTextField nameField, priceField, quantityField;
    private JComboBox<String> categoryBox;
    private JButton addButton, backButton;
    private ProductForm productForm;

    public AddProductWindow(ProductForm productForm) {
        this.productForm = productForm;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Add Product");
        frame.setSize(600, 350); // Increased height for back button
        frame.setLayout(new GridLayout(7, 2, 10, 10)); // Added rows for back button
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(135, 206, 235));

        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        String[] categories = {"Electronics", "Clothing", "Food", "Books", "Others"};
        categoryBox = new JComboBox<>(categories);

        addButton = new JButton("Add Product");
        backButton = new JButton("Back");

        frame.add(new JLabel("Product Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Price:"));
        frame.add(priceField);
        frame.add(new JLabel("Quantity:"));
        frame.add(quantityField);
        frame.add(new JLabel("Category:"));
        frame.add(categoryBox);
        frame.add(new JLabel());  // empty cell
        frame.add(addButton);
        frame.add(new JLabel());  // empty cell
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addButton.addActionListener(e -> addProduct());
        backButton.addActionListener(e -> goBack());

        // Add window listener to handle closing
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    private void addProduct() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String category = (String) categoryBox.getSelectedItem();

        // Basic validation
        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(frame, "Price and quantity must be non-negative.");
                return;
            }

            // Create CategorizedProduct object
            CategorizedProduct product = new CategorizedProduct(name, price, quantity, category);
            product.displayInfo(); // For console demonstration

            // Insert into database
            insertProductIntoDatabase(product);
            JOptionPane.showMessageDialog(frame, "Product added successfully!");

            // Clear fields
            nameField.setText("");
            priceField.setText("");
            quantityField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Price and quantity must be numbers.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void insertProductIntoDatabase(CategorizedProduct product) throws SQLException {
        String sql = "INSERT INTO inventory (name, price, quantity, category) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setString(4, product.getCategory());
            pstmt.executeUpdate();
        }
    }

    private void goBack() {
        frame.dispose();
        productForm.show();
    }
}