package receiptapp.dao;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import receiptapp.domain.Receipt;

/**
 * Luokka tiedon tallentamista varten.
 * @author resure
 */
public class FileReceiptDao implements ReceiptDao {
    
    public ObservableList<Receipt> receipts;
    private String dbFile;
    
    /**
     * Konstruktori, jossa alustetaan receipts-olio tietokannan kuiteilla.
     * Jos tietokantaa ei vielä ole, se luodaan tauluineen.
     * @throws SQLException 
     */
    public FileReceiptDao() throws SQLException {
        this.receipts = FXCollections.observableArrayList();
        this.dbFile = "receipts.db"; // vaikka receiptAppDatabase tms
        File file = new File(this.dbFile);
        // lue tietokannasta kuitit ja tee niistä kuittiolioita
        // katso onko annettu tiedosto jo olemassa, jos on niin lue sieltä tiedot
        // kuittilistaan
        // jos ei, tee tiedosto ja sinne uudet tarvittavat taulut
        
        if (!(file.exists())) {
            // tiedostoa ei ole olemassa
            // luo tietokantatiedosto eli .db + luo uudet taulut sinne
            //File dbReceipts = Paths.get(this.dbFile, "receipts.db").toFile();
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("receiptapp.dao.FileReceiptDao.<init>(): "
                        + e);
            }
            System.out.println("receiptapp.dao.FileReceiptDao.<init>(): tehty tiedosto");
        } // else {
            // lue jutut kuittiolioon
            //Connection dbReceipts = DriverManager.getConnection("jdbc:sqlite:" + this.dbFile + ".db");
            //Connection dbReceiptItems = DriverManager.getConnection("jdbc:sqlite:" + )
        // }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:receipts.db");
            Statement s = db.createStatement();
            s.execute("CREATE TABLE IF NOT EXISTS Receipts (id INTEGER PRIMARY KEY, store TEXT);");
            s.execute("CREATE TABLE IF NOT EXISTS Items (id INTEGER PRIMARY KEY, price INTEGER, is_unit_price BOOLEAN, unit TEXT);");
            s.execute("CREATE TABLE IF NOT EXISTS receipts_items (receipt INTEGER REFERENCES Receipts, item INTEGER REFERENCES Items);");
        
        System.out.println("receiptapp.dao.FileReceiptDao.<init>(): Ei tehty databasea");
                
    }
    
    private void save() throws Exception {
        // kirjoita receipts-olion kuitit tietokantaan
        System.out.println("receiptapp.dao.FileReceiptDao.save(): tallennetaan leikisti");
    }
    
    public int getLatestId() {
        return 0;
    }
    
        
    @Override
    public List<Receipt> getAll() {
        return receipts;
    }
    
    @Override
    public Receipt create(Receipt receipt) {
        receipts.add(receipt);
        return receipt;
    }
}
