package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.MsNotification;

public class NotificationView extends BorderPane {

    private TableView<MsNotification> table;
    private Button btnDelete, btnMarkRead;
    private Label lblTitle;

    public NotificationView() {
        initialize();
        layouting();
        actions();
    }

    private void initialize() {
        lblTitle = new Label("My Notifications");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        table = new TableView<>();
        
        TableColumn<MsNotification, String> contentCol = new TableColumn<>("Message");
        contentCol.setCellValueFactory(new PropertyValueFactory<>("notifContent"));
        contentCol.setMinWidth(300);
        
        TableColumn<MsNotification, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("notifDate"));
        
        TableColumn<MsNotification, Boolean> readCol = new TableColumn<>("Status");
        readCol.setCellValueFactory(new PropertyValueFactory<>("read"));
        readCol.setCellFactory(col -> new TableCell<MsNotification, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Read" : "Unread");
                    setStyle(item ? "-fx-text-fill: grey;" : "-fx-text-fill: green; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().addAll(contentCol, dateCol, readCol);

        btnDelete = new Button("Delete Notification");
        btnMarkRead = new Button("Mark as Read");
    }

    private void layouting() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        
        javafx.scene.layout.HBox btnBox = new javafx.scene.layout.HBox(10, btnMarkRead, btnDelete);
        
        container.getChildren().addAll(lblTitle, table, btnBox);
        this.setCenter(container);
    }

    private void actions() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isRead()) {
                System.out.println("Marking notification as read...");
            }
        });

        btnDelete.setOnAction(e -> {
            MsNotification selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                System.out.println("Deleting notification...");
            }
        });
    }
}