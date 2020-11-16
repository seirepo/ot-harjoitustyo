package kuittitietokanta.domain;

/**
 * Luokka tuotteelle
 * @author resure
 */
public class Product {
    private String name;
    private int price;
    private double quantity;
    private String unit;
    
    public Product(String name, double price, double qty, String unit) {
        this.name = name;
        this.price = (int) (price * 100);
        this.quantity = qty;
        this.unit = unit;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public double getQuantity() {
        return this.quantity;
    }
    
    @Override
    public String toString() {
        double p = this.price / 100 + this.price % 100;
        return String.format("%s\n%2.2f\n%.2f %s\n", this.name, p, this.quantity, this.unit);
    }
}
