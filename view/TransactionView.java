package view;

import controller.ServiceController;
import controller.TransactionController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.MsService;
import model.MsUser;
import model.TransactionHeader;
import util.UserSession;

public class TransactionView extends BorderPane {
	private String pageMode;
    
    private TableView<TransactionHeader> table;
    private Label lblTitle;
    
    private ComboBox<String> cbService;
    private TextField txtWeight;
    private TextArea txtNotes;
    private Button btnSubmitOrder;

    private Button btnAssign, btnFinish;
    private TextField txtStaffIdInput;

    private TransactionController transController;
    private ServiceController serviceController;

    public TransactionView(String mode) {
        this.pageMode = mode;
        this.transController = new TransactionController();
        this.serviceController = new ServiceController(); 
        
        initialize();
        layouting();
        actions();
        
        prepareViewData();
    }

    private void initialize() {
        lblTitle = new Label("Transaction");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Setup Tabel
        table = new TableView<>();
        
        TableColumn<TransactionHeader, String> idCol = new TableColumn<>("Trans ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        
        TableColumn<TransactionHeader, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        
        TableColumn<TransactionHeader, String> custCol = new TableColumn<>("Customer ID");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        
        TableColumn<TransactionHeader, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
        
        TableColumn<TransactionHeader, String> staffCol = new TableColumn<>("Staff ID");
        staffCol.setCellValueFactory(new PropertyValueFactory<>("staffId"));

        table.getColumns().addAll(idCol, dateCol, custCol, statusCol, staffCol);

        // Init Form Components
        cbService = new ComboBox<>();
        cbService.setPromptText("Select a Service");
        
        txtWeight = new TextField();
        txtWeight.setPromptText("Weight (Kg)");
        
        txtNotes = new TextArea();
        txtNotes.setPromptText("Additional Notes (Optional)");
        txtNotes.setPrefHeight(60);
        
        btnSubmitOrder = new Button("Place Order");

        btnAssign = new Button("Assign Staff");
        btnFinish = new Button("Mark as Finished");
        txtStaffIdInput = new TextField();
        txtStaffIdInput.setPromptText("Input Staff ID");
    }

    private void layouting() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.getChildren().add(lblTitle);

        if (pageMode.equals("Create")) {
            GridPane form = new GridPane();
            form.setHgap(10); form.setVgap(10);
            
            form.add(new Label("Choose Service:"), 0, 0); form.add(cbService, 1, 0);
            form.add(new Label("Weight (Kg):"), 0, 1);    form.add(txtWeight, 1, 1);
            form.add(new Label("Notes:"), 0, 2);          form.add(txtNotes, 1, 2);
            form.add(btnSubmitOrder, 1, 3);
            
            container.getChildren().add(form);
            
        } else {
            container.getChildren().add(table);
            
            MsUser currentUser = UserSession.getInstance().getCurrentUser();
            if (currentUser != null) {
                
                if (currentUser.getRole().equals("Receptionist") && pageMode.equals("Tasks")) {
                    HBox assignBox = new HBox(10);
                    assignBox.getChildren().addAll(new Label("Assign Selected Order to:"), txtStaffIdInput, btnAssign);
                    container.getChildren().add(assignBox);
                } 
                
                else if (currentUser.getRole().equals("Laundry Staff") && pageMode.equals("Tasks")) {
                    container.getChildren().add(btnFinish);
                }
            }
        }
        
        this.setCenter(container);
    }

    private void prepareViewData() {
        MsUser u = UserSession.getInstance().getCurrentUser();
        if (u == null) return;

        if (pageMode.equals("Create")) {
            lblTitle.setText("Place New Laundry Order");
            
            cbService.getItems().clear();
            for (MsService s : serviceController.getAllServices()) {
                cbService.getItems().add(s.getServiceName());
            }
            
        } else {
            if (pageMode.equals("History")) {
                lblTitle.setText("My Transaction History");
            } else if (pageMode.equals("Tasks")) {
                if (u.getRole().equals("Receptionist")) lblTitle.setText("Pending Transactions (Need Assignment)");
                else if (u.getRole().equals("Laundry Staff")) lblTitle.setText("My Assigned Tasks");
                else lblTitle.setText("All Transactions Report");
            }
            refreshTable();
        }
    }

    private void refreshTable() {
        table.setItems(transController.getTransactionsByRole());
    }

    private void actions() {
        btnSubmitOrder.setOnAction(e -> {
            boolean success = transController.createOrder(
                cbService.getValue(), 
                txtWeight.getText(), 
                txtNotes.getText()
            );
            
            if (success) {
                cbService.getSelectionModel().clearSelection();
                txtWeight.clear();
                txtNotes.clear();
            }
        });

        btnAssign.setOnAction(e -> {
            TransactionHeader selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean success = transController.assignStaff(selected.getTransactionId(), txtStaffIdInput.getText());
                if (success) {
                    refreshTable(); 
                    txtStaffIdInput.clear();
                }
            }
        });

        btnFinish.setOnAction(e -> {
            TransactionHeader selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean success = transController.finishOrder(selected.getTransactionId(), selected.getCustomerId());
                if (success) {
                    refreshTable();
                }
            }
        });
    }
}