import javax.swing.*;
import java.awt.event.*;
class ProductForm extends JFrame {
    private JTextField nameField, quantityField, priceField, categoryField;
    private JButton saveButton;

    public ProductForm() {
        setTitle("Add Product");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField();

        saveButton = new JButton("Save");

        nameLabel.setBounds(20, 20, 100, 25);
        nameField.setBounds(120, 20, 140, 25);

        quantityLabel.setBounds(20, 60, 100, 25);
        quantityField.setBounds(120, 60, 140, 25);

        priceLabel.setBounds(20, 100, 100, 25);
        priceField.setBounds(120, 100, 140, 25);

        categoryLabel.setBounds(20, 140, 100, 25);
        categoryField.setBounds(120, 140, 140, 25);

        saveButton.setBounds(90, 200, 100, 30);

        add(nameLabel); add(nameField);
        add(quantityLabel); add(quantityField);
        add(priceLabel); add(priceField);
        add(categoryLabel); add(categoryField);
        add(saveButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            String category = categoryField.getText();
            ProductService.addProduct(name, quantity, price, category);
            JOptionPane.showMessageDialog(this, "Product Saved");
        });
    }
}
