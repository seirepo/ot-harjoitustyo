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
    public void constructorSetsThePriceRight() {
        assertEquals(1475, item.getPriceCents(), 0.01);
    }
    
    @Test
    public void getPriceConvertsPriceToEuros() {
        assertEquals(14.75, item.getPrice(), 0.01);
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
        assertEquals(0, item.getPrice(), 0.01);
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
    
    @Test
    public void idIsNegativeOneWhenNotSet() {
        assertEquals(-1, item.getId());
    }
    
    @Test
    public void idCanBeSetAndGetsSetRight() {
        item.setId(10);
        assertEquals(10, item.getId());
    }
}
