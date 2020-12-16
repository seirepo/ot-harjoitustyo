package receiptapp.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import receiptapp.domain.HelperFunctions;
import receiptapp.domain.Receipt;
import receiptapp.domain.ReceiptItem;

/**
 *
 * @author resure
 */
public class FileReceiptDaoTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    File testFile;
    String testFileName;
    FileReceiptDao testDao;
    ObservableList<Receipt> testReceipts;
    ReceiptItem item;
    Receipt receipt;
    Random rand = new Random();
    
    public FileReceiptDaoTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        testFileName = "testfile_receipts.db";
        testDao = new FileReceiptDao(testFileName);
        testReceipts = testDao.getAll();
        testFile = testDao.getFile();
        item = new ReceiptItem("product_name", 10.5, true, 0.5, "kg");
        receipt = new Receipt("store", LocalDate.parse("2020-11-11"), FXCollections.observableArrayList());
    }
   
    @Test
    public void databaseExists() {
        assertTrue(testFile.exists());
        try (Connection conn = getConnection()) {
            ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
            assertTrue(rs.next());
            assertTrue(rs.next());
            assertTrue(rs.next());
            assertFalse(rs.next());
        } catch (Exception e) {
            System.out.println("FileReceiptDaoTest.databaseExists(): " + e);
        }
    }
    
    @Test
    public void receiptCanBeSavedToDatabase() throws Exception {
        Receipt receipt = new Receipt("store", LocalDate.parse("2020-11-11"), FXCollections.observableArrayList());

        assertTrue(testDao.saveNewReceipt(receipt));
        Receipt dbReceipt = getReceipt(receipt.getId());
        assertTrue(dbReceipt != null);
        assertEquals(dbReceipt.getId(), receipt.getId());
        assertEquals(dbReceipt.getStore(), receipt.getStore());
        assertEquals(dbReceipt.getDate(), receipt.getDate());
    }
    
    @Test
    public void receiptsCanBeDeleted() throws Exception {
        Receipt receipt = new Receipt("store", LocalDate.parse("2020-11-11"), FXCollections.observableArrayList());
        
        testDao.saveNewReceipt(receipt);
        assertEquals(1, testDao.deleteReceipt(receipt));
        assertEquals(null, getReceipt(receipt.getId()));
    }
    
    @Test
    public void nothingHappensWhenTryingToDeleteReceiptsNotInDB() throws Exception {
        Receipt receipt = new Receipt("store", LocalDate.parse("2020-11-11"), FXCollections.observableArrayList());
        assertEquals(0, testDao.deleteReceipt(receipt));
    }
    
    @Test
    public void saveReceiptItemsSavesItemsToDB() throws Exception {
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        ReceiptItem randItem1 = getRandomItem();
        ReceiptItem randItem2 = getRandomItem();
        items.add(randItem1);
        items.add(randItem2);
        
        assertEquals(2, testDao.saveNewReceiptItems(items, 1));
        
        ReceiptItem dbItem1 = getItem(randItem1.getId());
        ReceiptItem dbItem2 = getItem(randItem2.getId());
        
        assertEquals(dbItem1.getId(), randItem1.getId());
        assertEquals(dbItem1.getProduct(), randItem1.getProduct());
        assertEquals(dbItem1.getIsUnitPrice(), randItem1.getIsUnitPrice());
        assertEquals(dbItem1.getQuantity(), randItem1.getQuantity(), 0.01);
        assertEquals(dbItem1.getUnit(), randItem1.getUnit());
        
        assertEquals(dbItem2.getId(), randItem2.getId());
        assertEquals(dbItem2.getProduct(), randItem2.getProduct());
        assertEquals(dbItem2.getIsUnitPrice(), randItem2.getIsUnitPrice());
        assertEquals(dbItem2.getQuantity(), randItem2.getQuantity(), 0.01);
        assertEquals(dbItem2.getUnit(), randItem2.getUnit());
    }
    
    @Test
    public void nothingHappensWhenSavingEmptyItemList() throws Exception {
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        assertEquals(0, testDao.saveNewReceiptItems(items, 1));
    }
    
    @Test
    public void saveNewPurchasesSavesPurchasesToDB() throws Exception {
        testDao.saveNewPurchases(1, 2);
        testDao.saveNewPurchases(1, 4);
        testDao.saveNewPurchases(2, 4);
        
        try (Connection db = getConnection()) {
            PreparedStatement p = db.prepareStatement("SELECT * FROM Purchases WHERE receipt_id=1 AND item_id=2");
            ResultSet pairs = p.executeQuery();
            
            assertEquals(1, pairs.getInt("receipt_id"));
            assertEquals(2, pairs.getInt("item_id"));
            
            p = db.prepareStatement("SELECT * FROM Purchases WHERE receipt_id=1 AND item_id=4;");
            pairs = p.executeQuery();
            
            assertEquals(1, pairs.getInt("receipt_id"));
            assertEquals(4, pairs.getInt("item_id"));
            
            p = db.prepareStatement("SELECT * FROM Purchases WHERE receipt_id=2");
            pairs = p.executeQuery();
            pairs.next();
            assertEquals(2, pairs.getInt("receipt_id"));
            assertEquals(4, pairs.getInt("item_id"));
            assertFalse(pairs.next());
            
            p = db.prepareStatement("SELECT * FROM Purchases WHERE receipt_id=3");
            pairs = p.executeQuery();
            assertFalse(pairs.next());
            
        } catch (Exception e) {
            System.out.println("FileReceiptDaoTest.saveNewPurchasesSavesPurchasesToDB(): " + e);
        }
    }
    
    @Test
    public void purchasesWithIllegalIdsDontGetSaved() throws Exception {                
        assertEquals(-1, testDao.saveNewPurchases(-1, 4));
        assertEquals(-1, testDao.saveNewPurchases(1, -4));
        assertEquals(-1, testDao.saveNewPurchases(1, 0));
    }
    
    @Test
    public void deleteItemsDeletesItemsFromDB() throws Exception {
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        items.add(getRandomItem());
        items.add(getRandomItem());
        items.add(getRandomItem());
        assertEquals(3, testDao.saveNewReceiptItems(items, 1));
        assertEquals(3, testDao.deleteReceiptItems(items));
    }
    
    @Test
    public void nothingHappensWhenTryingToDeleteEmptyItemsList() throws Exception {
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        assertEquals(0, testDao.deleteReceiptItems(items));        
    }
    
    @After
    public void tearDown() {
        testFile.delete();
    }
    
    
    
    
    
    
    public Receipt getReceipt(int receiptId) {
        Receipt r = null;
        
        try (Connection db = getConnection()) {
            PreparedStatement p = db.prepareStatement("SELECT * FROM Receipts "
                    + "WHERE id=?");
            p.setInt(1, receiptId);
            ResultSet rs = p.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String store = rs.getString("store");
                String date = rs.getString("date");
                r = new Receipt(store, LocalDate.parse(date), FXCollections.observableArrayList());
                r.setId(id);
                return r;
            } else {
                System.out.println("getReceipt: no data");
            }
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.getReceipt(): " + e);
        }
        
        return r;
    }
    
    public ReceiptItem getItem(int itemId) {
        ReceiptItem i = null;
        
        try (Connection db = getConnection()) {
            PreparedStatement p = db.prepareStatement("SELECT * FROM Items "
                    + "WHERE id=?");
            p.setInt(1, itemId);
            ResultSet rs = p.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String product = rs.getString("product");
                int price = rs.getInt("price");
                boolean isUnitPrice = rs.getBoolean("is_unit_price");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                
                i = new ReceiptItem(product, HelperFunctions.shiftDouble(price, -2), isUnitPrice, quantity, unit);
                i.setId(id);
                return i;
            } else {
                System.out.println("getItem: no data");
            }
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.getReceipt(): " + e);
        }
        
        return i;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + testFileName);
    }
    
    public ReceiptItem getRandomItem() {
        double price = rand.nextInt(11) + 1 + rand.nextDouble();
        double quantity = rand.nextInt(5) + 1 + rand.nextDouble();
        String product = "product_" + rand.nextInt(51);
        int u = rand.nextInt(3);
        String unit = "pc";
        if (u % 3 == 0) {
            unit = "kg";
        } else if (u % 3 == 1) {
            unit = "l";
        }
        return new ReceiptItem(product, price, rand.nextBoolean(), quantity, unit);
    }
}
