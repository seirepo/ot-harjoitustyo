package receiptapp.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import receiptapp.domain.HelperFunctions;
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
    public FileReceiptDao(String fileName) throws Exception {
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
        
        readReceiptDatabase();
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
    
    public boolean readReceiptDatabase() throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        boolean success = true;
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            ResultSet receiptSet = s.executeQuery("SELECT * FROM Receipts;");
            
            Receipt receipt;
            ReceiptItem item;
            
            while (receiptSet.next()) {
                int id = receiptSet.getInt("id");
                String store = receiptSet.getString("store");
                LocalDate date = LocalDate.parse(receiptSet.getString("date"));
                
                receipt = new Receipt(store, date, FXCollections.observableArrayList());
                receipt.setId(id);
                
                PreparedStatement p = db.prepareStatement("SELECT * FROM Items I LEFT JOIN Purchases P ON I.id = P.item_id WHERE receipt_id=?;");
                p.setInt(1, id);
                ResultSet itemSet = p.executeQuery();
                
                while (itemSet.next()) {
                    int idItem = itemSet.getInt("id");
                    String product = itemSet.getString("product");
                    double price = HelperFunctions.shiftDouble(itemSet.getInt("price"),-2);
                    boolean isUnit = itemSet.getBoolean("is_unit_price");
                    int quantity = itemSet.getInt("quantity");
                    String unit = itemSet.getString("unit");

                    System.out.println("\t" + idItem + " " + product + " " + price +
                            " " + isUnit + " " + quantity + " " + unit);

                    item = new ReceiptItem(product, 0, isUnit, quantity, unit);
                    item.setTotalPrice(price);
                    item.setId(idItem);
                    receipt.addItem(item);
                }
                
                this.receipts.add(receipt);
            }            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.readReceiptDatabase(): " + e);
            success = false;
        } finally {
            db.close();
        }
        return success;
    }
    
    public boolean saveReceipt(Receipt receipt) throws SQLException {
        if (receipt.getId() < 0) {
            return saveNewReceipt(receipt);
        } else {
            return updateExistingReceipt(receipt);
        }
    }
    
    public boolean saveNewReceipt(Receipt receipt) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            PreparedStatement p = db.prepareStatement("INSERT INTO Receipts (store, date) "
                    + "VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1, receipt.getStore());
            p.setString(2, receipt.getDate().toString());
            p.executeUpdate();
            
            ResultSet rs = p.getGeneratedKeys();
            rs.next();
            int receiptId = rs.getInt(1);
            System.out.println("uusi id: " + receiptId);
            receipt.setId(receiptId);
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.saveNewReceipt(): " + e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }
    
    public boolean updateExistingReceipt(Receipt receipt) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.updateExistingReceipt(): " + e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public int getLatestId() {
        return 0;
    }
    
    public File getFile() {
        return this.dbFile;
    }
   
//    public Receipt getReceipt(int receiptId) throws SQLException {
//        Receipt r = null;
//        Connection db = DriverManager.getConnection(dbFileName);
//        try {
//            PreparedStatement p = db.prepareStatement("SELECT * FROM Receipts "
//                    + "WHERE id=?");
//            p.setInt(1, receiptId);
//            ResultSet rs = p.executeQuery();
//            
//            if (rs.next()) {
//                int id = rs.getInt("id");
//                String store = rs.getString("store");
//                String date = rs.getString("date");
//                r = new Receipt(store, LocalDate.parse(date), FXCollections.observableArrayList());
//                return r;
//            } else {
//                System.out.println("??no data??");
//            }
//            
//        } catch (Exception e) {
//            System.out.println("FileReceiptDao.getReceipt(): " + e);
//        } finally {
//            db.close();
//        }
//        return r;
//    }
    
    // not working
//    public boolean databaseContainsReceipt(Receipt receipt) throws SQLException {
//        Connection db = DriverManager.getConnection(dbFileName);
//        try {
//            int id = receipt.getId();
//            System.out.println("etsittävän kuitin id: " + id);
//            PreparedStatement p = db.prepareStatement("SELECT store FROM Receipts "
//                    + "WHERE id=?");
//            p.setInt(1, receipt.getId());
//            ResultSet rs = p.executeQuery();
//            if (rs.next() == false) {
//                return false;
//            }
////            System.out.println("asdasdasadasdsadSAsadsa " + p.executeQuery().next());
////            System.out.println("seuraava string: " + rs.getString(1));
//            //return rs.next();
////            System.out.println("asdsadsadasdadsaasddsa " + rs.next());
////            return rs.next();
////            int id = rs.getInt(1);
////            System.out.println("\n\tresultsetin next: " + rs.next() + "\n");
////            System.out.println("\n\tresultsetin seuraava next: " + rs.next() + "\n");
////            System.out.println("\n\tresultsetin getInt: " + rs.getInt("id") + "\n");
//            
//        } catch (Exception e) {
//            System.out.println("FileReceiptDao.databaseContainsReceipt(): " + e);
//            return false;
//        } finally {
//            db.close();
//        }
//        return true;
//    }
    
//    public int getRowsInReceipt() throws SQLException {
//        int rows = 0;
//        Connection db = DriverManager.getConnection(dbFileName);
//        try {
//            Statement s = db.createStatement();
//            s.execute("PRAGMA foreign_keys = ON;");
//            
//            ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Receipts;");
//            
//        } catch (Exception e) {
//            System.out.println("FileReceiptDao.getRowsInReceipt(): " + e);
//            return -1;
//        } finally {
//            db.close();
//        }
//        return rows;
//    }
        
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
