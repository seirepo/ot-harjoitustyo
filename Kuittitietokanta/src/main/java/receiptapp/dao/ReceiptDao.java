package receiptapp.dao;

import java.util.List;
import receiptapp.domain.Receipt;

/**
 *
 * @author resure
 */
public interface ReceiptDao {
    
    Receipt create(Receipt receipt) throws Exception;
    
    List<Receipt> getAll();
}
