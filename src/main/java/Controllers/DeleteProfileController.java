package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class DeleteProfileController {
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
    

    PreparedStatement pst = null;
    ResultSet rs = null;
    @FXML
    public void DeleteAcc(ActionEvent event) {
        UserService userService= new UserService();
        Connection cnx = DBconnection.getInstance().getCnx();
        String sql = "Select * from user where id_user = ?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, LoginUserController.logged + "");
            rs = pst.executeQuery();
                int idSupp = LoginUserController.logged ;
            if (rs.next()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous vraiment supprimer l'utilisateur " + idSupp  + " ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    userService.SupprimerUser(idSupp);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succ�s");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText(" l'utilisateur " + idSupp + " a été supprimées avec succés.");
                    successAlert.showAndWait();
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    @FXML
    public void DesactivateAcc(ActionEvent event) {
        UserService userService= new UserService();
        Connection cnx = DBconnection.getInstance().getCnx();
        String sql = "Select * from user where id_user = ?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, LoginUserController.logged + "");
            rs = pst.executeQuery();
            int idSupp = LoginUserController.logged ;


            if (rs.next()) {
                User user = new User();
                user.setId_user(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setMdp(rs.getString("mdp"));
                user.setAge(rs.getInt("age"));
                user.setRole(rs.getString("role"));
                user.setGender(rs.getString("gender"));
                user.setImage(rs.getString("image"));


                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous vraiment désactiver votre compte?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {

                    int currentStatus = Integer.parseInt(String.valueOf(user.getStatus()));
                    int newStatus = (currentStatus == 1) ? 0 : 1;
                    user.setStatus(newStatus);
                    userService.updateStatus(user);
                    LogOut(new ActionEvent());

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succ�s");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Votre compte a �t� d�sactiv�s avec succ�s.");
                    successAlert.showAndWait();
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void GoToSetting(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/UserSettings.fxml"));
        Parent root = loader.load();
        UserSettingsController lc = loader.getController();
        bBack.getScene().setRoot(root);
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