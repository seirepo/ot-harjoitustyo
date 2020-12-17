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
public class FileReceiptDao { //implements ReceiptDao {
    
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
        this.dbFile = new File(fileName);
        
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
                
                PreparedStatement p = db.prepareStatement("SELECT * FROM Items I"
                        + " LEFT JOIN Purchases P ON I.id = P.item_id WHERE receipt_id=?;");
                p.setInt(1, id);
                ResultSet itemSet = p.executeQuery();
                
                ObservableList<ReceiptItem> items = readItemsFromDB(itemSet);
                
//                while (itemSet.next()) {
//                    int idItem = itemSet.getInt("id");
//                    String product = itemSet.getString("product");
//                    double price = HelperFunctions.shiftDouble(itemSet.getInt("price"),-2);
//                    boolean isUnit = itemSet.getBoolean("is_unit_price");
//                    int quantity = itemSet.getInt("quantity");
//                    String unit = itemSet.getString("unit");
//
//                    System.out.println("\t" + idItem + " " + product + " " + price +
//                            " " + isUnit + " " + quantity + " " + unit);
//
//                    item = new ReceiptItem(product, 0, isUnit, quantity, unit);
//                    item.setPrice(price);
//                    item.setId(idItem);
//                    //receipt.addItem(item);
//                }
                receipt.setItems(items);
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
    
    public ObservableList<ReceiptItem> readItemsFromDB(ResultSet itemSet) throws SQLException {
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        ReceiptItem item;
        
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
            item.setPrice(price);
            item.setId(idItem);
            items.add(item);
        }
        return items;
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
            System.out.println("uuden kuitin uusi id: " + receiptId);
            receipt.setId(receiptId);
            
            int result = saveNewReceiptItems(receipt.getItems(), receiptId);
            
            return result >= 0;
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.saveNewReceipt(): " + e);
            return false;
        } finally {
            db.close();
        }
        // return true;
    }
    
    public int saveNewReceiptItems(ObservableList<ReceiptItem> items, int receiptId) throws SQLException {
        //boolean success = false;
        int affectedRows = 0;
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            PreparedStatement p;
            for (ReceiptItem item : items) {
                p = db.prepareStatement("INSERT INTO Items (product, price, is_unit_price, quantity, unit) "
                        + "VALUES (?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
                p.setString(1, item.getProduct());
                p.setInt(2, item.getTotalPriceCents());
                p.setBoolean(3, item.getIsUnitPrice());
                p.setDouble(4, item.getQuantity());
                p.setString(5, item.getUnit());
                affectedRows += p.executeUpdate();
                
                ResultSet rs = p.getGeneratedKeys();
                rs.next();
                int itemId = rs.getInt(1);
                item.setId(itemId);
                System.out.println("uuden itemin id: " + itemId);
                
                int result = saveNewPurchases(receiptId, itemId);
                if (result < 0) return -1;
            }
            return affectedRows;
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.saveNewReceiptItems(): " + e);
            return -1;
        } finally {
            db.close();
        }
        //return success;
        //return affectedRows;
    }
    
    public int saveNewPurchases(int receiptId, int itemId) throws Exception {
        if (receiptId < 1 || itemId < 1) return -1;
        
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
//            s.execute("PRAGMA foreign_keys = ON;");
            
            PreparedStatement p = db.prepareStatement("INSERT INTO Purchases (receipt_id, item_id) "
                    + "VALUES(?,?);");
            p.setInt(1, receiptId);
            p.setInt(2, itemId);
            return p.executeUpdate(); // kuinka moneen riviin muutos vaikutti?
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.saveNewPurchases(): " + e);
            return -1;
        } finally {
            db.close();
        }
    }
    
    public int deleteReceipt(Receipt receipt) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            PreparedStatement p = db.prepareStatement("DELETE FROM Receipts "
                    + "WHERE id=?");
            p.setInt(1, receipt.getId());
            return p.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("deleteReceipt(): " + e);
        } finally {
            db.close();
        }
        
        return -1;
    }
    
    public int deleteReceiptItems(ObservableList<ReceiptItem> items) throws Exception {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            int affectedRows = 0;
            for (ReceiptItem item : items) {
                
                PreparedStatement p = db.prepareStatement("DELETE FROM Items WHERE id=?");
                p.setInt(1, item.getId());
                affectedRows += p.executeUpdate();
            }
            
            return affectedRows;
        } catch (Exception e) {
            System.out.println("FileReceiptDao.databaseContainsReceipt(): " + e);
            return -1;
        } finally {
            db.close();
        }
    }
    
    public boolean updateExistingReceipt(Receipt receipt) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
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
    
    public ObservableList<Receipt> getAll() throws Exception {
//        readReceiptDatabase();
        return receipts;
    }
}
