package controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.MsService;
import util.IDGenerator;

public class ServiceController {

    public ObservableList<MsService> getAllServices() {
        ArrayList<MsService> services = MsService.getAllServices();
        return FXCollections.observableArrayList(services);
    }

    public boolean addService(String name, String durationStr, String priceStr, String desc) {
        if (!validateServiceInput(name, durationStr, priceStr, desc)) return false;

        int duration, price;
        try {
            duration = Integer.parseInt(durationStr);
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Duration and Price must be numeric");
            return false;
        }
        
        if (duration < 1 || duration > 30) {
            showAlert("Validation Error", "Duration must be between 1 and 30 days");
            return false;
        }
        if (price <= 0) {
            showAlert("Validation Error", "Price must be greater than 0");
            return false;
        }

        String newId = IDGenerator.generateID("serviceId", "MsService", "SV");
        
        MsService newService = new MsService(newId, name, duration, price, desc);
        if (newService.insertService()) {
            showAlert("Success", "Service Added Successfully");
            return true;
        }
        return false;
    }

    public boolean updateService(String id, String name, String durationStr, String priceStr, String desc) {
        if (id == null || id.isEmpty()) {
            showAlert("Error", "No service selected!");
            return false;
        }
        
        if (!validateServiceInput(name, durationStr, priceStr, desc)) return false;

        int duration, price;
        try {
            duration = Integer.parseInt(durationStr);
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Duration and Price must be numeric");
            return false;
        }

        if (duration < 1 || duration > 30) {
            showAlert("Validation Error", "Duration must be between 1 and 30 days");
            return false;
        }
        if (price <= 0) {
            showAlert("Validation Error", "Price must be greater than 0");
            return false;
        }

        MsService service = new MsService(id, name, duration, price, desc);
        if (service.updateService()) {
            showAlert("Success", "Service Updated Successfully");
            return true;
        }
        return false;
    }

    public boolean deleteService(String id) {
        if (id == null || id.isEmpty()) {
            showAlert("Error", "No service selected!");
            return false;
        }
        
        MsService service = new MsService(id, "", 0, 0, "");
        if (service.deleteService()) {
            showAlert("Success", "Service Deleted Successfully");
            return true;
        }
        return false;
    }

    private boolean validateServiceInput(String name, String dur, String price, String desc) {
        if (name.isEmpty() || dur.isEmpty() || price.isEmpty() || desc.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled");
            return false;
        }
        if (name.length() > 50) {
            showAlert("Validation Error", "Service Name max 50 characters");
            return false;
        }
        if (desc.length() > 250) {
            showAlert("Validation Error", "Description max 250 characters");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}