package controller;

import java.time.LocalDate;
import java.time.Period;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.MsUser;
import util.IDGenerator;

public class EmployeeController {

    public boolean addEmployee(String username, String password, String email, String role, String gender, LocalDate dob) {
        // 1. Validasi Input Kosong
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null || gender == null || dob == null) {
            showAlert("Validation Error", "All fields must be filled!");
            return false;
        }

        // 2. Validasi Username Unik
        if (!MsUser.isUsernameUnique(username)) {
            showAlert("Validation Error", "Username already taken!");
            return false;
        }

        // 3. Validasi Password (Min 6 char)
        if (password.length() < 6) {
            showAlert("Validation Error", "Password must be at least 6 characters!");
            return false;
        }

        // 4. Validasi Email (Harus @govlash.com)
        if (!email.endsWith("@govlash.com")) {
            showAlert("Validation Error", "Employee email must end with '@govlash.com'");
            return false;
        }

        // 5. Validasi Umur (Min 17 tahun)
        if (Period.between(dob, LocalDate.now()).getYears() < 17) {
            showAlert("Validation Error", "Employee must be at least 17 years old.");
            return false;
        }

        // 6. Generate ID & Simpan
        String newId = IDGenerator.generateID("userId", "MsUser", "US");
        MsUser newEmployee = new MsUser(newId, username, password, email, gender, java.sql.Date.valueOf(dob), role);

        if (newEmployee.register()) {
            showAlert("Success", "Employee added successfully!");
            return true;
        } else {
            showAlert("Error", "Failed to add employee to database.");
            return false;
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