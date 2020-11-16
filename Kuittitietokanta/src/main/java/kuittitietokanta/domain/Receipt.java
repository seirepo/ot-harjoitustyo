package kuittitietokanta.domain;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Luokka kuitille
 * @author resure
 */
public class Receipt {
    private String store;
    private Date date;
    private ArrayList<Product> products[];  
  
    public Receipt(String store, Date date, ArrayList<Product> products[]) {
        this.store = store;
        this.date = date;
        this.products = products;
    }

}
