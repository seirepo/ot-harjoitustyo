package receiptapp.domain;

/**
 * Kuitti yhdelle kuitilla olevalle riville
 * @author resure
 */
public class ReceiptItem {
    private String product;
    private int price; // kokonaishinta: tästä lasketaan yksikköhinta
    private int quantity;
    private final String[] UNITS = {"pc", "kg", "l"};
    private String unit;
    
    public ReceiptItem(String product, double price, int quantity, String unit) {
        this.product = product;
        this.price = (int) (price * 100);
        this.quantity = quantity;
        this.unit = unit;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getUnitPrice() {
        return this.price / quantity;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    @Override
    public String toString() {
        //double priceInEuros = this.price / 100 + this.price % 100;
        String s = String.format("%-12s\t%-3.2f\t%-3d\t%-2s\t%-2.2fe / %-2s",
                this.product, centsToEuros(this.price), this.quantity, this.unit,
                centsToEuros(getUnitPrice()), this.unit);
        return s;
    }
    
    /**
     * Muuntaa annetut sentit euroiksi kahden desimaalin tarkkuudelle
     * @param cents sentit
     * @return eurot doublena
     */
    public double centsToEuros(int cents) {
        return cents / 100 + ((cents % 100) * 1.0 / 100);
    }
    
    /**
     * Muuntaa annetut grammat kilogrammoiksi kolmen desimaalin tarkkuudelle
     * @param grams grammat
     * @return kilogrammat doublena
     */
    public double gToKg(int grams) {
        return (grams / 1000) + ((grams % 1000) * 1.0 / 1000);
    }
    
    /**
     * Muuntaa annetut millilitrat litroiksi kolmen desimaalin tarkkuudelle
     * käyttäen metodia gToKg()
     * @param ml millilitrat
     * @return litrat doublena
     */
    public double mlToL(int ml) {
        return gToKg(ml);
    }
}
