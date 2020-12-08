package receiptapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 * Luokka kuitille.
 * @author resure
 */
public class Receipt {
    private String store;
    private LocalDate date;
    private ObservableList<ReceiptItem> items;
    private int id = -1;
  
    /**
     * Konstruktori uudelle kuitille.
     * TODO: katso ettei päivämääärä ole tulevaisuudessa!
     * @param store kauppa
     * @param date päiväys
     * @param items tuotteet
     */
    public Receipt(String store, LocalDate date, ObservableList<ReceiptItem> items) {
        this.store = store;
        this.date = date;
        this.items = items;
    }
    
    /**
     * Toinen konstruktori kuitille, joka alustaa muiden attribuuttien lisäksi
     * myös id:n. Muut attribuutit alustetaan käyttämällä toista konstruktoria.
     * @param store kauppa
     * @param date päiväys
     * @param items tuotteet
     * @param id id
     */
    public Receipt(String store, LocalDate date, ObservableList<ReceiptItem> items, int id) {
        this(store, date, items);
        this.id = id;
    }
    
    public boolean addItem(ReceiptItem item) {
        return this.items.add(item);
    }
    
    /**
     * Palauttaa kuitin summan sentteinä.
     * @return total
     */
    public int getTotalCents() {
        int total = 0;
        for (ReceiptItem item : items) {
            total += item.getTotalPriceCents();
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
            total += item.getTotalPrice();
        }
        return total;
    }
    
    public String getStore() {
        return this.store;
    }
    
    public LocalDate getDate() {
        return this.date;
    }
    
    public ObservableList<ReceiptItem> getItems() {
        return this.items;
    }
    
    /**
     * Metodi, joka palauttaa kuitilla olevien tuotteiden kappalemäärän.
     * @return tuotteiden kappalemäärä.
     */
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
    
    public void setStore(String store) {
        this.store = store;
    }
    
    public void setDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            return;
        }
        this.date = date;
    }
    
    public void setItems(ObservableList<ReceiptItem> items) {
        this.items = items;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("store: " + this.store + ", date: " + this.date);
        for (ReceiptItem item : items) {
            s.append(item.getItem());
        }
        
        
        return s.toString();
    }
}
