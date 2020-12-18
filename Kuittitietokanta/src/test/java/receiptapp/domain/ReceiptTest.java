package receiptapp.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author resure
 */
public class ReceiptTest {
    
    Receipt receipt;
    ObservableList<ReceiptItem> products;
    ReceiptItem item1, item2, item3;
    
    @Before
    public void setUp() {
        LocalDate date = LocalDate.of(2020, 10, 10);
        item1 = new ReceiptItem("name", 6, true, 4, "pc");
        item2 = new ReceiptItem("name", 1.37, false, 200, "kg");
        item3 = new ReceiptItem("name", 1.45, true, 1, "l");        
        products = FXCollections.observableArrayList();
        receipt = new Receipt("store", date, products);
    }

    @Test
    public void getTotalCentsReturnsTheSumRight() {
       products.add(item1); products.add(item2); products.add(item3);
       assertEquals(2682, receipt.getTotalCents());
       assertEquals(26.82, receipt.getTotal(), 0.01);
    }    
    
    @Test
    public void idIsNegativeOneWhenNotSet() {
        assertEquals(-1, receipt.getId());
    }
    
    @Test
    public void idCanBeSetAndGetsSetRight() {
        receipt.setId(4);
        assertEquals(4, receipt.getId());
        receipt.setId(-10);
        assertEquals(4, receipt.getId());
    }
    
    @Test
    public void getProductCountReturnsRightWhenItemsIsEmpty() {
        assertEquals(0, receipt.getProductCount());
    }
    
    @Test
    public void getProductCountReturnsRightWhenProductUnitIsPc() {
       products.add(item1); products.add(item2); products.add(item3);
       assertEquals(6, receipt.getProductCount());
    }
    
    @Test
    public void dateCannotBeSetInTheFuture() {
        LocalDate c = receipt.getDate();
        LocalDate d = LocalDate.of(c.getYear()+1, c.getMonth(), c.getDayOfMonth());
        receipt.setDate(d);
        assertEquals(c, receipt.getDate());
    }
}
