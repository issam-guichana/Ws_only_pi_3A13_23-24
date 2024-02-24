package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminAddFormateurController implements Initializable {
    @FXML
    public TextField tfUsername;
    @FXML
    public TextField tfEmail;
    @FXML
    public TextField tfAge;
    @FXML
    public PasswordField tfPassword;
    @FXML
    public TextField cbRole;

    @FXML
    public Button bLogout;
    @FXML
    public Button bBack;
    public JFXButton bGoToAddFormateur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbRole.setText("FORMATEUR");
    }

    @FXML
    public void AddFormateur(ActionEvent event) {
        try {
            User p = new User(tfUsername.getText(), tfEmail.getText(),tfPassword.getText(), Integer.parseInt(tfAge.getText()),cbRole.getText());
            UserService sp = new UserService();
            sp.insertOne(p);
            System.out.println("FORMATEUR ADDED");
            GoToAddFormateur(new ActionEvent());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void Reset(ActionEvent event) {
        tfUsername.setText("");
        tfEmail.setText("");
        tfPassword.setText("");
        tfAge.setText("");
    }
    @FXML
    public void GoToAddFormateur(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminAddFormateur.fxml"));
        Parent root = loader.load();
        AdminAddFormateurController lc = loader.getController();
        bGoToAddFormateur.getScene().setRoot(root);
    }
    @FXML
    public void BackToAdminI(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminInterface.fxml"));
        Parent root = loader.load();
        AdminInterfaceController lc = loader.getController();
        bBack.getScene().setRoot(root);
    }

    @FXML
    public void LogOut(ActionEvent event) throws IOException {
        try {
            //thezek lel inscription
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
    @FXML
    void initialize(){
        assert tfAge != null : "fx:id=\"tfAge\" was not injected: check your FXML file 'AdminAddFormateur.fxml'.";
        assert tfUsername != null : "fx:id=\"tfUsername\" was not injected: check your FXML file 'AdminAddFormateur.fxml'.";
        assert tfPassword != null : "fx:id=\"tfPassword\" was not injected: check your FXML file 'AdminAddFormateur.fxml'.";
        assert tfEmail != null : "fx:id=\"tfEmail\" was not injected: check your FXML file 'AdminAddFormateur.fxml'.";
    }
}
