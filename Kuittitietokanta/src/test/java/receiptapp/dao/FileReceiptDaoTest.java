package receiptapp.dao;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author resure
 */
public class FileReceiptDaoTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();    
  
    File userFile;
    
    @Before
    public void setUp() throws Exception {
        userFile = testFolder.newFile("testfile_users.txt");       
    }
   
    @Test
    public void ReceiptsAreReadCorrectlyFromFile() {

    }    
    
    @Test
    public void receiptsCanBeEdited() throws Exception {

    }       
    
    @After
    public void tearDown() {
        userFile.delete();
    }
}
