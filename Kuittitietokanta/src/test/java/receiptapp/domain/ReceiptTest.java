package receiptapp.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author resure
 */
public class ReceiptTest {
    
    Receipt receipt;
    ArrayList<ReceiptItem> products;
    ReceiptItem item1, item2, item3;
    
    @Before
    public void setUp() {
        LocalDate date = LocalDate.of(2020, 10, 10);
        item1 = new ReceiptItem("name", 6, 4, "pc");
        item2 = new ReceiptItem("name", 1.37, 200, "kg");
        item3 = new ReceiptItem("name", 1.45, 1, "l");        
        products = new ArrayList();
        receipt = new Receipt("store", date, products);
    }

     @Test
     public void totalReturnsTheSumRight1() {
        products.add(item1); products.add(item2); products.add(item3);
        assertEquals(882, receipt.total());
     }   
}
