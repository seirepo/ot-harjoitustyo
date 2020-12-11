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
public class HelperFunctionsTest {
        
    @Before
    public void setUp() {
    }
    
//    @Test
//    public void centsToEurosReturnsRightWhenCentsLessThan100() {
//        assertEquals(0.75, HelperFunctions.centsToEuros(75), 0.001);
//    }
//    
//    @Test
//    public void centsToEurosReturnsRightWhenCentsMoreThan100() {
//        assertEquals(99.05, HelperFunctions.centsToEuros(9905), 0.001);
//    }
//    
//    @Test
//    public void centsToEurosReturnsRightWhenCentsLessThan0() {
//        assertEquals(-3.8, HelperFunctions.centsToEuros(-380), 0.001);
//    }
//    
//    @Test
//    public void gToKgRightWhenGLessThan1000() {
//        assertEquals(0.321, HelperFunctions.gToKg(321), 0.001);
//    }
//    
//    @Test
//    public void gToKgRightWhenGMoreThan1000() {
//        assertEquals(1.321, HelperFunctions.gToKg(1321), 0.001);
//    }
    
    @Test
    public void shiftDoubletShiftsDoubleToLeft() {
        assertEquals(1321000, HelperFunctions.shiftDouble(1321, 3), 0.001);
        assertEquals(-2300000, HelperFunctions.shiftDouble(-23, 5), 0.001);
        assertEquals(1234.5678, HelperFunctions.shiftDouble(0.12345678, 4), 0.001);
        assertEquals(22.4567, HelperFunctions.shiftDouble(2.24567, 1), 0.001);
        assertEquals(1.5, HelperFunctions.shiftDouble(1.5, 0), 0.001);
    }
    
    @Test
    public void shiftDoubleShiftsDoubleToRight() {
        assertEquals(1.321, HelperFunctions.shiftDouble(1321, -3), 0.001);
        assertEquals(-0.00023, HelperFunctions.shiftDouble(-23, -5), 0.001);
        assertEquals(99.05, HelperFunctions.shiftDouble(9905, -2), 0.001);
        assertEquals(123.456, HelperFunctions.shiftDouble(12345.6, -2), 0.01);
        assertEquals(1.5, HelperFunctions.shiftDouble(1.5, 0), 0.001);
    }
}