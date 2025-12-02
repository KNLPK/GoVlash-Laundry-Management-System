package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.MsNotification;
import util.IDGenerator;

public class NotificationController {

    public ObservableList<MsNotification> getUserNotifications(String userId) {
        ArrayList<MsNotification> list = MsNotification.getNotificationsByUser(userId);
        return FXCollections.observableArrayList(list);
    }

    public void sendFinishNotification(String customerId) {
        String notifId = IDGenerator.generateID("notifId", "MsNotification", "NT");
        String message = "Your order is finished and ready for pickup. Thank you for choosing our service!";
        Date now = Date.valueOf(LocalDate.now());

        MsNotification notif = new MsNotification(notifId, customerId, message, now, false);
        notif.sendNotification();
    }

    public void markAsRead(String notifId) {
        MsNotification.markAsRead(notifId);
    }

    public boolean deleteNotification(String notifId) {
        if (notifId == null) {
            showAlert("Error", "No notification selected!");
            return false;
        }
        
        MsNotification notif = new MsNotification(notifId, "", "", null, false);
        if (notif.deleteNotification()) {
            showAlert("Success", "Notification deleted.");
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