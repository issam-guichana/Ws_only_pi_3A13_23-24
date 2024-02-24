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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Refresh(new ActionEvent());
    }
    public void Refresh(ActionEvent event) {
        UserService us = new UserService();
        ObservableList<User> oblist = FXCollections.observableArrayList();
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        try {
            oblist.addAll(us.selectAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tabUsers.setItems(oblist);
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
