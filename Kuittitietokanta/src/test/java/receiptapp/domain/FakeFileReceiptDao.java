package receiptapp.domain;
import java.util.ArrayList;
import java.util.List;
import receiptapp.dao.ReceiptDao;

/**
 *
 * @author resure
 */
public class FakeFileReceiptDao implements ReceiptDao {
    
    public List<Receipt> receipts;
    
    public FakeFileReceiptDao() {
        this.receipts = new ArrayList<>();
    }
    
    @Override
    public List<Receipt> getAll() {
        return new ArrayList<>();
    }
    
    @Override
    public Receipt create(Receipt receipt) {
        return null;
    }
}