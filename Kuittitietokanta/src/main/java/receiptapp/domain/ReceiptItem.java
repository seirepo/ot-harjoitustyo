package receiptapp.domain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Kuitti yhdelle kuitilla olevalle riville.
 * @author resure
 */
public class ReceiptItem {
    private String product;
    private int price; // kokonaishinta: tästä lasketaan yksikköhinta
    private double quantity;
    private ArrayList<String> units = new ArrayList<>(Arrays.asList("pc", "kg", "l"));
    private String unit;
    private int id = -1;
    
    /**
     * Konstruktori kuittiriville. Konstruktorissa varmistetaan, ettei asetettava
     * hinta tai määrä ole negatiivinen, ja että yksikkö on sallittujen yksikköjen
     * joukossa.
     * @param product tuote
     * @param price hinta
     * @param quantity määrä
     * @param unit yksikkö
     */
    public ReceiptItem(String product, double price, double quantity, String unit) {
        this.product = product;
        
        if (price < 0) {
            this.price = 0;
        } else {
            this.price = (int) (price * 100);
        }
        
        if (quantity <= 0) {
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
    
    public String getProduct() {
        return this.product;
    }
    
    public int getPriceCents() {
        return this.price;
    }
    
    public double getPrice() {
        return HelperFunctions.centsToEuros(this.price);
    }
    
    public int getUnitPrice() {
        return (int) (this.price / quantity);
    }
    
    public double getQuantity() {
        return this.quantity;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public ArrayList<String> getUnits() {
        return this.units;
    }
    
    public int getId() {
        return this.id;
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
                HelperFunctions.centsToEuros(getUnitPrice()), this.unit);
        return "";
    }
}
