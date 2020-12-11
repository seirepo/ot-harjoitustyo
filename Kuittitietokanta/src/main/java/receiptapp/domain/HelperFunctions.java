package receiptapp.domain;

/**
 * Luokka avustajafunktioille.
 * @author resure
 */
public class HelperFunctions {
        
//    /**
//     * Muuntaa annetut sentit euroiksi kahden desimaalin tarkkuudelle.
//     * @param cents sentit
//     * @return eurot doublena
//     */
//    public static double centsToEuros(int cents) {
//        return cents / 100 + ((cents % 100) * 1.0 / 100);
//    }
//    
//    /**
//     * Muuntaa annetut grammat kilogrammoiksi kolmen desimaalin tarkkuudelle.
//     * @param grams grammat
//     * @return kilogrammat doublena
//     */
//    public static double gToKg(int grams) {
//        return (grams / 1000) + ((grams % 1000) * 1.0 / 1000);
//    }
//    
//    /**
//     * Muuntaa annetut millilitrat litroiksi kolmen desimaalin tarkkuudelle
//     * käyttäen metodia gToKg().
//     * @param ml millilitrat
//     * @return litrat doublena
//     */
//    public static double mlToL(int ml) {
//        return gToKg(ml);
//    }
    
    /**
     * Funktio joka mahdollistaa yksikkömuunnokset liu'uttamalla doublea.
     * Lukua liikutetaan desimaalipisteen suhteen joko oikealle antamalla
     * negatiivisen siirron ja muutoin vasemmalle.
     * @param value liu'utettava luku
     * @param shift siirron määrä
     * @return 
     */
    public static double shiftDouble(double value, int shift) {
        return value * Math.pow(10, shift);
    }
}
