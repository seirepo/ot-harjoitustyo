package receiptapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Luokka kuitille
 * @author resure
 */
public class Receipt {
    private String store;
    private LocalDate date;
    private ArrayList<ReceiptItem> items;
  
    public Receipt(String store, LocalDate date, ArrayList<ReceiptItem> items) {
        this.store = store;
        this.date = date;
        this.items = items;
    }
    
    /**
     * Palauttaa kuitin summan sentteinä
     * @return total
     */
    public int total() {
        int total = 0;
        for (ReceiptItem item : items) {
            total += item.getPrice();
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
