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
    
    @Test
    public void centsToEurosReturnsRightWhenCentsLessThan100() {
        assertEquals(0.75, HelperFunctions.centsToEuros(75), 0.001);
    }
    
    @Test
    public void centsToEurosReturnsRightWhenCentsMoreThan100() {
        assertEquals(99.05, HelperFunctions.centsToEuros(9905), 0.001);
    }
    
    @Test
    public void centsToEurosReturnsRightWhenCentsLessThan0() {
        assertEquals(-3.8, HelperFunctions.centsToEuros(-380), 0.001);
    }
    
    @Test
    public void gToKgRightWhenGLessThan1000() {
        assertEquals(0.321, HelperFunctions.gToKg(321), 0.001);
    }
    
    @Test
    public void gToKgRightWhenGMoreThan1000() {
        assertEquals(1.321, HelperFunctions.gToKg(1321), 0.001);
    }    
}