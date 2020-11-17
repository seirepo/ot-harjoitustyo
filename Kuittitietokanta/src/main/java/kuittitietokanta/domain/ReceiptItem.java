package kuittitietokanta.domain;

/**
 * Kuitti yhdelle kuitilla olevalle riville
 * @author resure
 */
public class ReceiptItem {
    private String product;
    private int price; // kokonaishinta: tästä lasketaan yksikköhinta
    private int quantity;
    private final String[] UNITS = {"pc", "kg", "l"};
    private String unit;
    
    public ReceiptItem(String product, double price, int quantity, String unit) {
        this.product = product;
        this.price = (int) (price * 100);
        this.quantity = quantity;
        this.unit = unit;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getUnitPrice() {
        return this.price / quantity;
    }
}
