package receiptapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * TODO: 
 * kuittien lisääminen
 * kuittien muokkaaminen
 * kuittien poistaminen
 * tilastoja, faktoja, analyysia
 * @author resure
 */
public class ReceiptService {
    private ArrayList<ReceiptItem> items; // voiskohan tää olla vaan controllerissa kun sitä ei täällä taida tarvita?
    private ArrayList<Receipt> receipts;
    
    
    public ReceiptService() {
        // alusta joku daofile joka hoitaa tallennuksen
        this.items = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }
    
    // TODO: testit
    public boolean addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
        this.items.clear();
        return true;
    }
    
    // TODO: testit
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

    // TODO: testit    
    public int getMeanTotal(LocalDate start, LocalDate end) {
        // hae daosta kuittien määrä annetulla välillä
        return 0;
    }
    
    // TODO: testit    
    public ArrayList<String> getMostBoughtProducts(LocalDate start, LocalDate end, int top) {
        // hae daosta ostetuimmat tuotteet ja palauta top kappaletta tuotteita
        return new ArrayList<String>();
    }

    // TODO: testit    
    public String getMostVisitedStores(LocalDate start, LocalDate end, int top) {
        // hae daosta kaupat joissa on käyty eniten ja palauta niistä top kappaletta
        return "";
    }
    
    
}
