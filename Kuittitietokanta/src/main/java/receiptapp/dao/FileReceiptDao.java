/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiptapp.dao;

import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import receiptapp.domain.Receipt;

/**
 *
 * @author resure
 */
public class FileReceiptDao implements ReceiptDao {
    
    public List<Receipt> receipts;
    private String dbFile;
    
    
    public FileReceiptDao(String dbFile) throws SQLException {
        this.receipts = new ArrayList<>();
        this.dbFile = dbFile; // vaikka receiptAppDatabase tms
        File dir = new File(this.dbFile);
        // lue tietokannasta kuitit ja tee niistä kuittiolioita
        
        // katso onko annettu tiedosto jo olemassa, jos on niin lue sieltä tiedot
        // kuittilistaan
        // jos ei, alusta uudet tarvittavat taulut?
        if (!(dir.exists() && dir.isDirectory())) {
            // eli not exists() or not a dir = joko ei ole olemassa tai ei ole dir
            // luo uudet taulut = db-tietokantatiedostot
            File dbReceipts = Paths.get(this.dbFile, "receipts.db").toFile();
            File dbReceiptItems = Paths.get(this.dbFile, "receiptItems.db").toFile();
            File dbReceiptXReceiptItems = Paths.get(this.dbFile, "receiptXReceiptItems.db").toFile();
            
        } else {
            // lue jutut kuittiolioon
            //Connection dbReceipts = DriverManager.getConnection("jdbc:sqlite:" + this.dbFile + ".db");
            //Connection dbReceiptItems = DriverManager.getConnection("jdbc:sqlite:" + )
            
        }
                
    }
    
    private void save() throws Exception {
        // kirjoita receipts-olion kuitit tietokantaan
        System.out.println("Tallennetaan leikisti");
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
