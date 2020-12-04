package receiptapp.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Kuitti yhdelle kuitilla olevalle riville.
 * @author resure
 */
public class ReceiptItem {
    private String product;
    private int price;
    private boolean isUnitPrice;
    private double quantity;
    private List<String> units = new ArrayList<>(Arrays.asList("pc", "kg", "l"));
    private String unit;
    private int id = -1;
    
    /**
     * Konstruktori kuittiriville. Konstruktorissa varmistetaan, ettei asetettava
     * hinta tai määrä ole negatiivinen, ja että yksikkö on sallittujen yksikköjen
     * joukossa.
     * @param product tuote
     * @param price hinta
     * @param isUnitPrice onko annettu hinta yksikköhinta
     * @param quantity määrä
     * @param unit yksikkö
     */
    public ReceiptItem(String product, double price, boolean isUnitPrice, double quantity, String unit) {
        this.product = product;
        this.isUnitPrice = isUnitPrice;
        
        if (quantity <= 0) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }     
        
        if (price < 0) {
            this.price = 0;
        } else {
            this.price = (int) (price * 100);
        }
        
        if (this.units.contains(unit)) {
            this.unit = unit;
        } else {
            this.unit = "pc";
        }   
    }
    
    public String getProduct() {
        return this.product;
    }
    
    public int getTotalPriceCents() {
        if (this.isUnitPrice) {
            return (int) (this.price * this.quantity);
        }
        return this.price;
    }
    
    public double getTotalPrice() {
        if (this.isUnitPrice) {
            return this.price * this.quantity / 100.0;
        }
        return this.price / 100.0;
    }
    
    public boolean getIsUnitPrice() {
        return this.isUnitPrice;
    }
    
    public double getUnitPrice() {
        return getTotalPrice() / this.quantity;
    }
    
    public double getQuantity() {
        return this.quantity;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public List<String> getUnits() {
        return this.units;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public void setPrice(double price) {
        if (price > 0) {
            this.price = (int) (price * 100);
        }
    }
    
    public void setIsUnitPrice(boolean isUnitPrice) {
        this.isUnitPrice = isUnitPrice;
    }
    
    public void setQuantity(double quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }
    
    public void setUnit(String unit) {
        if (this.units.contains(unit)) {
            this.unit = unit;
        }
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return this.product + ";" + this.price + ";" + this.quantity + ";" +
                this.unit;
    }
    
    /**
     * Väliaikainen metodi, joka palauttaa tuotteen attribuutit muotoiltuna
     * merkkijonona.
     * @return tuote merkkijonona
     */
    public String getItem() {
        String s = String.format("%-12s\t%-3.2f\t%-3d\t%-2s\t%-2.2fe / %-2s",
                this.product, HelperFunctions.centsToEuros(this.price), this.quantity, this.unit,
                getUnitPrice(), this.unit);
        return "";
    }
}
