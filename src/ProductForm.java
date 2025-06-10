import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import static sun.net.www.MimeTable.loadTable;

class ProductForm extends JFrame {
    private ProductService service = new ProductService();
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, quantityField, priceField, categoryField;
    private JButton saveButton;

    public ProductForm() {
        setTitle("Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        saveButton = new JButton("Save");

        nameLabel.setBounds(20, 20, 100, 25);
        nameField.setBounds(120, 20, 140, 25);

        quantityLabel.setBounds(20, 60, 100, 25);
        quantityField.setBounds(120, 60, 140, 25);

        priceLabel.setBounds(20, 100, 100, 25);
        priceField.setBounds(120, 100, 140, 25);


        saveButton.setBounds(90, 200, 100, 30);

        formPanel.add(nameLabel); formPanel.add(nameField);
        formPanel.add(quantityLabel); formPanel.add(quantityField);
        formPanel.add(priceLabel); formPanel.add(priceField);
        formPanel.add(addBtn); formPanel.add(updateBtn);
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Quantity", "Price"}, 0);

        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadTable();

        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            int qty = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            service.addProduct(new Product(0, name, qty, price));
            clearFields(nameField, quantityField, priceField);
            loadTable();
        });

        updateBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) return;

            int id = (int) model.getValueAt(selected, 0);
            String name = nameField.getText();
            int qty = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            service.updateProduct(new Product(id, name, qty, price));
            clearFields(nameField, quantityField, priceField);
            loadTable();
        });

        deleteBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) return;
            int id = (int) model.getValueAt(selected, 0);
            service.deleteProduct(id);
            loadTable();
        });
        
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
