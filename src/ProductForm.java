import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProductForm extends JFrame {
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField priceTextField;
    private JTextField quantityTextField;
    private JButton addButton, updateButton, deleteButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    private ProductService productService;

    public ProductForm() {
        productService = new ProductService();

        setTitle("Inventory Management");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colors
        Color backgroundColor = new Color(230, 230, 230); // light grey background for input panel
        Color labelColor = new Color(45, 60, 80);         // dark blue-grey labels
        Color fieldBackgroundColor = Color.WHITE;         // white for editable fields
        Color fieldTextColor = new Color(80, 80, 90);     // dark text for fields
        Color buttonBackgroundColor = new Color(45, 60, 80);
        Color buttonForegroundColor = Color.WHITE;
        Color tableBackgroundColor = new Color(220, 230, 240);
        Color tableForegroundColor = new Color(20, 20, 20);
        Color tableHeaderBackground = new Color(45, 60, 80);
        Color tableHeaderForeground = Color.WHITE;

        // Panel for inputs
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(backgroundColor);

        // ID Label and TextField (now editable)
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(labelColor);
        inputPanel.add(idLabel);

        idTextField = new JTextField();
        idTextField.setBackground(fieldBackgroundColor);
        idTextField.setForeground(fieldTextColor);
        inputPanel.add(idTextField);

        // Name Label and TextField
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(labelColor);
        inputPanel.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBackground(fieldBackgroundColor);
        nameTextField.setForeground(fieldTextColor);
        inputPanel.add(nameTextField);

        // Price Label and TextField
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(labelColor);
        inputPanel.add(priceLabel);

        priceTextField = new JTextField();
        priceTextField.setBackground(fieldBackgroundColor);
        priceTextField.setForeground(fieldTextColor);
        inputPanel.add(priceTextField);

        // Quantity Label and TextField
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setForeground(labelColor);
        inputPanel.add(quantityLabel);

        quantityTextField = new JTextField();
        quantityTextField.setBackground(fieldBackgroundColor);
        quantityTextField.setForeground(fieldTextColor);
        inputPanel.add(quantityTextField);

        // Buttons
        addButton = new JButton("Add");
        addButton.setBackground(buttonBackgroundColor);
        addButton.setForeground(buttonForegroundColor);

        updateButton = new JButton("Update");
        updateButton.setBackground(buttonBackgroundColor);
        updateButton.setForeground(buttonForegroundColor);

        deleteButton = new JButton("Delete");
        deleteButton.setBackground(buttonBackgroundColor);
        deleteButton.setForeground(buttonForegroundColor);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        inputPanel.add(new JLabel());  // filler cell
        inputPanel.add(buttonPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Table for product list
        String[] columns = {"ID", "Name", "Price", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
            // Make cells non-editable
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setBackground(tableBackgroundColor);
        productTable.setForeground(tableForegroundColor);
        productTable.getTableHeader().setBackground(tableHeaderBackground);
        productTable.getTableHeader().setForeground(tableHeaderForeground);

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load existing products into table
        loadTable();

        // Add button action
        addButton.addActionListener(e -> {
            try {
                addProduct();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid price, quantity and ID (if provided).", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update button action
        updateButton.addActionListener(e -> {
            try {
                updateProduct();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please select a product and enter valid ID, price and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> deleteProduct());

        // Table row click to load data into inputs
        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    idTextField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameTextField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    priceTextField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    quantityTextField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            Object[] row = {p.getId(), p.getName(), p.getPrice(), p.getQuantity()};
            tableModel.addRow(row);
        }
    }

    private void addProduct() {
        // Allow user to input ID manually or leave blank for auto-increment
        String idText = idTextField.getText().trim();
        int id = 0;
        if (!idText.isEmpty()) {
            id = Integer.parseInt(idText);
        }
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        int quantity = Integer.parseInt(quantityTextField.getText());

        Product product = new Product(id, name, price, quantity);
        productService.addProduct(product);
        loadTable();
        clearInputs();
    }

    private void updateProduct() {
        int id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        int quantity = Integer.parseInt(quantityTextField.getText());

        Product product = new Product(id, name, price, quantity);
        productService.updateProduct(product);
        loadTable();
        clearInputs();
    }

    private void deleteProduct() {
        if (idTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idTextField.getText());
        productService.deleteProduct(id);
        loadTable();
        clearInputs();
    }

    private void clearInputs() {
        idTextField.setText("");
        nameTextField.setText("");
        priceTextField.setText("");
        quantityTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductForm form = new ProductForm();
            form.setVisible(true);
        });
    }
}
