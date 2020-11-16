package kuittitietokanta.domain;

import java.util.Date;

/**
 * Luokka kuitille
 * @author resure
 */
public class Receipt {
    private String store;
    private Date date;
    private Product products[];  
  
    public Receipt(String store, Date date, Product products[]) {
        this.store = store;
        this.date = date;
        this.products = products;
    }
}
