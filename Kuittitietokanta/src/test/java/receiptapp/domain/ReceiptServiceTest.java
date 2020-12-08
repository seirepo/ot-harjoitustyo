package receiptapp.domain;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author resure
 */
public class ReceiptServiceTest {
    
    Receipt receipt;
    
    @Before
    public void setUp() {
        
    }
    
    @Test
    public void getTotalReturnsCorrectTotal() {
        ReceiptItem i1 = new ReceiptItem("a", 0.5, true, 5, "pc");
        ReceiptItem i2 = new ReceiptItem("b", 1.5, true, 2, "pc");
        ReceiptItem i3 = new ReceiptItem("c", 5, true, 1, "pc");
        ReceiptItem i4 = new ReceiptItem("d", 0.65, true, 1, "pc");
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        items.add(i1); items.add(i2); items.add(i3); items.add(i4);
        receipt = new Receipt("a", LocalDate.now(), items);
        assertEquals(11.15, receipt.getTotal(), 0.001);
    }
}
