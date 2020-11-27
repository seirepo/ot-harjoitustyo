/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiptapp.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author resure
 */
public class ReceiptItemTest {
    ReceiptItem item;
    
    @Before
    public void setUp() {
        item = new ReceiptItem("name", 14.75, 5, "pc");
    }
    
    @Test
    public void constructor1SetsThePriceRight() {
        assertEquals(1475, item.getPrice());
    }
    
    @Test
    public void getUnitPriceReturnsCorrectUnitPrice() {
        assertEquals(295, item.getUnitPrice());
    }
    
    @Test
    public void getUnitPriceReturnsCorrectlyWhenQntyNotInteger() {
        item = new ReceiptItem("name", 14.75, 5.5, "kg");
        assertEquals(268, item.getUnitPrice());
    }
    
    @Test
    public void constructorSetsNegativePriceToZero() {
        item = new ReceiptItem("name", -10.5, 1, "pc");
        assertEquals(0, item.getPrice());
    }
    
    @Test
    public void constructorSetsUnknownUnitToPc() {
        item = new ReceiptItem("name", 1, 1, "asd");
        assertEquals("pc", item.getUnit());
    }
    
    @Test
    public void constructorSetsQuantityToOneIfLess() {
        item = new ReceiptItem("name", 1, -1, "pc");
        assertEquals(1, item.getQuantity(), 0.001);
    }
//    
//    @Test
//    public void constructor2SplitsTheGivenProductRight() {
//        item = new ReceiptItem("name;14.75;5;pc");
//        assertEquals("name", item.getProduct());
//    }
//    
//    @Test
//    public void constructor2SplitsTheGivenPriceRight() {
//        item = new ReceiptItem("name;14.75;5;pc");
//        assertEquals(1475, item.getPrice());
//    }
//    
//    @Test
//    public void constructor2SplitsTheGivenQuantityRight() {
//        item = new ReceiptItem("name;14.75;5;pc");
//        assertEquals(5, item.getQuantity());
//    }
//    
//    @Test
//    public void constructor2SplitsTheGivenUnitRight() {
//        item = new ReceiptItem("name;14.75;5;pc");
//        assertEquals("pc", item.getUnit());
//    }
}
