package kuittitietokanta;

import kuittitietokanta.domain.*;
import java.util.ArrayList;
/**
 *
 * @author resure
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Product p1 = new Product("nuudeli", 0.89, 5, "pc");
        Product p2 = new Product("kaali", 1.35, 1.5, "kg");
        Product p3 = new Product("tofu", 1.95, 1, "pc");
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(p1);
        products.add(p2);
        products.add(p3);
        
        Receipt r = new Receipt("S-market", java.time.LocalDate.now(), products);
    }
    
}
