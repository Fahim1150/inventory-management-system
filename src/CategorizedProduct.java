// File: CategorizedProduct.java
public class CategorizedProduct extends Product {
    private String category;

    public CategorizedProduct(String name, double price, int quantity, String category) {
        super(name, price, quantity);
        this.category = category.trim();
    }

    public String getCategory() { return category; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Category: " + category);
    }
}
