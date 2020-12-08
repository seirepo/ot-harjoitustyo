package receiptapp;

import receiptapp.fx.ReceiptMain;

public class Main {
    
    /**
     * main-metodi sovelluksen käynnistykseen luokassa, joka ei peri
     * Application-luokkaa. Tässä kutsutaan kuitenkin vain
     * "oikean" Mainin main-metodia
     * @param args 
     */
    public static void main(String[] args) {
        ReceiptMain.main(args);
    }
}
