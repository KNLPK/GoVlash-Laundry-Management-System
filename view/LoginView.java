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

public class LoginView extends BorderPane {

    private Stage stage;
    private GridPane grid;
    private Label lblTitle, lblUsername, lblPassword, lblError;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Hyperlink linkRegister;
    private AuthController authController;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.authController = new AuthController(); 
        initialize();
        layouting();
        actions();
        
        Scene scene = new Scene(this, 500, 400);
        stage.setScene(scene);
        stage.setTitle("GoVlash Laundry - Login");
        stage.centerOnScreen();
        stage.show();
    }

    private void initialize() {
        grid = new GridPane();
        lblTitle = new Label("GoVlash Laundry");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblUsername = new Label("Username:");
        lblPassword = new Label("Password:");
        lblError = new Label("");
        lblError.setStyle("-fx-text-fill: red;");
        txtUsername = new TextField();
        txtPassword = new PasswordField();
        btnLogin = new Button("Login");
        btnLogin.setPrefWidth(100);
        linkRegister = new Hyperlink("Don't have an account? Register here");
    }

    private void layouting() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(lblUsername, 0, 0); grid.add(txtUsername, 1, 0);
        grid.add(lblPassword, 0, 1); grid.add(txtPassword, 1, 1);
        grid.add(btnLogin, 1, 2);
        
        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getChildren().addAll(lblTitle, lblError, grid, linkRegister);
        this.setCenter(centerContainer);
    }

    private void actions() {
        btnLogin.setOnAction(e -> {
            authController.login(txtUsername.getText(), txtPassword.getText(), stage);
        });

        linkRegister.setOnAction(e -> {
            new RegisterView(stage);
        });
    }
}