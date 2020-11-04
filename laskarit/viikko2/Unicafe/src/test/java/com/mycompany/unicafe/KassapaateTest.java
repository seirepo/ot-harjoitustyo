package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti1;
    Maksukortti kortti2;
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti1 = new Maksukortti(1000);
        kortti2 = new Maksukortti(200);
    }
    
     @Test
     public void konstruktoriAsettaaRahamaaranAlussaOikein() {
         assertEquals(100000, kassa.kassassaRahaa());
     }
     
     @Test
     public void edullisiaLounaitaMyytyAlussaNolla() {
         assertEquals(0, kassa.edullisiaLounaitaMyyty());
     }
     
     @Test
     public void maukkaitaLounaitaMyytyAlussaNolla() {
         assertEquals(0, kassa.maukkaitaLounaitaMyyty());
     }
     
     @Test
     public void onnistunutEdullisenLounaanKateisostoKasvattaaRahamaaraa() {
         kassa.syoEdullisesti(240);
         assertEquals(100240, kassa.kassassaRahaa());
     }
     
     @Test
     public void onnistuneenEdullisenLounaanKateisostostaOikeaMaaraVaihtorahaa() {
         assertEquals(260, kassa.syoEdullisesti(500));
     }
     
     @Test
     public void onnistunutMaukkaanLounaanKateisostoKasvattaaRahamaaraa() {
         kassa.syoMaukkaasti(600);
         assertEquals(100400, kassa.kassassaRahaa());
     }
     
     @Test
     public void onnistuneenMaukkaanLounaanKateisostostaOikeaMaaraVaihtorahaa() {
         assertEquals(600, kassa.syoMaukkaasti(1000));
     }
     
     @Test
     public void onnistunutEdullisenLounaanKateisostoKasvattaaMyytyjaLounaita() {
         kassa.syoEdullisesti(500);
         assertEquals(1, kassa.edullisiaLounaitaMyyty());
     }
     
     @Test
     public void onnistunutMaukkaanLounaanKateisostoKasvattaaMyytyjaLounaita() {
         kassa.syoMaukkaasti(500);
         assertEquals(1, kassa.maukkaitaLounaitaMyyty());
     }
     
     @Test
     public void epaonnistunutEdullisenLounaanKateisostoEiMuutaRahamaaraa() {
         kassa.syoEdullisesti(100);
         assertEquals(100000, kassa.kassassaRahaa());
     }
     
     @Test
     public void epaonnistunutEdullisenLounaanKateisostoPalautuuVaihtorahana() {
         assertEquals(100, kassa.syoEdullisesti(100));
     }
     
     @Test
     public void epaonnistunutMaukkaanLounaanKateisostoEiMuutaRahamaaraa() {
         kassa.syoMaukkaasti(350);
         assertEquals(100000, kassa.kassassaRahaa());
     }
     
     @Test
     public void epaonnistunutMaukkaanLounaanKateisostoPalautuuVaihtorahana() {
         assertEquals(350, kassa.syoMaukkaasti(350));
     }
     
     @Test
     public void onnistunutEdullisenLounaanKorttiostoPalauttaaTrue() {
         assertTrue(kassa.syoEdullisesti(kortti1));
     }
     
     @Test
     public void onnistunutEdullisenLounaanKorttiostoKasvattaaMyytyjaEdullisiaLounaita() {
         kassa.syoEdullisesti(kortti1);
         assertEquals(1, kassa.edullisiaLounaitaMyyty());
     }
     
     @Test
     public void onnistunutMaukkaanLounaanKorttiostoPalauttaaTrue() {
         assertTrue(kassa.syoMaukkaasti(kortti1));
     }
     
     @Test
     public void onnistunutEdullisenLounaanKorttiostoKasvattaaMyytyjaMaukkaitaLounaita() {
         kassa.syoMaukkaasti(kortti1);
         assertEquals(1, kassa.maukkaitaLounaitaMyyty());
     }
     
     @Test
     public void epaonnistunutEdullisenLounaanKorttiostoPalauttaaFalse() {
         assertFalse(kassa.syoEdullisesti(kortti2));
     }
     
     @Test
     public void epaonnistunutEdullisenLounaanKorttiostoEiKasvataMyytyjaEdullisiaLounaita() {
         kassa.syoEdullisesti(kortti2);
         assertEquals(0, kassa.edullisiaLounaitaMyyty());
     }
     
     @Test
     public void epaonnistunutMaukkaanLounaanKorttiostoPalauttaaFalse() {
         assertFalse(kassa.syoMaukkaasti(kortti2));
     }
     
     @Test
     public void epaonnistunutMaukkaanLounaanKorttiostoEiKasvataMyytyjaMaukkaitaLounaita() {
         kassa.syoMaukkaasti(kortti2);
         assertEquals(0, kassa.maukkaitaLounaitaMyyty());
     }
     
     @Test
     public void kassassaOlevaRahamaaraEiMuutuEdullisenLounaanKorttiostossa() {
         kassa.syoEdullisesti(kortti1);
         assertEquals(100000, kassa.kassassaRahaa());
     }
          
     @Test
     public void kassassaOlevaRahamaaraEiMuutuMaukkaanLounaanKorttiostossa() {
         kassa.syoMaukkaasti(kortti1);
         assertEquals(100000, kassa.kassassaRahaa());
     }
     
     @Test
     public void kassanRahamaaraKasvaaKunKortilleLadataanPositiivinenSumma() {
         kassa.lataaRahaaKortille(kortti2, 600);
         assertEquals(100600, kassa.kassassaRahaa());
     }
     
     @Test
     public void kassanRahamaaraEiMuutuKunKortilleLadataanNegatiivinenSumma() {
         kassa.lataaRahaaKortille(kortti2, -100);
         assertEquals(100000, kassa.kassassaRahaa());
     }
}
