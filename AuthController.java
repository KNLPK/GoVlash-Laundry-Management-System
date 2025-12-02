package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.MsUser;
import util.IDGenerator;
import util.UserSession;
import view.LoginView;
import view.MainMenuView;
import view.RegisterView;
import javafx.stage.Stage;

public class AuthController {
    
    public void login(String username, String password, Stage stage) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and Password cannot be empty!");
            return;
        }
        
        MsUser user = MsUser.login(username, password);
        
        if (user != null) {
            UserSession.getInstance().setCurrentUser(user);
            
            new MainMenuView(stage);
        } else {
            showAlert("Login Failed", "Invalid Username or Password");
        }
    }
    
    public void register(String username, String password, String confirmPass, String email, 
                         String gender, java.time.LocalDate dob, Stage stage) {
        
        // 1. Username Validation
        if (username.isEmpty()) {
            showAlert("Validation Error", "Username cannot be empty");
            return;
        }
        if (!MsUser.isUsernameUnique(username)) {
            showAlert("Validation Error", "Username already taken!");
            return;
        }
        
        // 2. Email Validation (Must end with @email.com & Unique)
        if (email.isEmpty()) {
            showAlert("Validation Error", "Email cannot be empty");
            return;
        }
        if (!email.endsWith("@email.com")) {
            showAlert("Validation Error", "Email must end with '@email.com'");
            return;
        }
        
        // 3. Password Validation (Min 6 chars)
        if (password.length() < 6) {
            showAlert("Validation Error", "Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPass)) {
            showAlert("Validation Error", "Confirm Password does not match");
            return;
        }
        
        // 4. Gender Validation
        if (gender == null) {
            showAlert("Validation Error", "Gender must be selected");
            return;
        }
        
        // 5. DOB Validation (Min 12 years old)
        if (dob == null) {
            showAlert("Validation Error", "Date of Birth cannot be empty");
            return;
        }
        java.time.LocalDate now = java.time.LocalDate.now();
        if (java.time.Period.between(dob, now).getYears() < 12) {
            showAlert("Validation Error", "You must be at least 12 years old to register");
            return;
        }

        String newId = IDGenerator.generateID("userId", "MsUser", "US");
        
        MsUser newUser = new MsUser(newId, username, password, email, gender, java.sql.Date.valueOf(dob), "Customer");
        
        if (newUser.register()) {
            showAlert("Success", "Registration Successful! Please Login.");
            new LoginView(stage); 
        } else {
            showAlert("Error", "Registration Failed. Database Error.");
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}