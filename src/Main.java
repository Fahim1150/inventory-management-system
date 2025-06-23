public class Main {
    public static void main(String[] args) {

        DBConnection.initializeDatabase();

        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}