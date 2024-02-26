package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import models.User;
import services.UserService;
import utils.DBconnection;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResetPwdController {
    @FXML
    public PasswordField tfPassword;
    @FXML
    public PasswordField tfNewPassword;
    @FXML
    public PasswordField tfCfPassword;
    public Button bBack;
    @FXML
    private Button bSave;
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

    PreparedStatement pst = null;
    ResultSet rs = null;
    public void UpdatePwd(ActionEvent event) {
        //controle de saisie
        // Check if any required field is empty
        if (tfNewPassword.getText().isEmpty() || tfCfPassword.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tout les champs.");
            return;
        }
        if (tfPassword.getLength()<6){
            showAlert("Erreur", "Votre mot de passe doit contenir au moins 6 caractères");
            return;
        }
        else if (!tfNewPassword.getText().equals(tfCfPassword.getText())) {
            showAlert("Erreur", "Les deux mots de passe ne sont pas identiques");
            return;
        }
        UserService userService= new UserService();
        Connection cnx = DBconnection.getInstance().getCnx();
        String sql = "Select * from user where id_user = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, LoginUserController.logged + "");
            rs = pst.executeQuery();
            int idModf = LoginUserController.logged ;

            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous vraiment modifier votre mot de passe ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    String username = userService.ChercherParId(idModf).getUsername();
                    String email = userService.ChercherParId(idModf).getEmail() ;
                    int age = userService.ChercherParId(idModf).getAge();
                    String role =userService.ChercherParId(idModf).getRole();

                    String pwd = tfPassword.getText();
                    String newPwd = tfNewPassword.getText();
                    String CfPsw = tfCfPassword.getText();

                    if(pwd.equals(userService.ChercherParId(idModf).getMdp()) && newPwd.equals(CfPsw)) {
                        System.out.println("l id ta3 el user hedha " + idModf);
                        User userModif = new User(idModf, username, email, newPwd, age, role);
                        System.out.println(userModif);

                        userService.updateOne(userModif);

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Succés");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText(" Vos données a été modifies avec succés.");
                        successAlert.showAndWait();
                    }else {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText(null);
                        alert.setContentText("Veuillez vérifier votre mot de passe");
                        Optional<ButtonType> result1 = alert.showAndWait();
                    }
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
    public void GoToSetting(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/UserSettings.fxml"));
        Parent root = loader.load();
        UserSettingsController lc = loader.getController();
        bBack.getScene().setRoot(root);

    }

    public void Exit(ActionEvent event) {
        System.exit(0);
    }

}
