package receiptapp.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import receiptapp.domain.Receipt;
import receiptapp.domain.ReceiptItem;

/**
 * Luokka tiedon tallentamista varten.
 * @author resure
 */
public class FileReceiptDao implements ReceiptDao {
    
    public ObservableList<Receipt> receipts;
    private String dbFileName; // "jdbc:sqlite:receipts.db";
    private File dbFile;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    
    /**
     * Konstruktori, jossa alustetaan receipts-olio tietokannan kuiteilla.
     * Jos tietokantaa ei vielä ole, se luodaan tauluineen.
     * @param fileName tiedston nimi, johon tietokanta tallennetaan
     * @throws SQLException 
     */
    public FileReceiptDao(String fileName) throws SQLException {
        this.receipts = FXCollections.observableArrayList();
        this.dbFileName = "jdbc:sqlite:" + fileName;
        this.dbFile = new File(this.dbFileName);
        
        if (!(dbFile.exists())) {
            try {
                dbFile.createNewFile();
            } catch (Exception e) {
                System.out.println("receiptapp.dao.FileReceiptDao.<init>(): "
                        + e);
            }
            System.out.println("receiptapp.dao.FileReceiptDao.<init>(): tehty tiedosto");
        }
        
        if (createTables()) {
            System.out.println("\ttaulujen luonti onnistui");
        } else {
            System.out.println("\ttaulujen luonti ei onnistunut");
        }
//        Connection db = DriverManager.getConnection("jdbc:sqlite:receipts.db");
//        try {
//            Statement s = db.createStatement();
//            s.execute("PRAGMA foreign_keys = ON;");
//            s.execute("CREATE TABLE IF NOT EXISTS Receipts (id INTEGER PRIMARY KEY, store TEXT, date TEXT);");
//            s.execute("CREATE TABLE IF NOT EXISTS Items (id INTEGER PRIMARY KEY, product STRING, price INTEGER, is_unit_price BOOLEAN, quantity REAL, unit TEXT);");
//            s.execute("CREATE TABLE IF NOT EXISTS Purchases "
//                    + "(receipt_id INTEGER REFERENCES Receipts ON UPDATE CASCADE ON DELETE CASCADE,"
//                    + " item_id INTEGER REFERENCES Items ON UPDATE CASCADE ON DELETE CASCADE);");
//
//            ResultSet receiptSet = s.executeQuery("SELECT * FROM Receipts");
//
//            Receipt receipt;
//            ReceiptItem item;
//        } catch (Exception e) {
//            System.out.println("FileReceiptDao.<init>(): " + e);
//        } finally {
//            db.close();
//        }
    }
    
    public boolean createTables() throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            s.execute("CREATE TABLE IF NOT EXISTS Receipts (id INTEGER PRIMARY KEY, store TEXT, date TEXT);");
            s.execute("CREATE TABLE IF NOT EXISTS Items (id INTEGER PRIMARY KEY, product STRING, price INTEGER, is_unit_price BOOLEAN, quantity REAL, unit TEXT);");
            s.execute("CREATE TABLE IF NOT EXISTS Purchases "
                    + "(receipt_id INTEGER REFERENCES Receipts ON UPDATE CASCADE ON DELETE CASCADE,"
                    + " item_id INTEGER REFERENCES Items ON UPDATE CASCADE ON DELETE CASCADE);");

            ResultSet receiptSet = s.executeQuery("SELECT * FROM Receipts");

            Receipt receipt;
            ReceiptItem item;
        } catch (Exception e) {
            System.out.println("FileReceiptDao.<init>(): " + e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }
    
//    public void save(ObservableList<Receipt> deletedReceipts, ObservableList<ReceiptItem> deletedItems) throws Exception {
//        Connection db = DriverManager.getConnection("jdbc:sqlite:receipts.db");
//        try {
//            Statement s = db.createStatement();
//            s.execute("PRAGMA foreign_keys = ON;");
//        }
//        catch (Exception e) {
//            System.out.println("FileReceiptDao.save() koko metodi: " + e);
//        } finally {
//            db.close();
//        }
//        
//        //List<Receipt> newReceipts = this.receipts.stream().filter(r -> r.getId() < 0).collect(Collectors.toList());
//    }
    
    public int getLatestId() {
        return 0;
    }
    
    public File getFile() {
        return this.dbFile;
    }
        
    @Override
    public ObservableList<Receipt> getAll() {
        return receipts;
    }
    
    @Override
    public Receipt create(Receipt receipt) {
        receipts.add(receipt);
        return receipt;
    }
}
