package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.User;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSettingsController implements Initializable {

    public TextField tfUsername;
    public TextField tfEmail;
    public TextField tfAge;
    @FXML
    private Button bGotoupdate;
    @FXML
    private Button bGotouppwd;
    @FXML
    private Button bGotodeleteacc;
    @FXML
    private Button bGotopay;
    @FXML
    private Button bLogout;
    @FXML
    private Button bBack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserService us =new UserService();

        tfUsername.setText(us.ChercherParId(LoginUserController.logged).getUsername());
        tfEmail.setText(us.ChercherParId(LoginUserController.logged).getEmail());
        tfAge.setText(String.valueOf(us.ChercherParId(LoginUserController.logged).getAge()));
    }
    public void BackToMenu(ActionEvent event) {
        // to doooo
    }
    public void GoToUpdateProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/UpdateProfile.fxml"));
        Parent root = loader.load();
        UpdateProfileController lc = loader.getController();
        bGotoupdate.getScene().setRoot(root);
    }

    public void GoToResetPwd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/ResetPwd.fxml"));
        Parent root = loader.load();
        ResetPwdController lc = loader.getController();
        bGotouppwd.getScene().setRoot(root);
    }

    public void GoToDeleteAcc(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/DeleteProfile.fxml"));
        Parent root = loader.load();
        DeleteProfileController lc = loader.getController();
        bGotodeleteacc.getScene().setRoot(root);
    }
    public void GoToPaymentMethod(ActionEvent event)throws IOException {
        //Tooo dooooo
    }

    public void LogOut(ActionEvent event) throws IOException{
        try {
            //thezek lel inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            bLogout.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void Exit(ActionEvent event) {
        System.exit(0);
    }
}
