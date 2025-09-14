public class Main {
    public static void main(String[] args) {
        // Initialize the database first
        DBConnection.initializeDatabase();

        DatabaseManager dbManager = new DatabaseManager();

        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}