package receiptapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import receiptapp.dao.FileReceiptDao;

/**
 * Luokka, joka operoi käyttöliittymän ja tietojen tallennuksen välillä.
 * TODO: 
 * kuittien lisääminen
 * kuittien muokkaaminen
 * kuittien poistaminen
 * tilastoja, faktoja, analyysia
 * @author resure
 */
public class ReceiptService {
    private ObservableList<ReceiptItem> items;
    private ObservableList<Receipt> receipts;
    private FileReceiptDao fileReceiptDao;
    private ObservableList<Receipt> deletedReceipts;
    private ObservableList<ReceiptItem> deletedItems;
    
    /**
     * Konstruktori luokalle.
     */
    public ReceiptService() {
        this.items = FXCollections.observableArrayList();
        this.receipts = FXCollections.observableArrayList();
        this.deletedReceipts = FXCollections.observableArrayList();
        this.deletedItems = FXCollections.observableArrayList();
        try {
            this.fileReceiptDao = new FileReceiptDao("receipts.db");
            this.receipts = this.fileReceiptDao.getAll();
        } catch (Exception e) {
            System.out.println("receiptapp.domain.ReceiptService.<init>(): " + e);
        }
    }
    
    /**
     * Metodi uuden kuitin lisäämiseksi.
     * @param store lisättävä kuitti
     * @param date kuitin päivämäärä
     * @return true jos lisäys onnistuu, false jos ei
     */
    public boolean addReceipt(String store, LocalDate date) { //throws Exception {
        ObservableList<ReceiptItem> receiptItems = FXCollections.observableArrayList();
        
        for (ReceiptItem item : this.items) {
            receiptItems.add(item);
        }
        
        Receipt receipt = new Receipt(store, date, receiptItems);
        try {
            this.fileReceiptDao.saveReceipt(receipt);
        } catch (Exception e) {
            // avaa error-dialogi tms. heitä virhe eteenpäin
            //throw new Exception("virhe kuitin tallennuksessa: " + e);
            return false;
        }
        this.receipts.add(receipt);
        this.items.clear();
        return true;
    }

    /**
     * Metodi uuden kuittirivin lisäämiseksi.
     * @param item lisättävä rivi
     * @return true jos lisäys onnistuu, false jos ei
     */
    public boolean addReceiptItem(ReceiptItem item) {
        this.items.add(item);        
        return true;
    }
    
    public boolean updateReceipt(Receipt receipt, String store, LocalDate date) {
        receipt.setStore(store);
        receipt.setDate(date);
        
        ObservableList<ReceiptItem> receiptItems = FXCollections.observableArrayList();
        
        for (ReceiptItem item : this.items) {
            receiptItems.add(item);
        }
        
        receipt.setItems(receiptItems);
        
        return true;
    }
    
    public boolean updateItem(ReceiptItem item, String product, double price, boolean isUnitPrice, double qnty, String unit) {
//        item.setIsUnitPrice(isUnitPrice);
//        item.setProduct(product);
//        item.setTotalPrice(price);
//        item.setQuantity(qnty);
//        item.setUnit(unit);
        item.updateProperties(product, price, isUnitPrice, qnty, unit);
        return true;
    }
    
    public boolean deleteItem(ReceiptItem item) {
        if (item.getId() > 0) {
            this.deletedItems.add(item);
        }
        return this.items.remove(item);
    }
    
    public boolean deleteReceipt(Receipt receipt) {
        if (receipt.getId() > 0) {
            this.deletedReceipts.add(receipt);
        }
        return this.receipts.remove(receipt);
    }
    
    public ObservableList<Receipt> getReceipts() {
        return this.receipts;
    }
    
    public ObservableList<ReceiptItem> getReceiptItems() {
        return this.items;
    }
    
    public boolean setReceiptItems(ObservableList<ReceiptItem> items) {
        for (ReceiptItem item : items) {
            this.items.add(item);
        }
        return true;
    }
    
    public boolean clearItems() {
        this.items.clear();
        return true;
    }
    
//    public boolean save() {
//        try {
//            this.fileReceiptDao.save(this.deletedReceipts, this.deletedItems);
//            this.deletedReceipts.clear();
//            return true;
//        } catch (Exception e) {
//            System.out.println("receiptapp.domain.ReceiptService.save(): " + e);
//            return false;
//        }
//    }
    
    public double getTotal() {
        double sum = 0;
        for (ReceiptItem item : this.items) {
            sum += item.getTotalPrice();
        }
        return sum;
    }
    
    public List getUnits() {
        List<String> units = new ArrayList<String>();
        units.add("pc"); units.add("kg"); units.add("l");
        return units;
    }

    /**
     * Metodi palauttaa kuittien summien keskiarvon annetulla aikavälillä.
     * @param start alkupvm
     * @param end loppupvm
     * @return keskiarvo
     */
    public int getMeanTotal(LocalDate start, LocalDate end) {
        // hae daosta kuittien määrä annetulla välillä
        return 0;
    }
    
    /**
     * Metodi palauttaa listana ostetuimmat tuotteet annetulla aikavälillä.
     * Palautettavien tuotteiden määrän voi valita.
     * @param start alkupvm
     * @param end loppupvm
     * @param top kuinka monta palautetaan
     * @return ostetuimmat tuotteet
     */
    public List<String> getMostBoughtProducts(LocalDate start, LocalDate end, int top) {
        // hae daosta ostetuimmat tuotteet ja palauta top kappaletta tuotteita
        return new ArrayList<String>();
    }

    /**
     * Metodi palauttaa ne kauppojen nimet, joissa on käyty eniten annetulla
     * aikavälillä. Palautettavien kauppojen määrän voi valita.
     * @param start alkupvm
     * @param end loppupvm
     * @param top kuinka monta palautetaan
     * @return käytetyimmät kaupat
     */    
    public List<String> getMostVisitedStores(LocalDate start, LocalDate end, int top) {
        // hae daosta kaupat joissa on käyty eniten ja palauta niistä top kappaletta
        return new ArrayList<>();
    }
    
    
}
