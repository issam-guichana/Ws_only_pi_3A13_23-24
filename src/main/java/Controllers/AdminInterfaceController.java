package Controllers;


import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.User;
import services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AdminInterfaceController implements Initializable {
    @FXML
    public Button bLogout;
    @FXML
    public TableView <User>tabUsers;
    @FXML
    public Button bRefresh;
    @FXML
    public JFXButton bGoToGestUser;
    @FXML
    private TableColumn<models.User, String> colUsername;
    @FXML
    private TableColumn<models.User, String> colEmail;
    @FXML
    private TableColumn<models.User, String> colAge;
    @FXML
    private TableColumn<models.User, String >colPassword;
    @FXML
    private TableColumn<models.User, String> colRole;
    @FXML
    public TableColumn<models.User, String> colGender;
    @FXML
    public TableColumn<models.User, String> colImage;
    @FXML
    public TableColumn<models.User, String> colStatus;
    UserService us = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Refresh(new ActionEvent());
        setupStatusButtonColumn();
    }
    public void Refresh(ActionEvent event) {
        UserService us = new UserService();
        ObservableList<User> oblist = FXCollections.observableArrayList();
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            oblist.addAll(us.selectAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tabUsers.setItems(oblist);
    }
    private void setupStatusButtonColumn() {
        TableColumn<models.User, Void> StatusColumn = new TableColumn<>("TestStat");
        StatusColumn.setCellFactory(col -> new TableCell<models.User, Void>() {
            private final Button StatusButton = new Button("TestStat");

            {
                StatusButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    // Toggle the status of the user
                    int currentStatus = Integer.parseInt(String.valueOf(user.getStatus()));
                    int newStatus = (currentStatus == 1) ? 0 : 1; // Toggle between 0 and 1
                    user.setStatus(Integer.parseInt(String.valueOf(newStatus)));

                    us.updateStatus(user);
                    // Update the UI to reflect the status change

                    StatusButton.setText(String.valueOf(newStatus));
                    Refresh(new ActionEvent());
                });
            }
         @Override
            protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                models.User user = getTableView().getItems().get(getIndex());
               // StatusButton.setText(String.valueOf(user.getStatus()));
                if (String.valueOf(user.getStatus()).equals("1"))
                    StatusButton.setText("Désactiver");
                else StatusButton.setText("Activer");
                setGraphic(StatusButton);
                Refresh(new ActionEvent());
            }
         }
        });
            tabUsers.getColumns().add(StatusColumn);
    }


    @FXML
    public void GoToGestUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminAddFormateur.fxml"));
        Parent root = loader.load();
        AdminAddFormateurController lc = loader.getController();
        bGoToGestUser.getScene().setRoot(root);
    }

   @FXML
    public void LogOut(ActionEvent event) {
       try {
           //taawed thezzek lel inscription
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
           Parent root = loader.load();
           bLogout.getScene().setRoot(root);

       } catch (IOException ex) {
           Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    @FXML
    public void Exit(ActionEvent event) {
        System.exit(0);
    }
}
