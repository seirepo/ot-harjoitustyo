package receiptapp.domain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Kuitti yhdelle kuitilla olevalle riville
 * @author resure
 */
public class ReceiptItem {
    private String product;
    private int price; // kokonaishinta: tästä lasketaan yksikköhinta
    private int quantity;
    private final ArrayList<String> UNITS = new ArrayList<>(Arrays.asList("pc", "kg", "l"));
    private String unit;
    
    public ReceiptItem(String product, double price, int quantity, String unit) {
        this.product = product;
        if (price < 0) {
            this.price = 0;
        } else {
            this.price = (int) (price * 100);
        }
        this.quantity = quantity;
        if (this.UNITS.contains(unit)) {
            this.unit = unit;
        } else {
            this.unit = "pc";
        }
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getUnitPrice() {
        return this.price / quantity;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    @Override
    public String toString() {
        String s = String.format("%-12s\t%-3.2f\t%-3d\t%-2s\t%-2.2fe / %-2s",
                this.product, HelperFunctions.centsToEuros(this.price), this.quantity, this.unit,
                HelperFunctions.centsToEuros(getUnitPrice()), this.unit);
        return s;
    }
}
