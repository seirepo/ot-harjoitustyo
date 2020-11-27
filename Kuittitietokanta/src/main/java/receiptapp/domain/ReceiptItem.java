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
    private ArrayList<String> units = new ArrayList<>(Arrays.asList("pc", "kg", "l"));
    private String unit;
    
    public ReceiptItem(String product, double price, int quantity, String unit) {
        this.product = product;
        
        if (price < 0) {
            this.price = 0;
        } else {
            this.price = (int) (price * 100);
        }
        
        if (quantity < 1) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }

        
        if (this.units.contains(unit)) {
            this.unit = unit;
        } else {
            this.unit = "pc";
        }
    }
    
//    public ReceiptItem(String item) {
//        this(item.split(";")[0], Double.parseDouble(item.split(";")[1]),
//                Integer.parseInt(item.split(";")[2]), item.split(";")[3]);
//    }
    
    public String getProduct() {
        return this.product;
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
    
    public ArrayList<String> getUnits() {
        return this.units;
    }
    
    @Override
    public String toString() {
        return this.product + ";" + this.price + ";" + this.quantity + ";" +
                this.unit;
    }
    
    public String getItem() {
        String s = String.format("%-12s\t%-3.2f\t%-3d\t%-2s\t%-2.2fe / %-2s",
                this.product, HelperFunctions.centsToEuros(this.price), this.quantity, this.unit,
                HelperFunctions.centsToEuros(getUnitPrice()), this.unit);
        return s;
    }
}
