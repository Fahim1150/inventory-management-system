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
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductService productService;
    private String userRole;

    public ProductForm(String userRole) {
        this.userRole = userRole;
        productService = new ProductService();

        setTitle("Inventory Management - Logged in as: " + userRole);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colors
        Color backgroundColor = new Color(0, 150, 230);
        Color labelColor = new Color(45, 60, 80);
        Color fieldBackgroundColor = Color.WHITE;
        Color fieldTextColor = new Color(80, 80, 90);
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

        // ID Label and TextField
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

        searchButton = new JButton("Search");
        searchButton.setBackground(buttonBackgroundColor);
        searchButton.setForeground(buttonForegroundColor);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        inputPanel.add(new JLabel());
        inputPanel.add(buttonPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Table for product list
        String[] columns = {"ID", "Name", "Price", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
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

        // Button actions
        addButton.addActionListener(e -> {
            try {
                addProduct();
            } catch (NumberFormatException ex) {
                showError("Please enter valid price, quantity and ID (if provided).");
            }
        });

        updateButton.addActionListener(e -> {
            try {
                updateProduct();
            } catch (NumberFormatException ex) {
                showError("Please select a product and enter valid ID, price and quantity.");
            }
        });

        deleteButton.addActionListener(e -> deleteProduct());

        // Table row selection
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

        // Role-based restrictions
        if ("user".equals(userRole)) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }



    private void loadTable() {
        tableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity()});
        }
    }

    private void addProduct() {
        String idText = idTextField.getText().trim();
        int id = idText.isEmpty() ? 0 : Integer.parseInt(idText);
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
            JOptionPane.showMessageDialog(this,
                    "Select a product to delete.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}