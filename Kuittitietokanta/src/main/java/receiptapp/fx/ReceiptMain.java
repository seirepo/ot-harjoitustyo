package receiptapp.fx;

import receiptapp.fx.ReceiptController;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import receiptapp.domain.ReceiptService;

/**
 * Luokka varsinaiselle ohjelman käynnistämiselle.
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
     * Main, joka oikeasti käynnistää sovelluksen.
     * @param args komentoriviargumentit
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
