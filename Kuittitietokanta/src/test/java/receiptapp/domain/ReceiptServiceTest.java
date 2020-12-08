package receiptapp.domain;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author resure
 */
public class ReceiptServiceTest {
    ReceiptService service;
    Receipt receipt;
    
    @Before
    public void setUp() {
        service = new ReceiptService();
    }
    
    @Test
    public void getTotalReturnsCorrectTotal() {
        ReceiptItem i1 = new ReceiptItem("a", 0.5, true, 5, "pc");
        ReceiptItem i2 = new ReceiptItem("b", 1.5, true, 2, "pc");
        ReceiptItem i3 = new ReceiptItem("c", 5, true, 1, "pc");
        ReceiptItem i4 = new ReceiptItem("d", 0.65, true, 1, "pc");
        service.addReceiptItem(i1); service.addReceiptItem(i2);
        service.addReceiptItem(i3); service.addReceiptItem(i4);
        assertEquals(11.15, service.getTotal(), 0.001);
    }
    
    @Test
    public void addReceiptItemAddsReceiptItem() {
        ReceiptItem i1 = new ReceiptItem("a", 0.5, true, 5, "pc");
        ReceiptItem i2 = new ReceiptItem("b", 1.5, true, 2, "pc");
        service.addReceiptItem(i1); service.addReceiptItem(i2);
        assertTrue(service.getReceiptItems().size() > 0);
        assertEquals(i1, service.getReceiptItems().get(0));
        assertEquals(i2, service.getReceiptItems().get(1));
    }
    
    @Test
    public void clearItemsClearsItems() {
        ReceiptItem i1 = new ReceiptItem("a", 0.5, true, 5, "pc");
        ReceiptItem i2 = new ReceiptItem("b", 1.5, true, 2, "pc");
        ReceiptItem i3 = new ReceiptItem("c", 5, true, 1, "pc");
        ReceiptItem i4 = new ReceiptItem("d", 0.65, true, 1, "pc");
        service.addReceiptItem(i1); service.addReceiptItem(i2);
        service.addReceiptItem(i3); service.addReceiptItem(i4);
        service.clearItems();
        assertEquals(0, service.getReceiptItems().size());
    }
}
