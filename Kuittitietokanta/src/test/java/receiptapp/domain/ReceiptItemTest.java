/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiptapp.domain;

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
public class ReceiptItemTest {
    ReceiptItem item;
    
    @Before
    public void setUp() {
        item = new ReceiptItem("name", 14.75, 5, "pc");
    }
    
    @Test
    public void constructorSetsThePriceRight() {
        assertEquals(1475, item.getPrice());
    }
    
    @Test
    public void getUnitPriceReturnsCorrectUnitPrice() {
        assertEquals(295, item.getUnitPrice());
    }
}
