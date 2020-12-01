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
    private int id = -1;
  
    public Receipt(String store, LocalDate date, ArrayList<ReceiptItem> items) {
        this.store = store;
        this.date = date;
        this.items = items;
    }
    
    public Receipt(String store, LocalDate date, ArrayList<ReceiptItem> items, int id) {
        this(store, date, items);
        this.id=id;
    }
    
    /**
     * Palauttaa kuitin summan sentteinä.
     * @return total
     */
    public int getTotalCents() {
        int total = 0;
        for (ReceiptItem item : items) {
            total += item.getPriceCents();
        }
        return total;
    }
    
    /**
     * Palauttaa kuitin summan euroina.
     * @return total
     */
    public double getTotal() {
        double total = 0;
        for (ReceiptItem item : items) {
            total += item.getPrice();
        }
        return total;
    }
    
    public String getStore() {
        return this.store;
    }
    
    public LocalDate getDate() {
        return this.date;
    }
    
    // TODO: testit!
    public int getProductCount() {
        int count = 0;
        
        for (ReceiptItem item : this.items) {
            if (item.getUnit().equals("pc")) {
                count += item.getQuantity();
            } else {
                count += 1;
            }
        }
        
        return count;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        
        for (ReceiptItem item : items) {
            s.append(item.toString());
        }
        
        
        return s.toString();
    }
}
