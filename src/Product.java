public class Product {
    private int id;   // new field for database primary key
    private String name;
    private double price;
    private int quantity;

    // Constructor for creating new Product without ID (before DB insert)
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Constructor for Product fetched from DB (with ID)
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name +
                ", Price: " + price + ", Quantity: " + quantity);
    }
}
