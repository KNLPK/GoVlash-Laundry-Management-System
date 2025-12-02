package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.MsService;
import model.TransactionDetail;
import model.TransactionHeader;
import util.IDGenerator;
import util.UserSession;

public class TransactionController {
    
    private NotificationController notifController = new NotificationController();

    public ObservableList<TransactionHeader> getTransactionsByRole() {
        model.MsUser currentUser = UserSession.getInstance().getCurrentUser();
        ArrayList<TransactionHeader> list = new ArrayList<>();

        if (currentUser.getRole().equals("Admin")) {
            list = TransactionHeader.getAllTransactions();
        } 
        else if (currentUser.getRole().equals("Receptionist")) {
            list = TransactionHeader.getPendingTransactions();
        } 
        else if (currentUser.getRole().equals("Laundry Staff")) {
            list = TransactionHeader.getAssignedTransactions(currentUser.getUserId());
        } 
        else if (currentUser.getRole().equals("Customer")) {
            list = TransactionHeader.getCustomerHistory(currentUser.getUserId());
        }
        
        return FXCollections.observableArrayList(list);
    }

    public boolean createOrder(String serviceName, String weightStr, String notes) {
        // 1. Validasi Input
        if (serviceName == null || weightStr.isEmpty()) {
            showAlert("Validation Error", "Service and Weight must be filled");
            return false;
        }
        
        // 2. Validasi Weight (2 - 50 Kg)
        int weight = 0;
        try {
            weight = Integer.parseInt(weightStr);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Weight must be a number");
            return false;
        }

        if (weight < 2 || weight > 50) {
            showAlert("Validation Error", "Weight must be between 2 and 50 Kg");
            return false;
        }
        
        // 3. Validasi Notes (Max 250 char)
        if (notes.length() > 250) {
            showAlert("Validation Error", "Notes cannot exceed 250 characters");
            return false;
        }

        // 4. Proses Insert: Ambil Service ID berdasarkan nama
        MsService service = MsService.getServiceByName(serviceName);
        if (service == null) return false;

        String transId = IDGenerator.generateID("transactionId", "TransactionHeader", "TR");
        String custId = UserSession.getInstance().getCurrentUser().getUserId();
        Date now = Date.valueOf(LocalDate.now());

        TransactionHeader header = new TransactionHeader(transId, custId, null, null, now, "Pending");
        
        if (header.insertTransaction()) {
            TransactionDetail detail = new TransactionDetail(transId, service.getServiceId());
            detail.insertDetail();
            
            showAlert("Success", "Order Placed Successfully!");
            return true;
        }
        return false;
    }

    public boolean assignStaff(String transId, String staffId) {
        if (transId == null) {
            showAlert("Error", "No transaction selected");
            return false;
        }
        if (staffId.isEmpty()) {
            showAlert("Error", "Please input Staff ID");
            return false;
        }

        String receptionistId = UserSession.getInstance().getCurrentUser().getUserId();
        
        if (TransactionHeader.assignStaff(transId, staffId, receptionistId)) {
            showAlert("Success", "Task assigned to " + staffId);
            return true;
        }
        return false;
    }

    public boolean finishOrder(String transId, String customerId) {
        if (transId == null) {
            showAlert("Error", "No transaction selected");
            return false;
        }

        if (TransactionHeader.finishTransaction(transId)) {
            notifController.sendFinishNotification(customerId);
            
            showAlert("Success", "Order Finished. Notification Sent to Customer.");
            return true;
        }
        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}