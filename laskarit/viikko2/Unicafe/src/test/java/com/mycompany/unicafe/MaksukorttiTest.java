package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void konstruktoriAsettaaSaldonOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoa() {
        kortti.lataaRahaa(990);
        
        assertEquals("saldo: 10.0", kortti.toString());        
    }
    
    @Test
    public void saldoVaheneeJosRahaaTarpeeksi() {
        kortti.otaRahaa(5);
        
        assertEquals("saldo: 0.5", kortti.toString());
    }
    
    @Test
    public void saldoEiMuutuJosRahaaEiTarpeeksi() {
        kortti.otaRahaa(1000);
        
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void otaRahaaPalauttaaTrueJosSaldoRiittaa() {
        assertTrue(kortti.otaRahaa(5));
    }
    
    @Test
    public void otaRahaaPalauttaaFalseJosSaldoEiRiita() {
        assertFalse(kortti.otaRahaa(10000));
    }    
}
