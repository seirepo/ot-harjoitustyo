/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiptapp.dao;

import java.util.List;
import receiptapp.domain.Receipt;

/**
 *
 * @author resure
 */
public class FileReceiptDao implements ReceiptDao {
    
    public List<Receipt> receipts;
    private String file;
    
    
    
    public FileReceiptDao(String file) throws Exception {
        // lue tietokannasta kuitit ja tee niistä kuittiolioita
    }
    
    private void save() throws Exception {
        // kirjoita receipts-olion kuitit tietokantaan
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
