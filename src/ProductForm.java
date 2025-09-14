import javax.swing.*;
import java.awt.*;

public class ProductForm {
    private JFrame frame;
    private JComboBox<String> actionBox;

    public ProductForm() {
        createAndShowGUI();
        show();
    }

    public void show() {
        if (frame != null) {
            frame.setVisible(true);
        }
    }

    public void hide() {
        if (frame != null) {
            frame.setVisible(false);
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Product Management");
        frame.setSize(600, 150);
        frame.setLayout(new GridLayout(3, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.getContentPane().setBackground(new Color(135, 206, 235));

        String[] actions = {"Select Action", "Add Product", "Update Product", "Delete Product", "Show Products"};
        actionBox = new JComboBox<>(actions);

        JButton backButton = new JButton("Back to Login");

        frame.add(new JLabel("Choose Action:"));
        frame.add(actionBox);
        frame.add(new JLabel());
        frame.add(backButton);

        frame.setLocationRelativeTo(null);

        actionBox.addActionListener(e -> {
            String selected = (String) actionBox.getSelectedItem();
            switch (selected) {
                case "Add Product":
                    hide();
                    new AddProductWindow(this);
                    break;
                case "Update Product":
                    hide();
                    new UpdateProductWindow(this);
                    break;
                case "Delete Product":
                    hide();
                    new DeleteProductWindow(this);
                    break;
                case "Show Products":
                    hide();
                    new ShowProductsWindow(this);
                    break;
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new LoginForm().setVisible(true);
        });
    }

}
