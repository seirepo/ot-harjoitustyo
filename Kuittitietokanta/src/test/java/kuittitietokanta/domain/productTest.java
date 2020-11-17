package kuittitietokanta.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author resure
 */
public class productTest {

    Product p;
    
    @Before
    public void setUp() {
    }

    @Test
    public void constructorSetsThePriceRight1() {
        p = new Product("a", 0.345);
        assertEquals(34, p.getPrice());
    }
    
    @Test
    public void constructorSetsThePriceRight2() {
        p = new Product("b", 1.42);
        assertEquals(142, p.getPrice());
    }
}
