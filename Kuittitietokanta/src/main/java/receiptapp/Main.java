package receiptapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import receiptapp.domain.ReceiptItem;
import receiptapp.domain.ReceiptService;
/**
 *
 * @author resure
 */
public class Main extends Application {

//    private Stage stage;
//    private ReceiptService receiptService;
//    private Scene scene;
    
    @Override
    public void start(Stage primaryStage) {
        // ei toimi: Location is not set, getResourcen parametrissä on tod. näk. jotain vikaa
//        try {
//            FXMLLoader ldr = new FXMLLoader(getClass().getResource("/fxml/fxReceipt.fxml"));
//            Pane root = (Pane)ldr.load();
//            ReceiptController receiptCntrl = (ReceiptController)ldr.getController();
//            Scene scene = new Scene(root);
//            
//            scene.getStylesheets().add(getClass().getResource("fxreceipt.css").toExternalForm());
//            primaryStage.setScene(scene);
//            primaryStage.setTitle("very cool");
//            
//            
//            primaryStage.show();
//            
//        } catch(Exception e) {
//            e.printStackTrace();
//            System.err.println("ReceiptMain: " + e.getMessage());
//        }
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //launch(args);
        
        ReceiptItem item1 = new ReceiptItem("tofu", 1.95, 1, "pc");
        System.out.println("hinta: " + item1.getPrice());
        System.out.println(item1.getItem());

    }
    
}
