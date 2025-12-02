package view;

import controller.ServiceController;
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

public class ServiceView extends BorderPane {

    private TableView<MsService> table;
    private TextField txtName, txtDuration, txtPrice;
    private TextArea txtDesc;
    private Button btnAdd, btnUpdate, btnDelete, btnClear;
    private Label lblTitle;
    
    private ServiceController controller;

    public ServiceView() {
        this.controller = new ServiceController(); 
        initialize();
        layouting();
        actions();
        refreshTable(); 
    }

    private void initialize() {
        lblTitle = new Label("Manage Services");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        table = new TableView<>();
        
        TableColumn<MsService, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        idCol.setMinWidth(50);
        
        TableColumn<MsService, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        nameCol.setMinWidth(150);
        
        TableColumn<MsService, Integer> durationCol = new TableColumn<>("Duration (Days)");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("serviceDuration"));
        
        TableColumn<MsService, Integer> priceCol = new TableColumn<>("Price (Rp)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        
        TableColumn<MsService, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("serviceDesc"));
        descCol.setMinWidth(200);

        table.getColumns().addAll(idCol, nameCol, durationCol, priceCol, descCol);

        txtName = new TextField();
        txtName.setPromptText("Service Name");
        
        txtDuration = new TextField();
        txtDuration.setPromptText("Duration in days (e.g., 2)");
        
        txtPrice = new TextField();
        txtPrice.setPromptText("Price (e.g., 15000)");
        
        txtDesc = new TextArea();
        txtDesc.setPromptText("Service Description");
        txtDesc.setPrefHeight(60);

        btnAdd = new Button("Add Service");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");
        btnClear = new Button("Clear Form");
    }

    private void layouting() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        GridPane form = new GridPane();
        form.setHgap(10); 
        form.setVgap(10);
        
        form.add(new Label("Name:"), 0, 0);       form.add(txtName, 1, 0);
        form.add(new Label("Duration:"), 0, 1);   form.add(txtDuration, 1, 1);
        form.add(new Label("Price:"), 0, 2);      form.add(txtPrice, 1, 2);
        form.add(new Label("Description:"), 0, 3);form.add(txtDesc, 1, 3);
        
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnClear);
        form.add(btnBox, 1, 4);

        container.getChildren().addAll(lblTitle, table, new Separator(), form);
        this.setCenter(container);
    }

    private void actions() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtName.setText(newVal.getServiceName());
                txtDuration.setText(String.valueOf(newVal.getServiceDuration()));
                txtPrice.setText(String.valueOf(newVal.getServicePrice()));
                txtDesc.setText(newVal.getServiceDesc());
            }
        });

        btnClear.setOnAction(e -> {
            txtName.clear();
            txtDuration.clear();
            txtPrice.clear();
            txtDesc.clear();
            table.getSelectionModel().clearSelection();
        });

        btnAdd.setOnAction(e -> {
            boolean success = controller.addService(
                txtName.getText(), 
                txtDuration.getText(), 
                txtPrice.getText(), 
                txtDesc.getText()
            );
            if (success) {
                refreshTable();
                btnClear.fire(); 
            }
        });

        btnUpdate.setOnAction(e -> {
            MsService selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean success = controller.updateService(
                    selected.getServiceId(), 
                    txtName.getText(), 
                    txtDuration.getText(), 
                    txtPrice.getText(), 
                    txtDesc.getText()
                );
                if (success) {
                    refreshTable();
                    btnClear.fire();
                }
            }
        });

        btnDelete.setOnAction(e -> {
            MsService selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean success = controller.deleteService(selected.getServiceId());
                if (success) {
                    refreshTable();
                    btnClear.fire();
                }
            }
        });
    }

    private void refreshTable() {
        table.setItems(controller.getAllServices());
    }
}