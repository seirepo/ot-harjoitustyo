package kuittitietokanta.domain;

/**
 * Luokka tuotteelle
 * @author resure
 */
public class Product {
    private String name;
    private int price;
    
    
    public Product(String name, double price) {
        this.name = name;
        this.price = (int) (price * 100);
    }
       
    public int getPrice() {
        return this.price;
    }
    
        @Override
    public String toString() {
        double p = this.price / 100 + this.price % 100;
        return String.format("%s\n%2.2f\n", this.name, p);
    }
}
