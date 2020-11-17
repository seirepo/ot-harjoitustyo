package kuittitietokanta.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.HashMap;

/**
 *
 * @author resure
 */
public class receiptTest {
    
    Receipt receipt;
    HashMap<Product, Integer> products;
    Product p1, p2, p3;
    
    @Before
    public void setUp() {
        LocalDate date = LocalDate.of(2020, 10, 10);
        p1 = new Product("a", 0.5);
        p2 = new Product("b", 1.99);
        p3 = new Product("c", 1.45);
        products = new HashMap<>();
        receipt = new Receipt("x", date, products);
    }

     @Test
     public void totalReturnsTheSumRight1() {
         products.put(p1, 5); products.put(p2, 3); products.put(p3, 1);
         assertEquals(992, receipt.total());
     }   
}
