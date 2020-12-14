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
import receiptapp.domain.HelperFunctions;
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
        
        try {
            Connection db = DriverManager.getConnection("jdbc:sqlite:receipts.db");
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

            // luetaan sql-rivit kuittiolioihin ja lisätään ne receipts-listaan
            while (receiptSet.next()) {
                int id = receiptSet.getInt("id");
                String store = receiptSet.getString("store");
                LocalDate date = LocalDate.parse(receiptSet.getString("date"));

                System.out.println("id: " + id);
                System.out.println("store: " + store);
                System.out.println("date: " + date);

                receipt = new Receipt(store, date, FXCollections.observableArrayList());
                receipt.setId(id);

                PreparedStatement p = db.prepareStatement("SELECT * FROM Items I LEFT JOIN Purchases P ON I.id = P.item_id WHERE receipt_id=?;");
                p.setInt(1, id);
                ResultSet itemSet = p.executeQuery();
                // luodaan noudetun taulun sisältö ReceiptItem-olioihin ja lisätään ne
                // juuri luodulle kuittioliolle
                while (itemSet.next()) {
                    int idItem = itemSet.getInt("id");
                    String product = itemSet.getString("product");
                    int price = itemSet.getInt("price");
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
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void save(ObservableList<Receipt> deletedReceipts, ObservableList<ReceiptItem> deletedItems) throws Exception {
        try {
            Connection db = DriverManager.getConnection("jdbc:sqlite:receipts.db");
            Statement s = db.createStatement();
            s.execute("PRAGMA foreign_keys = ON;");

            List<ReceiptItem> items;

            // ensin poistetaan tietokannasta sovelluksessa poistetut kuitit ja niiden tuotteet
            for (Receipt deleted : deletedReceipts) {
                System.out.println("poistettavan id: " + deleted.getId());
                int rid = deleted.getId();
                PreparedStatement pd1 = db.prepareStatement("DELETE FROM Receipts WHERE id=?;");
                pd1.setInt(1, rid);
                pd1.executeUpdate();

                for (ReceiptItem item : deleted.getItems()) {
                    int iid = item.getId();
                    PreparedStatement pd2 = db.prepareStatement("DELETE FROM Items WHERE id=?;");
                    pd2.setInt(1, iid);
                    pd2.executeUpdate();
                }
            }

            // TODO: tee jotain tuotteille jotka on poistettu muuten vaan!
            
 
            for (Receipt receipt : this.receipts) {
                int receiptId = receipt.getId();
                String store = receipt.getStore();
                String date = receipt.getDate().toString();
                items = receipt.getItems();

                if (receiptId < 0) {
                    PreparedStatement pr = db.prepareStatement("INSERT INTO Receipts (store, date)"
                            + "VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
                    pr.setString(1, store);
                    pr.setString(2, date);

                    pr.executeUpdate();
                    ResultSet gk = pr.getGeneratedKeys();
                    gk.next();
                    int kuitinId = gk.getInt(1);
                    System.out.println("uusi id: " + kuitinId);

                    for (ReceiptItem item : items) {
                        String product = item.getProduct();
                        int price = item.getTotalPriceCents();
                        boolean isUnitPrice = item.getIsUnitPrice();
                        double quantity = item.getQuantity();
                        String unit = item.getUnit();
                        System.out.println("tuote: " + product + " " + price + " " +
                                isUnitPrice + " " + quantity + " " + unit);

                        PreparedStatement pi = 
                                db.prepareStatement("INSERT INTO Items (product, price, is_unit_price, quantity, unit) VALUES (?,?,?,?,?);",
                                        Statement.RETURN_GENERATED_KEYS);
                        pi.setString(1, product);
                        pi.setInt(2, price);
                        pi.setBoolean(3, isUnitPrice);
                        pi.setDouble(4, quantity);
                        pi.setString(5, unit);

                        pi.executeUpdate();
                        ResultSet rk = pi.getGeneratedKeys();
                        rk.next();
                        int tuotteenId = rk.getInt(1);
                        System.out.println("uusi item lisätty id:llä " + rk.getInt(1));

                        PreparedStatement p3 = db.prepareStatement("INSERT INTO Purchases VALUES (?,?);");
                        p3.setInt(1, kuitinId);
                        p3.setInt(2, tuotteenId);
                        p3.executeUpdate();
                    } 

                } else {
                    // käsitellään jo tietokannassa olevat, päivittyneet kuitit. niiden id > 0!
                    try {
                        PreparedStatement p = db.prepareStatement("UPDATE Receipts "
                                + "SET store=?, date=? "
                                + "WHERE id=?");
                        p.setString(1, store);
                        p.setString(2, date);
                        p.setInt(3, receiptId);
                        p.executeUpdate();
                    } catch (Exception e) {
                        // heitä oikeesti exception
                        System.out.println("FileReceiptDao.save() updateReceipts: " + e);
                    }
                                        
                    // lisätään kuitille lisätyt tuotteet tai päivitetään olemassaolevat
                    int itemId;
                    for (ReceiptItem item : items) {
                        itemId = item.getId();
                        if (itemId < 0) {
                            try {
                                String product = item.getProduct();
                                int price = item.getTotalPriceCents();
                                boolean isUnitPrice = item.getIsUnitPrice();
                                double quantity = item.getQuantity();
                                String unit = item.getUnit();
                                
                                PreparedStatement p = db.prepareStatement("INSERT INTO Items (product, price, is_unit_price, quantity, unit) "
                                        + "VALUES (?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
                                p.setString(1, product);
                                p.setInt(2, price);
                                p.setBoolean(3, isUnitPrice);
                                p.setDouble(4, quantity);
                                p.setString(5, unit);
                                p.executeUpdate();
                                ResultSet rs = p.getGeneratedKeys();
                                rs.next();
                                int newItemId = rs.getInt(1);
                                
                                // aika sama copypaste kuin ylempänäkin uutta kuittia lisätessä
                                PreparedStatement ps = db.prepareStatement("INSERT INTO Purchases VALUES(?,?);");
                                ps.setInt(1, receiptId);
                                ps.setInt(2, newItemId);
                                ps.executeUpdate();
                            } catch (Exception e) {
                                System.out.println("FileReceiptDao.save() update new items: " + e + 
                                        "\nadding item " + item.getProduct());
                            }
                        } else {
                            try {
                                String product = item.getProduct();
                                int price = item.getTotalPriceCents();
                                boolean isUnitPrice = item.getIsUnitPrice();
                                double quantity = item.getQuantity();
                                String unit = item.getUnit();

                                PreparedStatement p = db.prepareStatement("UPDATE Items " +
                                        "SET product=?, price=?, is_unit_price=?, quantity=?, unit=? "
                                        + "WHERE id=?");
                                p.setString(1, product);
                                p.setInt(2, price);
                                p.setBoolean(3, isUnitPrice);
                                p.setDouble(4, quantity);
                                p.setString(5, unit);
                                p.setInt(6, itemId);
                                p.executeUpdate();
                            } catch (Exception e) {
                                // heitä exception
                                System.out.println("FileReceiptDao.save() update items that already exist: " + e +
                                        "\nadding item " + item.getProduct());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            // heitä oikeesti exception
            System.out.println(e);
        }
    }
    
    public int getLatestId() {
        return 0;
    }
    
        
    @Override
    public ObservableList<Receipt> getAll() {
        // palautettavan pitää olla joko deep clone receipts-attribuutista
        // tai sitten poistaminen täytyy tehdä jotenkin toisin
        return receipts;
    }
    
    @Override
    public Receipt create(Receipt receipt) {
        receipts.add(receipt);
        return receipt;
    }
}
