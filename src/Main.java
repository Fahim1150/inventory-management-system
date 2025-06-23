public class Main {
    public static void main(String[] args) {
        // Initialize database (creates tables if they don't exist)
        DBConnection.initializeDatabase();

        // Show login form first
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}