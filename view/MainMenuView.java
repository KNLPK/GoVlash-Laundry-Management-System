package view;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.UserSession;
import model.MsUser;

public class MainMenuView extends BorderPane {

    private Stage stage;
    private MenuBar menuBar;
    private Menu menuFile, menuManage, menuTransaction;
    private MenuItem miLogout, miExit;
    private MenuItem miServices, miEmployees; 
    private MenuItem miBuyService, miViewHistory, miViewTasks, miNotifications;

    public MainMenuView(Stage stage) {
        this.stage = stage;
        initialize();
        layouting();
        actions();

        Scene scene = new Scene(this, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("GoVlash Laundry - Dashboard");
        stage.centerOnScreen();
    }

    private void initialize() {
        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuManage = new Menu("Manage");
        menuTransaction = new Menu("Transaction");

        miLogout = new MenuItem("Logout");
        miExit = new MenuItem("Exit");

        miServices = new MenuItem("Manage Services");
        miEmployees = new MenuItem("Manage Employees");

        miBuyService = new MenuItem("Order Laundry");
        miViewHistory = new MenuItem("My History");
        miNotifications = new MenuItem("Notifications");
        
        miViewTasks = new MenuItem("Manage Orders");
    }

    private void layouting() {
        menuFile.getItems().addAll(miLogout, miExit);
        menuBar.getMenus().add(menuFile);

        MsUser currentUser = UserSession.getInstance().getCurrentUser();
        String role = (currentUser != null) ? currentUser.getRole() : "";

        if (role.equals("Admin")) {
            menuManage.getItems().addAll(miServices, miEmployees);
            menuBar.getMenus().add(menuManage);
            
            menuTransaction.getItems().add(miViewTasks);
            menuBar.getMenus().add(menuTransaction);
        } 
        else if (role.equals("Customer")) {
            menuTransaction.getItems().addAll(miBuyService, miViewHistory, miNotifications);
            menuBar.getMenus().add(menuTransaction);
        }
        else if (role.equals("Laundry Staff") || role.equals("Receptionist")) {
            menuTransaction.getItems().add(miViewTasks);
            menuBar.getMenus().add(menuTransaction);
        }

        this.setTop(menuBar);
    }

    private void actions() {
        miExit.setOnAction(e -> System.exit(0));

        miLogout.setOnAction(e -> {
            UserSession.getInstance().logout();
            new LoginView(stage);
        });

        miServices.setOnAction(e -> this.setCenter(new ServiceView()));
        miEmployees.setOnAction(e -> this.setCenter(new EmployeeView()));
        
        miBuyService.setOnAction(e -> this.setCenter(new TransactionView("Create")));
        miViewHistory.setOnAction(e -> this.setCenter(new TransactionView("History")));
        miNotifications.setOnAction(e -> this.setCenter(new NotificationView()));
        miViewTasks.setOnAction(e -> this.setCenter(new TransactionView("Tasks")));
    }
}