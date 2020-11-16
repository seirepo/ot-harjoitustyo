package kuittitietokanta.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Luokka kuitille
 * @author resure
 */
public class Receipt {
    private String store;
    private LocalDate date;
    private ArrayList<Product> products[];
  
    public Receipt(String store, LocalDate date, ArrayList<Product> products[]) {
        this.store = store;
        this.date = date;
        this.products = products;
    }
        
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("asd");
        
        
        return s.toString();
    }
}
