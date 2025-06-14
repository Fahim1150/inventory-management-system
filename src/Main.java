public class Main {
    public static void main(String[] args) {
        DBConnection.initializeDatabase();
        new ProductForm().setVisible(true);
    }
}
