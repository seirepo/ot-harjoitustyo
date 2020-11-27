package receiptapp;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import receiptapp.domain.ReceiptService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import receiptapp.domain.ReceiptItem;

/**
 * FXML Controller class
 *
 * @author resure
 */
public class ReceiptController implements Initializable {

    private ReceiptService receiptService;
    private ReceiptMain application;
    
    @FXML private TextField storeField;
    @FXML private DatePicker date;
    @FXML private Button cancelBtn;
    @FXML private Button okBtn;
    @FXML private TextField productField;
    @FXML private TextField priceField;
    @FXML private TextField qntyField;
    @FXML private ChoiceBox<String> unitChoice;
    @FXML private Button addProductBtn;
    @FXML private Label receiptTotal;
    @FXML private Button addReceiptBtn;
    @FXML private Button removeReceipt;
    @FXML private TableView<?> itemTable;
    
    private final Pattern doublePattern;
    
    
    public ReceiptController() {
        this.doublePattern = Pattern.compile("[0-9]*\\.[0-9]+|[0-9]+");
    }
    
    public void setApplication(ReceiptMain application) {
        this.application = application;
    }
    public void setReceiptService(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.unitChoice.getItems().add("pc");
        this.unitChoice.getItems().add("kg");
        this.unitChoice.getItems().add("l");
    }

    @FXML
    void handleAddProduct(ActionEvent event) {
        System.out.println("klikkasit add!");
    }

    @FXML
    void handleAddReceipt(ActionEvent event) {

    }

    @FXML
    void handleCancel(ActionEvent event) {
        System.out.println("klikkasit cancel!");
    }

    @FXML
    void handleOk(ActionEvent event) {
        System.out.println("klikkasit ok!");
    }

    @FXML
    void handleRemoveReceipt(ActionEvent event) {

    }
    
}
