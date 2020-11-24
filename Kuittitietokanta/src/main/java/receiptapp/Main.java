package receiptapp;

import receiptapp.domain.ReceiptItem;
/**
 *
 * @author resure
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReceiptItem item1 = new ReceiptItem("tofu", 1.95, 1, "pc");
        System.out.println("hinta: " + item1.getPrice());
        
    }
    
}
