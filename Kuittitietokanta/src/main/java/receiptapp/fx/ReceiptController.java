package receiptapp.fx;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import receiptapp.domain.ReceiptService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import receiptapp.domain.ReceiptItem;
import receiptapp.domain.Receipt;

/**
 * FXML Controller class
 *
 * @author resure
 */
public class ReceiptController implements Initializable {

    private ReceiptService receiptService;
    private ReceiptMain application;
    
    @FXML private TextField storeField;
    @FXML private TextField productField;
    @FXML private TextField priceField;
    @FXML private TextField qntyField;
    @FXML private Label receiptTotal;
    @FXML private DatePicker date;
    @FXML private ChoiceBox<String> unitChoice;
    @FXML private Button cancelBtn;
    @FXML private Button okBtn;
    @FXML private Button addProductBtn;
    @FXML private Button addReceiptBtn;
    @FXML private Button removeReceipt;
    @FXML private Button editItemBtn;
    @FXML private Button deleteItemBtn;
    @FXML private Button addOrSaveReceiptBtn;
    @FXML private Button cancelEditBtn;
    @FXML private CheckBox unitPriceCheck;

    
    @FXML private TableView<ReceiptItem> itemTable;
    @FXML private TableColumn<ReceiptItem, String> productCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
    @FXML private TableColumn<ReceiptItem, Double> unitPriceCol;
    @FXML private TableColumn<ReceiptItem, Double> qntyCol;
    @FXML private TableColumn<ReceiptItem, String> unitCol;
    
    @FXML private TableView<Receipt> receiptTable;
    @FXML private TableColumn<Receipt, String> storeCol;
    @FXML private TableColumn<Receipt, LocalDate> dateCol;
    @FXML private TableColumn<Receipt, Integer> productsCol;
    @FXML private TableColumn<Receipt, Double> totalCol;

    
    private final Pattern doublePattern;
    private ReceiptItem selectedItem;
    private Receipt selectedReceipt;
    
    
    public ReceiptController() {
        this.doublePattern = Pattern.compile("[0-9]*\\.[0-9]+|[0-9]+");
    }
    
    public void setApplication(ReceiptMain application) {
        this.application = application;
    }
    public void setReceiptService(ReceiptService receiptService) {
        this.receiptService = receiptService;
        
        this.itemTable.setItems(this.receiptService.getReceiptItems());
        this.receiptTable.setItems(this.receiptService.getReceipts());
        
        List<String> units = this.receiptService.getUnits();
        for (var unit : units) {
            this.unitChoice.getItems().add("" + unit);
        }
        this.unitChoice.setValue(units.get(0));
    }
    
    /**
     * Alustetaan kaikki fxml-jutut ja tehdään tarvittavat asetukset.
     * TODO: jaa tämäkin erillisiin metodeihin jos mahdollista, eli esim.
     * initializeChoseBox(), initializeItemTable(), initializeReceiptTable()
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        this.productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        this.priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        this.unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        this.qntyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        this.unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));        
        this.itemTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) 
                        -> editItem(newSelection));
        
        this.storeCol.setCellValueFactory(new PropertyValueFactory<>("store"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.productsCol.setCellValueFactory(new PropertyValueFactory<>("productCount"));
        this.totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        this.receiptTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) ->
                              editReceipt(newSelection));
        
        //TODO: this.receiptTable.setItems(receiptService:n receiptit), ja sama itemeille
        // VAIN YHDEN KERRAN. tee testi: nappi joka lisää random itemin listaan ja katso päivittyykö tableview  
    }


    @FXML
    void handleAddItem(ActionEvent event) {
        addOrSaveItem();
    }

    @FXML
    void handleCancelEdit(ActionEvent event) {
        cancelEditing();
    }
    
    @FXML
    void handleEditItem(ActionEvent event) {
        System.out.println("receiptapp.fx.ReceiptController.handleEditItem(): " 
        + "nyt voi muokata");
        
        this.productField.setText(this.selectedItem.getProduct());
        this.priceField.setText("" + this.selectedItem.getTotalPrice());
        this.qntyField.setText("" + this.selectedItem.getQuantity());
        this.unitChoice.setValue(this.selectedItem.getUnit());
        int selectedId = this.selectedItem.getId();
        
    }
    
    @FXML
    void handleDeleteItem(ActionEvent event) {
        deleteItem();
    }    
    
    @FXML
    void handleAddOrSaveReceipt(ActionEvent event) {
        addOrSaveReceipt();
    }
    
    @FXML
    void handleRemoveReceipt(ActionEvent event) {

    }
    
    @FXML
    void HandleCheckDouble(KeyEvent event) {
        //System.out.println(event.getCharacter());
    }
    
    @FXML
    void handleAddReceipt(ActionEvent event) {
        
    }

    @FXML
    void handleCancel(ActionEvent event) {
        
    }

    /**
     * Lisätään uusi tuote itemTableen. Tarkistetaan ensin onko vaaditut kentät
     * täytetty oikein.
     * TODO: error-dialogi jos ei
     */
    public void addOrSaveItem() {
        String error = checkAddItemFields();
        
        if (error.length() > 0) {
            errorDialog(error);
            System.out.println(error);
            return;
        }
        
        String product = this.productField.getText();
        double price = Double.parseDouble(this.priceField.getText());
        boolean isUnitPrice = this.unitPriceCheck.isSelected();
        double qnty = Double.parseDouble(this.qntyField.getText());
        String unit = this.unitChoice.getValue();
        
        ReceiptItem selected = this.itemTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {            
            ReceiptItem i = new ReceiptItem(product, price, isUnitPrice, qnty, unit);
            this.receiptService.addReceiptItem(i);
        } else {
            this.receiptService.updateItem(selected, product, price, isUnitPrice,
                    qnty, unit);
        }
        this.itemTable.refresh();
        this.itemTable.getSelectionModel().clearSelection();
        clearAddFields();
        updateTotal();        
    }
    
    public void editItem(ReceiptItem item) {
        
        if (item == null) return;
        
        System.out.println("receiptapp.fx.ReceiptController.editItem(): "
                + item);
        System.out.println("receiptapp.fx.ReceiptController.editItem(): "
                + this.itemTable.getSelectionModel().getSelectedItem());
        
        this.productField.setText(item.getProduct());
        this.priceField.setText("" + item.getTotalPrice());
        this.unitPriceCheck.setSelected(item.getIsUnitPrice());
        this.qntyField.setText("" + item.getQuantity());
        this.unitChoice.setValue(item.getUnit());
    }
    
    public void cancelEditing() {
        this.itemTable.getSelectionModel().clearSelection();
        clearAddFields();
    }
    
    public void deleteItem() {
        ReceiptItem selected = this.itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorDialog("No item selected ¯\\_(ツ)_/¯");
            return;
        }
        
        this.receiptService.deleteItem(selected);
        this.itemTable.getSelectionModel().clearSelection();
        clearAddFields();
        updateTotal();
        this.itemTable.refresh();
    }
    
    /**
     * Päivitetään kuitin loppusumma.
     */
    public void updateTotal() {
        String total = "" + this.receiptService.getTotal();
        this.receiptTotal.setText(total);
    }
    
    /**
     * Tyhjennetään kaikki uuden tuotteen lisäämiseen liittyvät kentät.
     */
    public void clearAddFields() {
        this.productField.setText("");
        this.priceField.setText("");
        this.unitPriceCheck.setSelected(false);
        this.qntyField.setText("");
    }
    
    /**
     * Lisätään uusi kuitti. Samalla tarkistetaan onko kaikki lisäämiseen
     * vaadittavat kentät täytetty oikein.
     */    
    public void addOrSaveReceipt() {
        String error = checkAddReceiptFields();
        
        if (error.length() > 0) {
            errorDialog(error);
            System.out.println(error);
            return;
        }
        
        Receipt selected = this.receiptTable.getSelectionModel().getSelectedItem();
        
        String store = this.storeField.getText();
        LocalDate dt = this.date.getValue();
        
        if (selected == null) {
            this.receiptService.addReceipt(store, dt);
        } else {
            this.receiptService.updateReceipt(selected, store, dt);
        }

        this.receiptTable.getSelectionModel().clearSelection();
        this.receiptService.clearItems();
        this.receiptTable.refresh();
        clearAllFields();
        updateTotal();        
    }
    
    /**
     * Kesken!
     * @param selected 
     */
    public void enableEditAndRemove(ReceiptItem selected) {

    }
    
    public void setSelectedReceipt(Receipt selected) {
        //this.selectedReceipt = selected;
    }
    
    public void editReceipt(Receipt receipt) {
        if (receipt == null) return;
        
        this.receiptService.clearItems();
        this.storeField.setText(receipt.getStore());
        this.date.setValue(receipt.getDate());
        this.receiptService.setReceiptItems(receipt.getItems());
        this.itemTable.refresh();
        updateTotal();
    }
    
    /**
     * Tyhjentää kaikki täytettävät kentät.
     */
    public void clearAllFields() {
        clearAddFields();
        this.storeField.setText("");
        this.date.setValue(null);
    }

    /**
     * Avaa virhedialogin.
     * @param message näytettävä virhe
     */
    public void errorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Oh no!");
        alert.setHeight(200);
        
        TextArea area = new TextArea(message);
        area.setWrapText(true);
        area.setEditable(false);
        
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);
        
        alert.showAndWait();
    }    
    
    /**
     * Tarkistetaan kaikki tuotteen lisäämiseen liittyvien kenttien oikeellisuus.
     * @return error-viesti merkkijonona. Tyhjä jos kaikki ok
     */
    public String checkAddItemFields() {
        String e = "";
        
        if ("".equals(this.productField.getText())) {
            e += "product name cannot be blank\n";
        }
        
        if (!this.doublePattern.matcher(this.priceField.getText()).matches()) {
            e += "price must be a number\n";
        }
        
        if (this.priceField.getText().equals("0")) {
            e += "price cannot be 0 or less\n";
        }
        
        if (this.qntyField.getText().equals("0")) {
            e += "quantity cannot be 0 or less\n";
        }
        
        if (!this.doublePattern.matcher(this.qntyField.getText()).matches()) {
            e += "quantity must be a number\n";
        }
        
        return e;
    }
    
    /**
     * Tarkistetaan kaikki kuitin lisäämiseen liittyvien kenttien oikeellisuus.
     * @return error-viesti merkkijonona. Tyhjä jos kaikki ok
     */
    public String checkAddReceiptFields() {
        String e = "";
        
        if ("".equals(this.storeField.getText())) {
            e += "store name cannot be blank\n";
        }
        
        if (this.date.getValue() == null) {
            e += "date cannot be blank\n";
        } else if (this.date.getValue().isAfter(LocalDate.now())) {
            e += "date cannot be in the future\n";
        }
        
        if (this.itemTable.getItems().isEmpty()) {
            e += "receipt must have at least one product";
        }
        
        return e;
    }
}
