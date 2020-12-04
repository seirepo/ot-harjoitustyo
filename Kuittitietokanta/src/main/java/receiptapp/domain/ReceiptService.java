package receiptapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    
    /**
     * Konstruktori luokalle.
     */
    public ReceiptService() {
        this.items = FXCollections.observableArrayList();
        this.receipts = FXCollections.observableArrayList();
    }
    
    /**
     * Metodi uuden kuitin lisäämiseksi.
     * @param store lisättävä kuitti
     * @param date kuitin päivämäärä
     * @return true jos lisäys onnistuu, false jos ei
     */
    public boolean addReceipt(String store, LocalDate date) {
        Receipt receipt = new Receipt(store, date, this.items);
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
    
    public boolean updateItem(ReceiptItem item, String product, double price, boolean isUnitPrice, double qnty, String unit) {
        item.setProduct(product);
        item.setPrice(price);
        item.setIsUnitPrice(isUnitPrice);
        item.setQuantity(qnty);
        item.setUnit(unit);
        return true;
    }
    
    public ObservableList<Receipt> getReceipts() {
        return this.receipts;
    }
    
    public ObservableList<ReceiptItem> getReceiptItems() {
        return this.items;
    }
    
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
