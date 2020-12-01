package receiptapp.dao;

import java.util.List;
import receiptapp.domain.Receipt;

/**
 * Rajapinta tietokantaluokille.
 * @author resure
 */
public interface ReceiptDao {
    
    /**
     * Abstrakti metodi uuden kuitin luomiselle ja lisäämiselle tietokantaan.
     * @param receipt lisättävä kuitti
     * @return lisätty kuitti tai null jos lisäys ei onnistu
     * @throws Exception 
     */
    Receipt create(Receipt receipt) throws Exception;
    
    /**
     * Abstrakti metodi, jolla on tarkoitus palauttaa kaikki tietokannassa olevat kuitit.
     * @return kaikki kuitit
     */
    List<Receipt> getAll();
}
