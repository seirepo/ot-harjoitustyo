package kuittitietokanta.domain;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Luokka kuitille
 * @author resure
 */
public class Receipt {
    private String store;
    private LocalDate date;
    private HashMap<Product, Integer> products;
  
    public Receipt(String store, LocalDate date, HashMap<Product, Integer> products) {
        this.store = store;
        this.date = date;
        this.products = products;
    }
    
    /**
     * Palauttaa kuitin summan sentteinä
     * @return total
     */
    public int total() {
        int total = 0;
        for (Product p : products.keySet()) {
            total += p.getPrice() * products.get(p);
        }
        return total;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("asd");
        
        
        return s.toString();
    }
}
