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
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import receiptapp.domain.HelperFunctions;
import receiptapp.domain.Receipt;
import receiptapp.domain.ReceiptItem;

/**
 * Luokka tiedon tallentamista varten.
 * @author resure
 */
public class FileReceiptDao {
    
    public ObservableList<Receipt> receipts;
    private String dbFileName;
    private File dbFile;
    private String ERR_MSG = "Tietokantatiedosto on korruptoitunut";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    
    /**
     * Konstruktori, jossa alustetaan receipts-olio tietokannan kuiteilla.
     * Jos tietokantaa ei vielä ole, se luodaan tauluineen.
     * @param fileName tiedston nimi, johon tietokanta tallennetaan
     * @throws Exception jos tietokantatiedoston luominen epäonnistuu
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
        }
        createTables();
        readReceiptDatabase();
    }
    
    /**
     * Luo tietokantataulut kuiteille, kuittiriveille ja näiden id:lle, jotta kuitit
     * ja sen rivit saadaan yhdistettyä.
     * @return onnistuuko taulujen luonti
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
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
    
    /**
     * Lukee tietokannasta kuittirivit, tekee niistä Receipt-olioita ja tallentaa
     * luodut oliot attribuuttina olevaan kuittilistaan.
     * Kerää tietokannasta kuitin id:tä vastaavat kuittirivit ostostaulusta, ja
     * luo näistä kuitille kuittirivioliolistan metodilla readItemsFromDatabase.
     * @return onnistuuko kuittien lukeminen tietokannasta
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
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
                
                PreparedStatement p = db.prepareStatement("SELECT item_id FROM Purchases "
                        + "WHERE receipt_id=?");
                p.setInt(1, id);
                ResultSet itemIdSet = p.executeQuery();
                
                List<Integer> itemIds = new ArrayList<>();
                while (itemIdSet.next()) {
                    itemIds.add(itemIdSet.getInt("item_id"));
                }
                
                ObservableList<ReceiptItem> items = readItemsFromDB(itemIds);
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
    
    /**
     * Lukee tietokannasta ReceiptItem-olioiksi annetun listan id:tä vastaavat
     * kuittirivit. Palauttaa ObservableListin luoduista olioista
     * @param itemIds kuittirivien id:t jotka halutaan lukea olioiksi
     * @return ObservableList ReceiptItem-olioista
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public ObservableList<ReceiptItem> readItemsFromDB(List<Integer> itemIds) throws SQLException {        
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        ReceiptItem item;
        
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            PreparedStatement p;
            for (int itemId : itemIds) {
                p = db.prepareStatement("SELECT * FROM Items WHERE id=?;");
                p.setInt(1, itemId);
                ResultSet itemSet = p.executeQuery();
                
                int dbId = itemSet.getInt("id");
                String product = itemSet.getString("product");
                double price = HelperFunctions.shiftDouble(itemSet.getInt("price"), -2);
                boolean isUnitPrice = itemSet.getBoolean("is_unit_price");
                double quantity = itemSet.getDouble("quantity");
                String unit = itemSet.getString("unit");
                
                item = new ReceiptItem(product, price, isUnitPrice, quantity, unit);
                item.setId(dbId);
                items.add(item);                
            }            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.readItemsFromDB(): " + e);
        } finally {
            db.close();
        }
        return items;
    }

    /**
     * Tallentaa tietokantaan annetun kuitin. Samalla tallennetaan kuitin
     * kuittirivit, ja kuitin ja sen tuotteiden id:t yhdistävä rivi ostostauluun.
     * @param receipt tallennettava kuitti
     * @return onnistuuko kuitin tallennus
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public boolean saveReceipt(Receipt receipt) throws SQLException {
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
            receipt.setId(receiptId);
            
            int result = saveNewReceiptItems(receipt.getItems(), receiptId);
            
            return result >= 0;
            
        } catch (SQLException e) {
            System.out.println("FileReceiptDao.saveReceipt(): " + e);
            throw new SQLException("Virhe kuitin tallennuksessa: " + ERR_MSG);
        } finally {
            db.close();
        }
    }
    
    /**
     * Tallentaa tietokantaan annetun listan kuittirivejä ja yhdistää ne 
     * parametrina annettuun kuitin id:hen. Lisää tuotetaulun lisäksi myös
     * ostostauluun rivin jossa yhdistetään kyseinen tuote annettuun kuittiin.
     * @param items lisättävät kuittirivit
     * @param receiptId kuitin id, jolle kuittirivit kuuluvat
     * @return lisättyjen kuittirivien määrä
     * @throws SQLException jos tietokantayhteys epäonnistuus
     */
    public int saveNewReceiptItems(ObservableList<ReceiptItem> items, int receiptId) throws SQLException {
        int affectedRows = 0;
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            PreparedStatement p;
            for (ReceiptItem item : items) {
                if (dbContainsItem(item)) {
                    updateExistingItem(item, item.getProduct(), item.getPriceCents(), item.getIsUnitPrice(), item.getQuantity(), item.getUnit());
                } else {
                    p = db.prepareStatement("INSERT INTO Items (product, price, is_unit_price, quantity, unit) "
                            + "VALUES (?, ?, ?, ?, ?);",
                            Statement.RETURN_GENERATED_KEYS);
                    p.setString(1, item.getProduct());
                    p.setInt(2, item.getPriceCents());
                    p.setBoolean(3, item.getIsUnitPrice());
                    p.setDouble(4, item.getQuantity());
                    p.setString(5, item.getUnit());
                    affectedRows += p.executeUpdate();

                    ResultSet rs = p.getGeneratedKeys();
                    rs.next();
                    int itemId = rs.getInt(1);
                    item.setId(itemId);

                    int result = saveNewPurchases(receiptId, itemId);
                    if (result < 0) {
                        return -1;
                    }
                }
            }
            return affectedRows;
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.saveNewReceiptItems(): " + e);
            return -1;
        } finally {
            db.close();
        }
    }
    
    /**
     * Tallentaa tietokantaan uuden ostoksen. Liittää annetun kuitin id:hen
     * annetun kuittirivin id:n ja tallentaa rivin tauluun ostoksista. Palauttaa
     * negatiivisen luvun jos tallennus epäonnistuu.
     * @param receiptId kuitin id
     * @param itemId kuittirivin id
     * @return kuinka moneen riviin muutos vaikuttaa
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public int saveNewPurchases(int receiptId, int itemId) throws SQLException {
        if (receiptId < 1 || itemId < 1) {
            return -1;
        }
        
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            Statement s = db.createStatement();
            
            PreparedStatement p = db.prepareStatement("INSERT INTO Purchases (receipt_id, item_id) "
                    + "VALUES(?,?);");
            p.setInt(1, receiptId);
            p.setInt(2, itemId);
            return p.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.saveNewPurchases(): " + e);
            return -1;
        } finally {
            db.close();
        }
    }
    
    /**
     * Poistaa tietokannasta annetun kuitin. Palauttaa negatiivisen luvun, jos
     * poisto epäonnistuu.
     * @param receipt poistettava kuitti
     * @return kuinka monta riviä poistettiin
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
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
            throw new SQLException(ERR_MSG);
        } finally {
            db.close();
        }
    }
    
    /**
     * Poistaa tietokannasta annetun ObservableListin sisältämät kuittirivit.
     * Palauttaa niiden tietokantarivien määrän, joihin toimenpide vaikuttaa.
     * Jos poisto ei onnistu, palautetaan negatiivinen luku.
     * @param items poistettavat kuittirivit
     * @return kuinka monta riviä poistettiin
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public int deleteReceiptItems(ObservableList<ReceiptItem> items) throws SQLException {
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
    
    /**
     * Poistaa tietokannasta annetun kuittirivin. Käyttää poistoon
     * deleteReceiptItems-metodia. Palauttaa negatiivisen kokonaisluvun
     * jos poisto epäonnistuu.
     * @param item poistettava kuittirivi
     * @return onnistuiko poisto
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public int deleteItem(ReceiptItem item) throws SQLException {
        ObservableList<ReceiptItem> items = FXCollections.observableArrayList();
        items.add(item);
        try {
            return deleteReceiptItems(items);
        } catch (Exception e) {
            System.out.println("FileReceiptDao.deleteItem(): " + e);
            return -1;
        }
    }
    
    /**
     * Päivittää annetulle kuitilla parametreina annetut attribuutit tietokantaan.
     * Palauttaa false, jos kuittia ei päivitetä tai sitä ei löydy tietokannasta.
     * @param receipt päivitettävä kuitti
     * @param store uusi myymälä
     * @param date uusi päiväys
     * @return tehtiinkö päivitys
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public boolean updateExistingReceipt(Receipt receipt, String store, LocalDate date) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            if (!dbContainsReceipt(receipt)) {
                return false;
            }
            
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
            
            PreparedStatement p = db.prepareStatement("UPDATE Receipts "
                    + "SET store=?, date=? "
                    + "WHERE id=?");
            p.setString(1, store);
            p.setString(2, date.toString());
            p.setInt(3, receipt.getId());
            
            int affRows = p.executeUpdate();
            return affRows > 0;
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.updateExistingReceipt(): " + e);
            return false;
        } finally {
            db.close();
        }
    }
    
    /**
     * Päivittää annetulle kuittiriville parametreina annetut attribuutit
     * tietokantaan. Palauttaa false, jos kuittiriviä ei päivitetä tai jos
     * kuittia ei löydy tietokannasta.
     * @param item päivitettävä kuittirivi
     * @param product uusi tuote
     * @param price uusi hinta
     * @param isUnitPrice uusi yksikköhintavalinta
     * @param qnty uusi määrä
     * @param unit uusi yksikkö
     * @return tehtiinkö päivitys
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public boolean updateExistingItem(ReceiptItem item, String product, int price, boolean isUnitPrice, double qnty, String unit) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        try {
            if (!dbContainsItem(item)) {
                return false;
            }
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");
                        
            PreparedStatement p = db.prepareStatement("UPDATE Items "
                    + "SET product=?, price=?, is_unit_price=?, quantity=?, unit=? "
                    + "WHERE id=?;");
            p.setString(1, product);
            p.setInt(2, price);
            p.setBoolean(3, isUnitPrice);
            p.setDouble(4, qnty);
            p.setString(5, unit);
            p.setInt(6, item.getId());
            
            int affRows = p.executeUpdate();
            return affRows > 0;
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.updateExistingItem(): " + e);
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Palauttaa tiedon sisältääkö tietokanta parametrina annetun kuittirivin.
     * @param item etsittävä kuittirivi
     * @return onko kuittirivi tietokannassa
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public boolean dbContainsItem(ReceiptItem item) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        int res = 0;
        try {
            PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM Items "
                    + "WHERE id=?");
            p.setInt(1, item.getId());
            ResultSet rs = p.executeQuery();
            res = rs.getInt(1);
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.dbContainsItem(): " + e);
        } finally {
            db.close();
        }
        
        if (res == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Palauttaa tiedon sisältääkö tietokanta parametrina annetun kuitin.
     * @param receipt etsittävä kuitti
     * @return onko kuitti tietokannassa
     * @throws SQLException jos tietokantayhteys epäonnistuu
     */
    public boolean dbContainsReceipt(Receipt receipt) throws SQLException {
        Connection db = DriverManager.getConnection(dbFileName);
        int res = 0;
        try {
            PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM Receipts "
                    + "WHERE id=?");
            p.setInt(1, receipt.getId());
            ResultSet rs = p.executeQuery();
            res = rs.getInt(1);
            
        } catch (Exception e) {
            System.out.println("FileReceiptDao.dbContainsItem(): " + e);
        } finally {
            db.close();
        }
        if (res == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getLatestId() {
        return 0;
    }
    
    public File getFile() {
        return this.dbFile;
    }
    
    public ObservableList<Receipt> getReceipts() {
        return receipts;
    }
}
