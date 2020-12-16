package receiptapp.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDate;
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
import receiptapp.domain.Receipt;

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
    
    public FileReceiptDaoTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        testFileName = "testfile_receipts.db";
        testDao = new FileReceiptDao(testFileName);
        testReceipts = testDao.getAll();
        testFile = testDao.getFile();
        Receipt r1 = new Receipt("store1", LocalDate.parse("2020-10-10"), FXCollections.observableArrayList());
        Receipt r2 = new Receipt("store2", LocalDate.parse("2020-10-05"), FXCollections.observableArrayList());
        Receipt r3 = new Receipt("store3", LocalDate.parse("2020-10-03"), FXCollections.observableArrayList());
        testReceipts.add(r1); testReceipts.add(r2); testReceipts.add(r3);        
    }
   
    @Test
    public void databaseExists() {
        assertTrue(testFile.exists());
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + testFileName)) {
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
    public void receiptCanBeSavedToDatabase() {
        Receipt r = new Receipt("store", LocalDate.parse("2020-11-11"), FXCollections.observableArrayList());

        try {
            assertTrue(testDao.saveNewReceipt(r));
            Receipt rr = testDao.getReceipt(r.getId());
            assertTrue(rr != null);
            assertEquals(rr.getId(), r.getId());
            assertEquals(rr.getStore(), r.getStore());
            assertEquals(rr.getDate(), r.getDate());
        } catch (Exception e) {
            
        }
    }
    
    @Test
    public void receiptsCanBeEdited() throws Exception {

    }       
    
    @After
    public void tearDown() {
        testFile.delete();
    }
}
