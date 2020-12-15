package receiptapp.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.decimal4j.util.DoubleRounder;

/**
 * Kuitti yhdelle kuitilla olevalle riville.
 * @author resure
 */
public class ReceiptItem {
    private String product;
    private int totalPrice;
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
            double q = HelperFunctions.shiftDouble(DoubleRounder.round(quantity, 3), 3);
            if (((int) q) == 0) {
                this.quantity = 0.001;
            } else {
                this.quantity = HelperFunctions.shiftDouble(q, -3);
            }
        }
        
        if (price < 0) {
            this.totalPrice = 1;
        } else {
            int cents = (int) HelperFunctions.shiftDouble(DoubleRounder.round(price, 3), 2);
            if (cents == 0) {
                this.totalPrice = 1;
            } else {
                this.totalPrice = cents;
            }
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
    
    /**
     * Palauttaa kokonaishinnan sentteinä.
     * @return 
     */
    public int getTotalPriceCents() {
        if (this.isUnitPrice) {
            int cents = (int) (this.totalPrice * this.quantity);
            if (cents == 0) return 1;
            else return cents;
        }
        return this.totalPrice;
    }
    
    /**
     * Palauttaa tuotteen kokonaishinnan. Huomioi tarvittaessa kappalemäärän.
     * @return 
     */
    public double getTotalPrice() {
        if (this.isUnitPrice) {
            // return this.totalPrice * this.quantity / 100.0;
            double x = this.totalPrice * this.quantity;
            double roundX = DoubleRounder.round(x, 2);
            return HelperFunctions.shiftDouble(roundX, -2);
        }
        double sum = HelperFunctions.shiftDouble(this.totalPrice, -2); // this.totalPrice / 100.0;
        return sum;
    }
    
    public boolean getIsUnitPrice() {
        return this.isUnitPrice;
    }
    
    /**
     * Palauttaa tuotteen yksikköhinnan kokonaishinnasta laskettuna.
     * @return 
     */
    public double getUnitPrice() {
        if (this.isUnitPrice) {
            return HelperFunctions.shiftDouble(this.totalPrice, -2);
//            double val = HelperFunctions.shiftDouble(this.totalPrice, -2);
//            return DoubleRounder.round(val/this.quantity, 3);
        } else {
            double val = DoubleRounder.round(getTotalPrice() / this.quantity, 3);
            //System.out.println("ReceiptItem.getUnitPrice(); val: " + val);
            return val;
        }
        //double val = getTotalPrice() / this.quantity;
        //val = (int) (val * 100);
        //return val / 100;
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
    
    /**
     * Asettaa tuotteelle uuden kokonaishinnan. Varmistaa ettei asetettava hinta
     * ole negatiivista tai pyöristy nollaan.
     * @param price uusi hinta
     */
    public void setTotalPrice(double price) {
        if (price <= 0) return;
        
        if (this.isUnitPrice) {
            int cents = (int) HelperFunctions.shiftDouble(DoubleRounder.round(price / this.quantity, 3), 2);
            if (cents == 0) {
                this.totalPrice = 1;
            } else {
                this.totalPrice = cents;
            }
        } else {
            int cents = (int) (price * 100);
            if (cents == 0) {
                this.totalPrice = 1;
                return;
            }
            this.totalPrice = cents;
        }
    }
    
    public void setTotalPriceTest(double price) {
//        if (this.isUnitPrice) {
//            setTotalPrice(price / this.quantity);
//        } else {
//            setTotalPrice(price);
//        }
    }
    
    public void setIsUnitPrice(boolean isUnitPrice) {
//        if (this.isUnitPrice) {
//            setTotalPrice(this.totalPrice / this.quantity);
//        }
        this.isUnitPrice = isUnitPrice;
    }
    
    public void setQuantity(double quantity) {
        if (quantity > 0) {
            double q = HelperFunctions.shiftDouble(DoubleRounder.round(quantity, 3), 3);
            if (((int) q) == 0) {
                this.quantity = 0.001;
            } else {
                this.quantity = HelperFunctions.shiftDouble(q, -3);
            }
            //this.quantity = quantity;
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
        return this.product + ";" + this.totalPrice + ";" + this.quantity + ";" +
                this.unit;
    }
    
    /**
     * Väliaikainen metodi, joka palauttaa tuotteen attribuutit muotoiltuna
     * merkkijonona.
     * @return tuote merkkijonona
     */
    public String getItem() {
        String s = String.format("%-12s\t%-3.2f\t%-3d\t%-2s\t%-2.2fe / %-2s",
                this.product, HelperFunctions.shiftDouble(this.totalPrice, -2), this.quantity, this.unit,
                getUnitPrice(), this.unit);
        return s;
    }
    
    /**
     * Väliaikainen testi selkeyttämään daon tulosteita.
     * @return 
     */
    public String getItem1() {
        String s = this.product + "\t" + this.totalPrice + "\t" +
                this.quantity + "\t" + this.isUnitPrice + "\t" + this.unit;
        return s;
    }
}
