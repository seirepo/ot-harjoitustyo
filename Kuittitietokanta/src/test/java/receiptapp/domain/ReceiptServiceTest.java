package receiptapp.domain;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author resure
 */
public class ReceiptServiceTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    File testFile;
    String testFileName = "service-test_receipts.db";
    ReceiptService service;
    Receipt receipt;
    ObservableList<ReceiptItem> items;
    ObservableList<Receipt> receipts;
    
    @Before
    public void setUp() {
        service = new ReceiptService(testFileName);
        testFile = service.getDbFile();
        receipt = new Receipt("store", LocalDate.parse("2020-12-14"), FXCollections.observableArrayList());
        items = FXCollections.observableArrayList();
    }
    
    @Test
    public void receiptsIsEmptyInTheBeginning() {
        assertEquals(0, service.getReceipts().size());
    }
    
    @Test
    public void addReceiptItemAddsItem() {
        
    }
    
    @Test
    public void addReceiptAddsReceiptAndItems() {
        items.add(new ReceiptItem("prod_1", 1.5, true, 2, "l"));
        items.add(new ReceiptItem("prod_2", 2.95, false, 1, "kg"));
        items.add(new ReceiptItem("prod_3", 1.95, true, 2, "pc"));
        
        service.setReceiptItems(items);
        assertTrue(service.addReceipt("store_1", LocalDate.parse("2020-12-15")));
        receipts = service.getReceipts();
        assertEquals(1, receipts.size());
        assertEquals(3, receipts.get(0).getItems().size());
                
        List<String> savedProducts = receipts.get(0).getItems().stream()
                .map(i -> i.getProduct()).collect(Collectors.toCollection(ArrayList::new));
        
        for (ReceiptItem item : items) {
            assertTrue(savedProducts.contains(item.getProduct()));
        }
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
    
    @After
    public void tearDown() {
        testFile.delete();
    }
}
