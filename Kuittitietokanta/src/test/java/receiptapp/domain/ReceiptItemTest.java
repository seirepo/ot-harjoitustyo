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
    public void settingIsUnitPriceFalseAffectsUnitPrice() {
        item = new ReceiptItem("name", 14.75, true, 5, "pc");
        assertEquals(14.75, item.getUnitPrice(), 0.01);
        item.setIsUnitPrice(false);
        assertEquals(2.95, item.getUnitPrice(), 0.01);
        assertEquals(14.75, item.getTotalPrice(), 0.01);
    }
    
    @Test
    public void quantityCannotBeSetZeroOrLess() {
        item.setQuantity(-4);
        assertEquals(5, item.getQuantity(), 0.01);
    }
    
    @Test
    public void setUnitDoesNotChangeUnitIfUnitIsNotLegal() {
        item.setUnit("dkg");
        assertEquals("pc", item.getUnit());
    }
        
    @Test
    public void quantityRoundsToThreeDecimals() {
        item.setQuantity(0.56785345);
        assertEquals(0.568, item.getQuantity(), 0.001);
        item.setQuantity(0.56742);
        assertEquals(0.567, item.getQuantity(), 0.001);
    }
    
    @Test
    public void quantityIsSetToMinimumIfItRoundsToZero() {
        item.setQuantity(0.000000001);
        assertEquals(0.001, item.getQuantity(), 0.001);
    }
    
    @Test
    public void constructorSetsQuantityAndPriceToMinimumIfItRoundsToZero() {
        item = new ReceiptItem("name", 0.014, true, 0.00002, "kg");
        assertEquals(1, item.getTotalPriceCents());
        assertEquals(0.01, item.getTotalPrice(), 0.01);
        assertEquals(0.001, item.getQuantity(), 0.001);
        item = new ReceiptItem("name", 0.00001241, false, 5, "pc");
        assertEquals(0.01, item.getTotalPrice(), 0.001);
        assertEquals(0.001, item.getUnitPrice(), 0.001);
    }
    
    @Test
    public void updatePropertiesUpdatesProperties() {
        item.updateProperties("new name", 5, false, 100, "kg");
        assertEquals("new name", item.getProduct());
        assertEquals(5, item.getTotalPrice(), 0.01);
        assertEquals(false, item.getIsUnitPrice());
        assertEquals(100, item.getQuantity(), 0.001);
        assertEquals("kg", item.getUnit());
    }
    
    @Test
    public void updatePropertiesUpdatesProperties1() {
        item.updateProperties("new name", 5, true, 100, "kg");
        assertEquals("new name", item.getProduct());
        assertEquals(500, item.getTotalPrice(), 0.01);
        assertEquals(true, item.getIsUnitPrice());
        assertEquals(100, item.getQuantity(), 0.001);
        assertEquals("kg", item.getUnit());
    }

}