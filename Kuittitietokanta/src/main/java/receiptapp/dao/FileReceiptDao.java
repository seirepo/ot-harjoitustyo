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
import java.util.List;
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
    private String dbFile;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    
    /**
     * Konstruktori, jossa alustetaan receipts-olio tietokannan kuiteilla.
     * Jos tietokantaa ei vielä ole, se luodaan tauluineen.
     * @throws SQLException 
     */
    public FileReceiptDao() throws SQLException {        
        this.receipts = FXCollections.observableArrayList();
        this.dbFile = "receipts.db";
        File file = new File(this.dbFile);
        
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("receiptapp.dao.FileReceiptDao.<init>(): "
                        + e);
            }
            System.out.println("receiptapp.dao.FileReceiptDao.<init>(): tehty tiedosto");
        }
        
        Connection db = DriverManager.getConnection("jdbc:sqlite:receipts.db");
        Statement s = db.createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS Receipts (id INTEGER PRIMARY KEY, store TEXT, date TEXT);");
        s.execute("CREATE TABLE IF NOT EXISTS Items (id INTEGER PRIMARY KEY, product STRING, price INTEGER, is_unit_price BOOLEAN, quantity INTEGER, unit TEXT);");
        s.execute("CREATE TABLE IF NOT EXISTS receipts_items (receipt_id INTEGER REFERENCES Receipts, item_id INTEGER REFERENCES Items);");
        
        // lue kuittioliot tietokannasta receipts-olioon
        
        ResultSet receiptSet = s.executeQuery("SELECT * FROM Receipts");
        
        Receipt receipt;
        ReceiptItem item;
        
        while (receiptSet.next()) {
            int id = receiptSet.getInt("id");
            String store = receiptSet.getString("store");
            LocalDate date = LocalDate.parse(receiptSet.getString("date"));
            
            System.out.println("id: " + id);
            System.out.println("store: " + store);
            System.out.println("date: " + date);
            
            receipt = new Receipt(store, date, FXCollections.observableArrayList());
            receipt.setId(id);
            
            PreparedStatement p = db.prepareStatement("SELECT * FROM Items I "
                    + "LEFT JOIN receipts_items U ON I.id = U.item_id "
                    + "WHERE receipt_id=?");
            p.setInt(1, id);
            ResultSet itemSet = p.executeQuery();
            
            
            while (itemSet.next()) {
                int idItem = itemSet.getInt("id");
                String product = itemSet.getString("product");
                int price = itemSet.getInt("price");
                boolean isUnit = itemSet.getBoolean("is_unit_price");
                int quantity = itemSet.getInt("quantity");
                String unit = itemSet.getString("unit");
                
                System.out.println("\t" + idItem + " " + product + " " + price +
                        " " + isUnit + " " + quantity + " " + unit);
                
                item = new ReceiptItem(product, price, isUnit, quantity, unit);
                item.setId(idItem);
                receipt.addItem(item);
            }
            
            this.receipts.add(receipt);
        }
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
