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
        item = new ReceiptItem("name", 14.75, false, 5, "pc");
    }
    
    @Test
    public void constructorSetsThePriceRight() {
        assertEquals(1475, item.getTotalPriceCents(), 0.01);
    }
    
    @Test
    public void getPriceConvertsPriceToEuros() {
        assertEquals(14.75, item.getTotalPrice(), 0.01);
    }
    
    @Test
    public void getUnitPriceReturnsCorrectUnitPrice() {
        assertEquals(2.95, item.getUnitPrice(), 0.01);
    }
    
    @Test
    public void getUnitPriceReturnsCorrectPriceWhenQntyNotInteger() {
        item = new ReceiptItem("name", 14.75, false, 5.5, "kg");
        assertEquals(2.68, item.getUnitPrice(), 0.01);
    }
    
    @Test
    public void getUnitPriceReturnsCorrectPriceWhenIsUnitPriceIsFalse() {
        assertEquals(2.95, item.getUnitPrice(), 0.01);
    }
    
    @Test
    public void getUnitPriceReturnsCorrectPriceWhenIsUnitPriceIsTrue() {
        item = new ReceiptItem("name", 14.75, true, 5, "pc");
        assertEquals(73.75, item.getTotalPrice(), 0.01);
    }
    
    @Test
    public void constructorSetsNegativePriceToZero() {
        item = new ReceiptItem("name", -10.5, true, 1, "pc");
        assertEquals(0, item.getTotalPrice(), 0.01);
    }
    
    @Test
    public void constructorSetsUnknownUnitToPc() {
        item = new ReceiptItem("name", 1, true, 1, "asd");
        assertEquals("pc", item.getUnit());
    }
    
    @Test
    public void constructorSetsQuantityToOneIfLess() {
        item = new ReceiptItem("name", 1, true, -1, "pc");
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
    
    @Test
    public void priceCanBeSetUsingSetPrice() {
        item.setTotalPrice(10.50);
        assertEquals(1050, item.getTotalPriceCents());
    }
    
    @Test
    public void priceCannotBeSetZeroOrLess() {
        item.setTotalPrice(-1.5);
        assertEquals(1475, item.getTotalPriceCents());
    }
    
    @Test
    public void priceTooCloseToZeroRoundsToOneCent() {
        item = new ReceiptItem("name", 0.000001, false, 1, "pc");
        assertEquals(1, item.getTotalPriceCents());
    }
    
    @Test
    public void priceSetTooCloseToZeroRoundsToOneCent() {
        item.setTotalPrice(0.000005);
        assertEquals(1, item.getTotalPriceCents());
    }
    
    @Test
    public void settingIsUnitPriceTrueAffectsTotalPrice() {
        item.setIsUnitPrice(true);
        assertEquals(73.75, item.getTotalPrice(), 0.01);
    }
    
    @Test
    public void settingIsUnitPriceFalseAffectsTotalPrice() {
        item = new ReceiptItem("name", 14.75, true, 5, "pc");
        assertEquals(73.75, item.getTotalPrice(), 0.01);
        item.setIsUnitPrice(false);
        assertEquals(14.75, item.getTotalPrice(), 0.01);
    }
    
    @Test
    public void settingIsUnitPriceTrueAffectsUnitPrice() {
        item.setIsUnitPrice(true);
        assertEquals(14.75, item.getUnitPrice(), 0.01);
    }
    
    @Test
    public void settingIsUnitPriceFalseAffectsUnitPricec() {
        item = new ReceiptItem("name", 14.75, true, 5, "pc");
        assertEquals(14.75, item.getUnitPrice(), 0.01);
        item.setIsUnitPrice(false);
        assertEquals(2.95, item.getUnitPrice(), 0.01);
    }
    
    
    @Test
    public void quantityCannotBeSetZeroOrLess() {
        item.setQuantity(-4);
        assertEquals(5, item.getQuantity(), 0.01);
    }
    
    @Test
    public void unitCannotBeSetOtherThanAllowed() {
        item.setUnit("hp");
        assertEquals("pc", item.getUnit());
    }
    
    @Test
    public void setUnitDoesNotChangeUnitIfUnitIsNotLegal() {
        item.setUnit("dkg");
        assertEquals("pc", item.getUnit());
    }
}
