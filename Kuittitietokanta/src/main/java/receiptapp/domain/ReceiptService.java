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
    private ArrayList<ReceiptItem> items;
    private ArrayList<Receipt> receipts;
    
    
    public ReceiptService() {
        // alusta joku daofile joka hoitaa tallennuksen
        this.items = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }
    
        
    public boolean addReceiptItem(ReceiptItem item) {
        this.items.add(item);
        return true;
    }
    
    public ArrayList<ReceiptItem> getReceiptItems() {
        return this.items;
    }
    
    public int getMeanTotal(LocalDate start, LocalDate end) {
        // hae daosta kuittien määrä annetulla välillä
        return 0;
    }
    
    public ArrayList<String> getMostBoughtProducts(LocalDate start, LocalDate end, int top) {
        // hae daosta ostetuimmat tuotteet ja palauta top kappaletta tuotteita
        return new ArrayList<String>();
    }
    
    public String getMostVisitedStores(LocalDate start, LocalDate end, int top) {
        // hae daosta kaupat joissa on käyty eniten ja palauta niistä top kappaletta
        return "";
    }
    
    
}
