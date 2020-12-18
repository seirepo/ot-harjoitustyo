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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import receiptapp.domain.ReceiptItem;
import receiptapp.domain.Receipt;

/**
 * FXML Controller class.
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
    @FXML private Button addOrSaveItemBtn;
    @FXML private Button cancelEditReceiptBtn;
    @FXML private Button addProductBtn;
    @FXML private Button addNewReceiptBtn;
    @FXML private Button deleteReceiptBtn;
    @FXML private Button editItemBtn;
    @FXML private Button deleteItemBtn;
    @FXML private Button addOrSaveReceiptBtn;
    @FXML private Button cancelEditItemBtn;
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
    
    /**
     * Konstruktori. Alustetaan ei-fxml-attribuutit.
     */
    public ReceiptController() {
        this.doublePattern = Pattern.compile("[0-9]*\\.[0-9]+|[0-9]+");
    }
    
    /**
     * Asetetaan controlleriin liittyvä sovellus.
     * @param application sovellus
     */
    public void setApplication(ReceiptMain application) {
        this.application = application;
    }
    
    /**
     * Asetetaan käytettävä receiptService-olio.
     * @param receiptService receiptService
     */
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
                .addListener((obs, oldSelection, newSelection)
                    -> editReceipt(newSelection));
    }


    @FXML
    void handleAddOrSaveItem(ActionEvent event) {
        addOrSaveItem();
    }

    @FXML
    void handleCancelEditItem(ActionEvent event) {
        cancelEditingItem();
    }
        
    @FXML
    void handleDeleteItem(ActionEvent event) {
        deleteItem();
    }    
    
    @FXML
    void handleAddNewReceipt(ActionEvent event) {
        addNewReceipt();
    }
    
    @FXML
    void handleAddOrSaveReceipt(ActionEvent event) {
        addOrSaveReceipt();
    }

    @FXML
    void handleCancelEditReceipt(ActionEvent event) {
        cancelEditReceipt();
    }
    
    @FXML
    void handleDeleteReceipt(ActionEvent event) {
        deleteReceipt();
    }

    /**
     * Lisätään uusi tuote itemTableen. Tarkistetaan ensin onko vaaditut kentät
     * täytetty oikein. Jos ei, palautetaan virheilmoitus error-dialogissa.
     * Tämän jälkeen otetaan vaadittujen kenttien sisältö, ja luodaan niiden
     * pohjalta uusi ReceiptItem-olio (jos itemTablen valinta on null), tai
     * päivitetään itemTablessa valituna olevan kuitin tiedot. Lopussa
     * tyhjennetään kentät ja itemTablen valinta.
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
        boolean result;
        
        if (selected == null) {            
            ReceiptItem i = new ReceiptItem(product, price, isUnitPrice, qnty, unit);
            result = this.receiptService.addReceiptItem(i);
        } else {
            result = this.receiptService.updateItem(selected, product, price, isUnitPrice,
                    qnty, unit);
        }
        
        if (!result) {
            errorDialog("Virhe tuotteen tallennuksessa :^(");
        }
        
        this.itemTable.refresh();
        this.itemTable.getSelectionModel().clearSelection();
        clearAddFields();
        updateTotal();        
    }
    
    /**
     * Asetetaan valitun itemin tiedot näkyviin tuotteen lisäyskenttiin.
     * @param item valittu tuote
     */
    public void editItem(ReceiptItem item) {
        
        if (item == null) {
            return;
        }
        
        this.productField.setText(item.getProduct());
        this.priceField.setText("" + item.getPrice());
        this.unitPriceCheck.setSelected(item.getIsUnitPrice());
        this.qntyField.setText("" + item.getQuantity());
        this.unitChoice.setValue(item.getUnit());
    }
    
    /**
     * Perutaan tuotteen muokkaaminen: tyhjennetään ItemTablen valinta ja
     * tuotteen lisäämiseen liittyvät kentät.
     */
    public void cancelEditingItem() {
        this.itemTable.getSelectionModel().clearSelection();
        clearAddFields();
    }
    
    /**
     * Haetaan valittuna oleva tuote ja poistetaan se. Tämän jälkeen päivitetään
     * itemTable ja loppusumma, sekä tyhjennetään itemTablen valinta.
     * Jos valittu tuote on null, näytetään error-dialogi.
     */
    public void deleteItem() {
        ReceiptItem selected = this.itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorDialog("No item selected ¯\\_(ツ)_/¯");
            return;
        }
        
        boolean result = this.receiptService.deleteItem(selected);
        if (!result) {
            //errorDialog("Virhe tuotteen poistossa :^(");
            String errMsg = this.receiptService.getSQLErrorMessage();
            errorDialog(errMsg);
            return;
        }
        this.itemTable.getSelectionModel().clearSelection();
        clearAddFields();
        updateTotal();
        this.itemTable.refresh();
    }
        
    /**
     * Tyhjennetään näkymän oikea puoli ja item-taulukko, jotta uuden kuitin
     * lisääminen on mahdollista.
     */
    public void addNewReceipt() {
        this.receiptService.clearItems();
        this.receiptTable.getSelectionModel().clearSelection();
        clearAddFields();
        clearAllFields();
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
        
        boolean result;
        
        if (selected == null) {
            result = this.receiptService.addReceipt(store, dt);
        } else {
            result = this.receiptService.updateReceipt(selected, store, dt);
        }
        
        if (!result) {
            String errMsg = this.receiptService.getSQLErrorMessage();
            errorDialog(errMsg);
            return;
        }

        this.receiptTable.getSelectionModel().clearSelection();
        this.receiptService.clearItems();
        this.receiptTable.refresh();
        clearAllFields();
        updateTotal();        
    }

    /**
     * Tyhjennetään jälleen näkymän oikea puoli, jos halutaankin perua
     * kuitin muokkaaminen tai uuden kuitin lisääminen. Kutsuu
     * addNewReceipt-metodia.
     * TODO: vahvistus?
     */
    public void cancelEditReceipt() {
        addNewReceipt();
    }
    
    /**
     * Poistetaan valittu kuitti. Näytetään virheviesti jos mitään kuittia ei
     * ole valittuna.
     * TODO: vahvistus?
     */
    public void deleteReceipt() {
        Receipt selected = this.receiptTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorDialog("No receipt selected ¯\\_(ツ)_/¯");
            return;
        }
        
        boolean result = this.receiptService.deleteReceipt(selected);
        
        if (!result) {
            String errMsg = this.receiptService.getSQLErrorMessage();
            errorDialog(errMsg);
            return;
        }
        
        this.receiptTable.getSelectionModel().clearSelection();
        this.receiptService.clearItems();
        clearAllFields();
        this.receiptTable.refresh();
        this.itemTable.refresh();
    }
    
    /**
     * Asetetaan valittuna olevan kuitin tiedot kuittikohtaiseen näkymään.
     * Tämä jälkeen asetetaan itemTablessa näkyviksi tuotteiksi muokattavan
     * kuitin tuotteet ja päivitetään table.
     * Jos valittu kuitti on null, ei tehdä mitään.
     * @param receipt valittu kuitti
     */
    public void editReceipt(Receipt receipt) {
        if (receipt == null) {
            return;
        }
        
        this.receiptService.clearItems();
        this.storeField.setText(receipt.getStore());
        this.date.setValue(receipt.getDate());
        this.receiptService.setReceiptItems(receipt.getItems());
        this.itemTable.refresh();
        updateTotal();
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
     * Tyhjentää kaikki täytettävät kentät.
     */
    public void clearAllFields() {
        clearAddFields();
        this.storeField.setText("");
        this.date.setValue(null);
    }

    /**
     * Avaa virhedialogin ja näyttää parametrina annetun viestin.
     * @param message näytettävä viesti
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
