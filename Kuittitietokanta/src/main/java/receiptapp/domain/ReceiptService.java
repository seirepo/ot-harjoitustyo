package receiptapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<ReceiptItem> items;
    private ArrayList<Receipt> receipts;
    
    /**
     * Konstruktori luokalle.
     */
    public ReceiptService() {
        this.items = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }
    
    /**
     * Metodi uuden kuitin lisäämiseksi.
     * @param receipt lisättävä kuitti
     * @return true jos lisäys onnistuu, false jos ei
     */
    public boolean addReceipt(Receipt receipt) {
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
    
    public ArrayList<Receipt> getReceipts() {
        return this.receipts;
    }
    
    public ArrayList<ReceiptItem> getReceiptItems() {
        return this.items;
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
