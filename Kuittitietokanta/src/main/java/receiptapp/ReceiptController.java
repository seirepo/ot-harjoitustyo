package receiptapp;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableView<ReceiptItem> itemTable;    
    @FXML private TableColumn<ReceiptItem, String> productCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
    @FXML private TableColumn<ReceiptItem, Double> qntyCol;
    @FXML private TableColumn<ReceiptItem, String> unitCol;
    
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
        // TODO: selvitä yksiköt receiptServiceltä
        // ongelma: receiptServiceä ei ole alustettu vielä tässä, eikä
        // fxml-elementtejä ole alustettu vielä konstruktorissa.
        // ts. fxml-elementit alustetaan ennen setReceiptServicen kutsumista
        // ArrayList<String> units = this.receiptService.getUnits();
        this.unitChoice.getItems().add("pc");
        this.unitChoice.getItems().add("kg");
        this.unitChoice.getItems().add("l");
        this.unitChoice.setValue("pc");
        
        this.productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        this.priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.qntyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        this.unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));

    }

    @FXML
    void handleAddItem(ActionEvent event) {        
        System.out.println("klikkasit add!");
        
        String error = checkAddFields();
        
        if (error.length() > 0) {
            System.out.println(error);
        } else {
        
            String product = this.productField.getText(); // jos tyhjä -> ""
            double price = Double.parseDouble(this.priceField.getText());
            String unit = this.unitChoice.getValue();
            double qnty = Double.parseDouble(this.qntyField.getText());

//            if (unit.equals("pc")) {
//                int qnty = Integer.parseInt(this.qntyField.getText());
//            } else {
//                int qnty = (int)(Double.parseDouble(this.qntyField.getText()) * 1000);
//            }

            if (unit.equals("pc")) {
                System.out.println("syöte:\n\t" + product + "\n\t" + price
                    + "\n\t" + (int)(qnty*1000) + "\n\t" + unit);
            } else {            
                System.out.println("syöte:\n\t" + product + "\n\t" + price
                        + "\n\t" + qnty + "\n\t" + unit);
            }
            //this.itemTable.getItems().add(new ReceiptItem(product, price, qnty, unit));
            
            //boolean added = this.receiptService.addReceiptItem();
        }
    }
    
    public String checkAddFields() {
        String e = "";
        
        if ("".equals(this.productField.getText())) {
            e += "product name cannot be blank\n";
        }
        
        if (!this.doublePattern.matcher(this.priceField.getText()).matches()) {
            e += "price must be a number\n";
        }
        
        if (!this.doublePattern.matcher(this.qntyField.getText()).matches()) {
            e += "quantity must be a number\n";
        }
        
        return e;
    }

    
    @FXML
    void HandleCheckDouble(KeyEvent event) {
        System.out.println(event.getCharacter());
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
