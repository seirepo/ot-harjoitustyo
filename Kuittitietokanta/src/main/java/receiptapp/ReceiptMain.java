package receiptapp;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import receiptapp.domain.ReceiptService;
/**
 *
 * @author resure
 */
public class ReceiptMain extends Application {

    private Stage stage;
    private ReceiptService receiptService;
    private Scene scene;
    
    @Override
    public void init() throws Exception {
        receiptService = new ReceiptService();
        
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/fxml/FxReceipt.fxml"));
        Parent mainPane = loader.load();
        ReceiptController receiptController = loader.getController();
        receiptController.setReceiptService(receiptService); 
        receiptController.setApplication(this);
        scene = new Scene(mainPane);
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        stage.setTitle("pls work");
        stage.setScene(scene);
        stage.show();
    }    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
//        ReceiptItem item1 = new ReceiptItem("tofu", 1.95, 1, "pc");
//        System.out.println("hinta: " + item1.getPrice());
//        System.out.println(item1.getItem());
    }
    
}
