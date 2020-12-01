package receiptapp;

import java.net.URL;
import java.time.LocalDate;
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
    
    @FXML private TableView<ReceiptItem> itemTable;
    @FXML private TableColumn<ReceiptItem, String> productCol;
    @FXML private TableColumn<ReceiptItem, Double> priceCol;
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
        this.itemTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) 
                        -> enableEditAndRemove(newSelection));
        
        this.storeCol.setCellValueFactory(new PropertyValueFactory<>("store"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.productsCol.setCellValueFactory(new PropertyValueFactory<>("productCount"));
        this.totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        this.receiptTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) ->
                              setSelectedReceipt(newSelection));
    }


    @FXML
    void handleAddItem(ActionEvent event) {
        addItem();
        updateItemTableAndTotal();
        clearAddFields();
    }
    
    @FXML
    void handleOk(ActionEvent event) {
        addReceipt();
        updateReceiptTable();
        clearAllFields();
        updateItemTableAndTotal();
    }
        
    @FXML
    void handleEditItem(ActionEvent event) {
        System.out.println("nyt voi muokata");
        
        this.productField.setText(this.selectedItem.getProduct());
        this.priceField.setText("" + this.selectedItem.getPrice());
        this.qntyField.setText("" + this.selectedItem.getQuantity());
        this.unitChoice.setValue(this.selectedItem.getUnit());
        int selectedId = this.selectedItem.getId();
        
    }
        
    /**
     * Lisätään uusi tuote itemTableen. Tarkistetaan ensin onko vaaditut kentät
     * täytetty oikein.
     * TODO: error-dialogi jos ei
     */
    public void addItem() {
        String error = checkAddFields();
        
        if (error.length() > 0) {
            // TODO: tee error-dialogi
            System.out.println(error);
        } else {
        
            String product = this.productField.getText();
            double price = Double.parseDouble(this.priceField.getText());
            String unit = this.unitChoice.getValue();
            double qnty = Double.parseDouble(this.qntyField.getText());
            
            ReceiptItem i = new ReceiptItem(product, price, qnty, unit);
            this.receiptService.addReceiptItem(i);
        }        
    }
    
    /**
     * Päivitetään kuitit sisältävä receiptTable.
     */
    public void updateReceiptTable() {
        this.receiptTable.getItems().clear();
        ArrayList<Receipt> receipts = this.receiptService.getReceipts();
        
        for (Receipt receipt : receipts) {
            this.receiptTable.getItems().add(receipt);
        }
    }
    
    /**
     * Päivitetään tuotetaulukko itemTable uuden tuotteen lisäyksen jälkeen.
     */
    public void updateItemTableAndTotal() {
        this.itemTable.getItems().clear();
        double total = 0;
        
        ArrayList<ReceiptItem> items = this.receiptService.getReceiptItems();
        for (ReceiptItem item : items) {
            this.itemTable.getItems().add(item);
            total += item.getPrice();
        }
        
        this.receiptTotal.setText("" + total);
    }
    
    /**
     * Tyhjennetään kaikki uuden tuotteen lisäämiseen liittyvät kentät.
     */
    public void clearAddFields() {
        this.productField.setText("");
        this.priceField.setText("");
        this.qntyField.setText("");
    }
    
    /**
     * Kesken!
     * @param selected 
     */
    public void enableEditAndRemove(ReceiptItem selected) {
        if (selected == null) {
            this.editItemBtn.setDisable(true);
            this.deleteItemBtn.setDisable(true);
        } else {
            this.editItemBtn.setDisable(false);
            this.deleteItemBtn.setDisable(false);
            this.selectedItem = selected;
        }
    }
    
    public void setSelectedReceipt(Receipt selected) {
        this.selectedReceipt = selected;
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
     * Lisätään uusi kuitti. Samalla tarkistetaan onko kaikki lisäämiseen
     * vaadittavat kentät täytetty oikein.
     */
    public void addReceipt() {
        String error = checkDemandedFields();
        
        if (error.length() > 0) {
            System.out.println(error);
        } else {
            String store = this.storeField.getText();
            LocalDate dt = this.date.getValue();
            ArrayList<ReceiptItem> items = new ArrayList<>(this.itemTable.getItems());
            
            Receipt receipt = new Receipt(store, dt, items);
            this.receiptService.addReceipt(receipt);
        }
    }
    
    /**
     * Tyhjentää kaikki täytettävät kentät.
     */
    public void clearAllFields() {
        clearAddFields();
        this.storeField.setText("");
        this.date.setValue(null);
    }


    @FXML
    void handleRemoveReceipt(ActionEvent event) {

    }

    @FXML
    void handleDeleteItem(ActionEvent event) {
        
    }
    
    /**
     * Tarkistetaan kaikki tuotteen lisäämiseen liittyvien kenttien oikeellisuus.
     * @return error-viesti merkkijonona. Tyhjä jos kaikki ok
     */
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
    
    /**
     * Tarkistetaan kaikki kuitin lisäämiseen liittyvien kenttien oikeellisuus.
     * @return error-viesti merkkijonona. Tyhjä jos kaikki ok
     */
    public String checkDemandedFields() {
        String e = "";
        
        if ("".equals(this.storeField.getText())) {
            e += "store name cannot be blank\n";
        }
        
        if (this.date.getValue() == null) {
            e += "date cannot be blank";
        }
        
        if (this.itemTable.getItems().isEmpty()) {
            e += "receipt must have at least one product";
        }
        
        return e;
    }
}
