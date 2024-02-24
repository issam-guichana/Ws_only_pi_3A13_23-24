package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import models.User;
import services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class RegisterUserController implements Initializable {
   @FXML
    public TextField cbRole;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfAge;
    @FXML
    private Hyperlink hBackToLogin;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbRole.setText("CLIENT");
    }

    @FXML
    void AddUser(ActionEvent event){
        try {
            User p = new User(tfUsername.getText(), tfEmail.getText(),tfPassword.getText(), Integer.parseInt(tfAge.getText()),cbRole.getText());
            UserService sp = new UserService();
            sp.insertOne(p);
            System.out.println("CLIENT ADDED ");
            Reset(new ActionEvent());
            BackToLogin(new ActionEvent());
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
    public void BackToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/LoginUser.fxml"));
        Parent root = loader.load();
        LoginUserController lc = loader.getController();
        hBackToLogin.getScene().setRoot(root);
    }

    @FXML
    public void Reset(ActionEvent event) {
        tfUsername.setText("");
        tfEmail.setText("");
        tfPassword.setText("");
        tfAge.setText("");
    }

    @FXML
    private void exit(ActionEvent event){
        System.exit(0);
    }

    @FXML
    void initialize(){
        assert tfAge != null : "fx:id=\"tfAge\" was not injected: check your FXML file 'RegisterUser.fxml'.";
        assert tfUsername != null : "fx:id=\"tfUsername\" was not injected: check your FXML file 'RegisterUser.fxml'.";
        assert tfPassword != null : "fx:id=\"tfPassword\" was not injected: check your FXML file 'RegisterUser.fxml'.";
        assert tfEmail != null : "fx:id=\"tfEmail\" was not injected: check your FXML file 'RegisterUser.fxml'.";
    }


}
