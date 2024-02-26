package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
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

public class UpdateProfileController {
    @FXML
    public Button bBack;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfAge;

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
    public void Update(ActionEvent event) {
        //controle de saisie
        // Check if any required field is empty
        if (tfUsername.getText().isEmpty() || tfEmail.getText().isEmpty() ||  tfAge.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tout les champs.");
            return;
        }
        if (tfUsername.getLength()<6){
            showAlert("Erreur", "Votre Nom d'utilisateur doit contenir au moins 6 caractères");
            return;
        }
        if (!isValidEmail(tfEmail.getText())) {
            showAlert("Erreur", "Entrer un Adress Email Valide\n Exemple : foulen@esprit.tn");
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
                alert.setContentText("Voulez-vous vraiment modifier vos données ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    String username = tfUsername.getText();
                    String email = tfEmail.getText();
                    int age = Integer.parseInt(tfAge.getText());
                    String pwd = userService.ChercherParId(idModf).getMdp();
                    String role =userService.ChercherParId(idModf).getRole();

                    //User u = new User(idModf,username,email,age);
                    //UserService us = new UserService();
                    System.out.println("l id ta3 el user hedha "+idModf);
                    User userModif = new User(idModf,username,email,pwd,age,role);
                    System.out.println(userModif);

                    userService.updateOne(userModif);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succés");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText(" Vos données a été modifiées avec succés.");
                    successAlert.showAndWait();
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void Reset(ActionEvent event) {
        tfUsername.setText("");
        tfEmail.setText("");
        tfAge.setText("");
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
