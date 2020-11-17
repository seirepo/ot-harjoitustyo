package kuittitietokanta;

import kuittitietokanta.domain.*;
import java.util.HashMap;
/**
 *
 * @author resure
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReceiptItem item = new ReceiptItem("tofu", 1.95, 1, "pc");
        System.out.println("hinta: " + item.getPrice());
    }
    
}
