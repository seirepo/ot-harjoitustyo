package receiptapp.domain;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import receiptapp.dao.FileReceiptDao;

/**
 * Luokka, joka operoi käyttöliittymän ja tietojen tallennuksen välillä.
 * TODO: 
 * kuittien lisääminen
 * kuittien muokkaaminen
 * kuittien poistaminen
 * tilastoja, faktoja, analyysia
 * @author resure
 */
public class ReceiptService {
    private ObservableList<ReceiptItem> items;
    private ObservableList<Receipt> receipts;
    private FileReceiptDao fileReceiptDao;
    private ObservableList<Receipt> deletedReceipts;
    private ObservableList<ReceiptItem> deletedItems;
    private String sqlErrorMessage = "";
    
    /**
     * Konstruktori luokalle. Parametrina tiedoston nimi johon halutaan
     * tietojen tallentuvan.
     * @param filename tiedoston nimi
     */
    public ReceiptService(String filename) {
        this.items = FXCollections.observableArrayList();
        this.receipts = FXCollections.observableArrayList();
        this.deletedReceipts = FXCollections.observableArrayList();
        this.deletedItems = FXCollections.observableArrayList();
        try {
            this.fileReceiptDao = new FileReceiptDao(filename);
            this.receipts = this.fileReceiptDao.getReceipts();
        } catch (Exception e) {
            System.out.println("receiptapp.domain.ReceiptService.<init>(): " + e);
        }
    }
    
    /**
     * Metodi uuden kuitin lisäämiseksi.
     * @param store lisättävä kuitti
     * @param date kuitin päivämäärä
     * @return true jos lisäys onnistuu, false jos ei
     */
    public boolean addReceipt(String store, LocalDate date) {
        ObservableList<ReceiptItem> receiptItems = FXCollections.observableArrayList();
        
        for (ReceiptItem item : this.items) {
            receiptItems.add(item);
        }
        
        Receipt receipt = new Receipt(store, date, receiptItems);
        try {
            this.fileReceiptDao.saveReceipt(receipt);
        } catch (SQLException e) {
            this.sqlErrorMessage = e.getMessage();
            return false;
        }
        this.receipts.add(receipt);
        this.items.clear();
        return true;
    }

    /**
     * Metodi uuden kuittirivin lisäämiseksi.
     * @param item lisättävä rivi
     * @return true jos lisäys onnistuu, false jos ei
     */
    public boolean addReceiptItem(ReceiptItem item) {
        this.items.add(item);
        return true;
    }
    
    /**
     * Päivittää parametrina annetun kuitin annetuilla tiedoilla ja tallentaa
     * muutokset tietokantaan. Kuitin lisäksi päivitetään myös sen tuotteissa
     * tapahtuneet muutokset.
     * @param receipt päivitettävä kuitti
     * @param store uusi myymälä
     * @param date uusi päiväys
     * @return onnistuuko päivitys
     */
    public boolean updateReceipt(Receipt receipt, String store, LocalDate date) {
        try {
            boolean receiptUpdateResult = this.fileReceiptDao.updateExistingReceipt(receipt, store, date);
            
            if (!receiptUpdateResult) {
                return false;
            }
            receipt.setStore(store);
            receipt.setDate(date);
            
            ObservableList<ReceiptItem> receiptItems = FXCollections.observableArrayList();
            for (ReceiptItem item : this.items) {
                receiptItems.add(item);
            }
            
            int receiptItemsUpdateResult = this.fileReceiptDao.saveNewReceiptItems(receiptItems, receipt.getId());
            
            if (receiptItemsUpdateResult < 0) {
                return false;
            }
            receipt.setItems(receiptItems);            
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Päivittää parametrina annetun kuittirivin annetuilla tiedoilla ja
     * tallentaa muutokset tietokantaan.
     * @param item päivitettävä kuittirivi
     * @param product uusi tuote
     * @param price uusi hinta
     * @param isUnitPrice yksikköhintacheckboxin uusi valinta
     * @param qnty uusi määrä
     * @param unit uusi yksikkö
     * @return onnistuuko päivitys
     */
    public boolean updateItem(ReceiptItem item, String product, double price, boolean isUnitPrice, double qnty, String unit) {
        boolean success = false;
        try {
            int p = (int) HelperFunctions.shiftDouble(price, 2);
            boolean result = this.fileReceiptDao.updateExistingItem(item, product, p, isUnitPrice, qnty, unit);
            if (!result) {
                return false;
            }
            item.updateProperties(product, price, isUnitPrice, qnty, unit);
            success = true;
        } catch (Exception e) {
            return false;
        }
        return success;
    }
    
    /**
     * Poistaa annetun kuittirivin.
     * @param item poistettava kuittirivi
     * @return onnistuiko poisto
     */
    public boolean deleteItem(ReceiptItem item) {
        if (item.getId() < 0) {
            return this.items.remove(item);
        } else {
            this.deletedItems.add(item);
            try {
                boolean result = this.fileReceiptDao.deleteItem(item) > 0;
                if (result) {
                    this.items.remove(item);
                }
                return result;
            } catch (Exception e) {
                return false;
            }
        }
    }
    
    /**
     * Poistaa parametrina annetun kuitin ja poistaa sen tuotteineen myös
     * tietokannasta. Palauttaa true/false poiston onnistumisesta riippuen.
     * @param receipt poistettava kuitti
     * @return onnistuiko poisto
     */
    public boolean deleteReceipt(Receipt receipt) {
        boolean success = false;
        try {
            int result = this.fileReceiptDao.deleteReceipt(receipt);
            if (result > 0) {
                this.receipts.remove(receipt);
                success = true;
            }
            
            int result2 = this.fileReceiptDao.deleteReceiptItems(receipt.getItems());
            if (result2 < 1) {
                success = false;
            }
        } catch (SQLException e) {
            this.sqlErrorMessage = e.getMessage();
            return false;
        } finally {
        }
        return success;
    }
    
    /**
     * Palauttaa listna kaikista kuiteista.
     * @return lista kuiteista
     */
    public ObservableList<Receipt> getReceipts() {
        return this.receipts;
    }
    
    /**
     * Palauttaa listan kaikista tällä hetkellä tarkasteltavista kuittiriveistä.
     * @return lista kuittiriveistä
     */
    public ObservableList<ReceiptItem> getReceiptItems() {
        return this.items;
    }
    
    /**
     * Asettaa tarkasteltaviksi kuittiriveiksi parametrina annetun listan.
     * @param items kuittirivit
     * @return onnistuuko asetus
     */
    public boolean setReceiptItems(ObservableList<ReceiptItem> items) {
        for (ReceiptItem item : items) {
            this.items.add(item);
        }
        return true;
    }
    
    /**
     * Poistaa kaikki tarkasteltavat kuittirivit.
     * @return onnistuuko poisto
     */
    public boolean clearItems() {
        this.items.clear();
        return true;
    }

    /**
     * Palauttaa tarkasteltavana olevien kuittirivien yhteishinnan.
     * @return hinta
     */
    public double getTotal() {
        double sum = 0;
        for (ReceiptItem item : this.items) {
            sum += item.getTotalPrice();
        }
        return sum;
    }
    
    /**
     * Palauttaa sovelluksessa käytettävät yksiköt.
     * @return lista käytetyistä yksiköistä
     */
    public List getUnits() {
        List<String> units = new ArrayList<String>();
        units.add("pc");
        units.add("kg");
        units.add("l");
        return units;
    }

    /**
     * Palauttaa tallennukseen käytettävän tietokannan tiedoston.
     * @return tiedosto
     */
    public File getDbFile() {
        return this.fileReceiptDao.getFile();
    }
    
    /**
     * Palauttaa viimeisimpänä tapahtuneen virheen. Tämän jälkeen asetetaan
     * attribuutti sqlErrorMessage tyhjäksi.
     * @return sql-virheviesti
     */
    public String getSQLErrorMessage() {
        String r = this.sqlErrorMessage + " :^(";
        this.sqlErrorMessage = "";
        return r;
    }
    
    /**
     * Palauttaa kuittien summien keskiarvon annetulla aikavälillä.
     * @param start alkupvm
     * @param end loppupvm
     * @return keskiarvo
     */
    public int getMeanTotal(LocalDate start, LocalDate end) {
        // hae daosta kuittien määrä annetulla välillä
        return 0;
    }
    
    /**
     * Palauttaa listana ostetuimmat tuotteet annetulla aikavälillä.
     * Palautettavien tuotteiden määrän voi valita.
     * @param start alkupvm
     * @param end loppupvm
     * @param top kuinka monta palautetaan
     * @return ostetuimmat tuotteet
     */
    public List<String> getMostBoughtProducts(LocalDate start, LocalDate end, int top) {
        // hae daosta ostetuimmat tuotteet ja palauta top kappaletta tuotteita
        return new ArrayList<String>();
    }

    /**
     * Palauttaa ne kauppojen nimet, joissa on käyty eniten annetulla
     * aikavälillä. Palautettavien kauppojen määrän voi valita.
     * @param start alkupvm
     * @param end loppupvm
     * @param top kuinka monta palautetaan
     * @return käytetyimmät kaupat
     */    
    public List<String> getMostVisitedStores(LocalDate start, LocalDate end, int top) {
        // hae daosta kaupat joissa on käyty eniten ja palauta niistä top kappaletta
        return new ArrayList<>();
    }
    
    
}
