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
    private int price; // total price or unit price of item, depending on isUnitPrice
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
            this.price = 1;
        } else {
            int cents = (int) HelperFunctions.shiftDouble(DoubleRounder.round(price, 3), 2);
            if (cents == 0) {
                this.price = 1;
            } else {
                this.price = cents;
            }
        }
        
        if (this.units.contains(unit)) {
            this.unit = unit;
        } else {
            this.unit = "pc";
        }   
    }
    
    /**
     * Päivittää tuotteen kaikki attribuutit.
     * @param product tuote
     * @param price hinta
     * @param isUnitPrice onko yksikköhinta
     * @param qnty määrä
     * @param unit yksikkö
     */
    public void updateProperties(String product, double price, boolean isUnitPrice, double qnty, String unit) {
        setIsUnitPrice(isUnitPrice);
        setProduct(product);
        setPrice(price);        
        setQuantity(qnty);
        setUnit(unit);
    }
    
    public String getProduct() {
        return this.product;
    }
    
    public double getPrice() {
        return HelperFunctions.shiftDouble(this.price, -2);
    }
    
    public int getPriceCents() {
        return this.price;
    }
    
    /**
     * Palauttaa kokonaishinnan sentteinä.
     * @return kokonaishinta senteissä
     */
    public int getTotalPriceCents() {
        if (this.isUnitPrice) {
            int cents = (int) (this.price * this.quantity);
            if (cents == 0) {
                return 1;
            } else {
                return cents;
            }
        }
        return this.price;
    }
    
    /**
     * Palauttaa tuotteen kokonaishinnan. Huomioi tarvittaessa kappalemäärän.
     * @return kokonaishinta euroina
     */
    public double getTotalPrice() {
        if (this.isUnitPrice) {
            double x = this.price * this.quantity;
            double toEuros = HelperFunctions.shiftDouble(x, -2);
            return DoubleRounder.round(toEuros, 2);
        }
        double sum = DoubleRounder.round(HelperFunctions.shiftDouble(this.price, -2), 2);
        return sum;
    }
    
    public boolean getIsUnitPrice() {
        return this.isUnitPrice;
    }
    
    /**
     * Palauttaa tuotteen yksikköhinnan kokonaishinnasta laskettuna.
     * @return yksikköhinta
     */
    public double getUnitPrice() {
        if (this.isUnitPrice) {
            return DoubleRounder.round(HelperFunctions.shiftDouble(this.price, -2), 2);
        } else {
            double val = DoubleRounder.round(getTotalPrice() / this.quantity, 2);
            return val;
        }
    }
    
    /**
     * Palauttaa tuotteen määrän pyöristettynä kolmen desimaalin tarkkuudelle.
     * @return määrä
     */
    public double getQuantity() {
        return DoubleRounder.round(this.quantity, 3);
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
     * ole negatiivista tai pyöristy nollaan. Jos hinta pyöristyy nollaan,
     * asetetaan hinnaksi minimihinta 1 sentti.
     * @param price uusi hinta
     */
    public void setPrice(double price) {
        if (price <= 0) {
            return;
        }
        
        int cents = (int) HelperFunctions.shiftDouble(DoubleRounder.round(price, 3), 2);
        if (cents == 0) {
            this.price = 1;
        } else {
            this.price = cents;
        }
    }
    
    /**
     * Asetetaan kuittiriville uusi kokonaishinta. Asetuksessa huomioidaan se,
     * onko kuittirivillä yksikköhinta. Jos on, asetettu hinta kerrotaan määrällä.
     * @param price uusi hinta
     */
    public void setTotalPrice(double price) {
        if (this.isUnitPrice) {
            setPrice(price * this.quantity);
        } else {
            setPrice(price);
        }
    }

    public void setIsUnitPrice(boolean isUnitPrice) {
        this.isUnitPrice = isUnitPrice;
    }
    
    /**
     * Asettaa tuotteelle uuden määrän. Määrän tulee olla positiivinen. Jos
     * uusi määrä pyöristyy nollaan kolmen desimaalin tarkkuudella, asetetaan
     * määräksi minimi 0.001.
     * @param quantity uusi määrä pyöristettynä kolmen desimaalin tarkkuudelle
     */
    public void setQuantity(double quantity) {
        if (quantity > 0) {
            double q = HelperFunctions.shiftDouble(DoubleRounder.round(quantity, 3), 3);
            if (((int) q) == 0) {
                this.quantity = 0.001;
            } else {
                this.quantity = HelperFunctions.shiftDouble(q, -3);
            }
        }
    }
    
    /**
     * Asettaa kuittiriville uuden yksikön. Yksikkö asetetaan vain jos se kuuluu
     * listaan sallituista yksiköistä.
     * @param unit uusi yksikkö
     */
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
        String s = String.format("%-12s\t%-3.2f\t%-3f\t%-2s\t%-2.2fe / %-2s",
                this.product, HelperFunctions.shiftDouble(this.price, -2), this.quantity, this.unit,
                getUnitPrice(), this.unit);
        return s;
    }
}
