package view;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RegisterView extends BorderPane {

    private Stage stage;
    private GridPane grid;
    private Label lblTitle, lblUsername, lblPassword, lblConfirmPass, lblEmail, lblGender, lblDOB, lblError;
    private TextField txtUsername, txtEmail;
    private PasswordField txtPassword, txtConfirmPass;
    private ToggleGroup genderGroup;
    private RadioButton rbMale, rbFemale;
    private DatePicker dobPicker;
    private Button btnRegister, btnBack;

    public RegisterView(Stage stage) {
        this.stage = stage;
        initialize();
        layouting();
        actions();

        Scene scene = new Scene(this, 600, 500);
        stage.setScene(scene);
        stage.setTitle("GoVlash Laundry - Register");
        stage.centerOnScreen();
    }

    private void initialize() {
        lblTitle = new Label("Register Customer");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        lblUsername = new Label("Username:");
        lblPassword = new Label("Password:");
        lblConfirmPass = new Label("Confirm Password:");
        lblEmail = new Label("Email:");
        lblGender = new Label("Gender:");
        lblDOB = new Label("Date of Birth:");
        
        lblError = new Label("");
        lblError.setStyle("-fx-text-fill: red;");

        txtUsername = new TextField();
        txtEmail = new TextField();
        txtPassword = new PasswordField();
        txtConfirmPass = new PasswordField();

        genderGroup = new ToggleGroup();
        rbMale = new RadioButton("Male");
        rbFemale = new RadioButton("Female");
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);

        dobPicker = new DatePicker();

        btnRegister = new Button("Register");
        btnRegister.setPrefWidth(100);
        
        btnBack = new Button("Back to Login");
    }

    private void layouting() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(lblUsername, 0, 0); grid.add(txtUsername, 1, 0);
        grid.add(lblPassword, 0, 1); grid.add(txtPassword, 1, 1);
        grid.add(lblConfirmPass, 0, 2); grid.add(txtConfirmPass, 1, 2);
        grid.add(lblEmail, 0, 3); grid.add(txtEmail, 1, 3);
        
        javafx.scene.layout.HBox genderBox = new javafx.scene.layout.HBox(10);
        genderBox.getChildren().addAll(rbMale, rbFemale);
        grid.add(lblGender, 0, 4); grid.add(genderBox, 1, 4);
        
        grid.add(lblDOB, 0, 5); grid.add(dobPicker, 1, 5);
        grid.add(btnRegister, 1, 6);

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));
        topBox.getChildren().addAll(lblTitle, lblError);

        this.setTop(topBox);
        this.setCenter(grid);
        
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        bottomBox.getChildren().add(btnBack);
        this.setBottom(bottomBox);
    }

    private void actions() {
        AuthController authController = new AuthController();

        btnBack.setOnAction(e -> {
            new LoginView(stage);
        });

        btnRegister.setOnAction(e -> {
            RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
            String gender = (selectedGender != null) ? selectedGender.getText() : null;
            
            authController.register(
                txtUsername.getText(),
                txtPassword.getText(),
                txtConfirmPass.getText(),
                txtEmail.getText(),
                gender,
                dobPicker.getValue(),
                stage
            );
        });
    }
}