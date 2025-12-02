package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.MsUser;

public class EmployeeView extends BorderPane {

    private TableView<MsUser> table;
    private TextField txtUsername, txtEmail;
    private PasswordField txtPassword;
    private ComboBox<String> cbRole;
    private RadioButton rbMale, rbFemale;
    private ToggleGroup genderGroup;
    private DatePicker dobPicker;
    private Button btnAdd, btnClear;
    private Label lblError;

    public EmployeeView() {
        initialize();
        layouting();
        actions();
    }

    private void initialize() {
        // Table Setup
        table = new TableView<>();
        
        TableColumn<MsUser, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        
        TableColumn<MsUser, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<MsUser, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        TableColumn<MsUser, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idCol, nameCol, roleCol, emailCol);

        // Form Components
        txtUsername = new TextField();
        txtEmail = new TextField();
        txtPassword = new PasswordField();
        
        cbRole = new ComboBox<>();
        cbRole.getItems().addAll("Laundry Staff", "Receptionist");
        cbRole.setPromptText("Select Role");

        genderGroup = new ToggleGroup();
        rbMale = new RadioButton("Male");
        rbFemale = new RadioButton("Female");
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);

        dobPicker = new DatePicker();
        
        lblError = new Label("");
        lblError.setStyle("-fx-text-fill: red;");

        btnAdd = new Button("Add Employee");
        btnClear = new Button("Clear");
    }

    private void layouting() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        Label title = new Label("Manage Employees");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);
        
        form.add(new Label("Username:"), 0, 0); form.add(txtUsername, 1, 0);
        form.add(new Label("Password:"), 0, 1); form.add(txtPassword, 1, 1);
        form.add(new Label("Email:"), 0, 2); form.add(txtEmail, 1, 2);
        form.add(new Label("Role:"), 0, 3); form.add(cbRole, 1, 3);
        
        javafx.scene.layout.HBox genderBox = new javafx.scene.layout.HBox(10, rbMale, rbFemale);
        form.add(new Label("Gender:"), 0, 4); form.add(genderBox, 1, 4);
        
        form.add(new Label("DOB:"), 0, 5); form.add(dobPicker, 1, 5);
        
        javafx.scene.layout.HBox btnBox = new javafx.scene.layout.HBox(10, btnAdd, btnClear);
        form.add(btnBox, 1, 6);

        container.getChildren().addAll(title, table, lblError, form);
        this.setCenter(container);
    }

    private void actions() {
        btnClear.setOnAction(e -> {
            txtUsername.clear();
            txtEmail.clear();
            txtPassword.clear();
            cbRole.getSelectionModel().clearSelection();
            genderGroup.selectToggle(null);
            dobPicker.setValue(null);
        });

        btnAdd.setOnAction(e -> {
            System.out.println("Add Employee Clicked");
        });
    }
}