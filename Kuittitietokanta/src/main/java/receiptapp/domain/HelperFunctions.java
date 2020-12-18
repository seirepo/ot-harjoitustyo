package receiptapp.domain;

/**
 * Luokka avustajafunktioille.
 * @author resure
 */
public class HelperFunctions {
    
    /**
     * Funktio joka mahdollistaa yksikkömuunnokset liu'uttamalla doublea.
     * Lukua liikutetaan desimaalipisteen suhteen joko oikealle antamalla
     * negatiivisen siirron ja muutoin vasemmalle.
     * @param value liu'utettava luku
     * @param shift siirron määrä
     * @return luku liu'utettuna
     */
    public static double shiftDouble(double value, int shift) {
        return value * Math.pow(10, shift);
    }
}
