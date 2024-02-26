package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.DBconnection;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.*;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginUserController implements Initializable {
   @FXML
    public Text passwordfalsemessage;
    private ResourceBundle resources;
    private URL location;

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button bLogin;
    @FXML
    private Hyperlink hForgotPwd;
    @FXML
    private Hyperlink hRegister;

    public static int logged ;
    private int falsepassword = 3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    PreparedStatement ps = null ;
    ResultSet rs =null;
    @FXML
    public void connect(ActionEvent event) throws Exception {


        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection cnx = DBconnection.getInstance().getCnx();
        String sql = "Select * from user where username = ? and mdp = ?";
        try{
            pst = cnx.prepareStatement(sql);
            pst.setString(1,tfUsername.getText());
            pst.setString(2,tfPassword.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                logged = rs.getInt("id_user");
                String role = rs.getString("role");

                UserService us = new UserService();
                User u = us.ChercherParUsername(tfUsername.getText());
                // controle de saisie
                if (tfUsername.getText().compareTo("") == 0 || tfPassword.getText().compareTo("") == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Un ou plusieurs champs sont manquants");
                    alert.setHeaderText("Un ou plusieurs champs sont manquants ");
                    alert.setContentText("Les champs nom d'utilisateur et mot de passe sont obligatoires !");
                    alert.showAndWait();
                    return;
                }
                 if (us.ChercherParUsername(tfUsername.getText()) == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Compte introuvable");
                    alert.setHeaderText("Votre nom d'utilisateur ou votre mot de passe sont introuvables");
                    alert.setContentText("Vérifiez vos informations ou\nMerci de remplir notre formulaire d'inscription ");
                    alert.showAndWait();
                    return;
                }
                //systeme de suspension (suspendre fi wa9t mou3ayan) tsir wa9telli l mot de passe 8alet
                 if (!tfPassword.getText().equals(u.getMdp())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Compte introuvable");
                    alert.setHeaderText("Mot de passe incorrect");
                    alert.setContentText("Mot de passe incorrect \n nombre des essais : " + falsepassword);
                    alert.showAndWait();
                    falsepassword--;
                    if (falsepassword == 0) {
                        bLogin.setDisable(true);
                        java.util.Timer chrono = new java.util.Timer();
                        chrono.schedule(new TimerTask() {
                            int time = 60;
                            @Override
                            public void run() {
                                passwordfalsemessage.setText("Compte Verrouillé , \n Réessayez dans " + time + "s");
                                time--;
                                if (time == 0) {
                                    bLogin.setDisable(false);
                                    passwordfalsemessage.setText("");
                                    chrono.cancel();
                                    falsepassword = 3;
                                }
                            }
                        }, new Date(), 1000);
                    }
                }

                //********************* kif yod5el si l'admin ***********
                            //tfUsername.getText().equals("issam") && tfPassword.getText().equals("azerty")
                else if (u.getRole().equals("ADMIN")) {
                    FXMLLoader loader = new FXMLLoader(getClass()
                            .getResource("/AdminInterface.fxml"));
                    Parent root = loader.load();
                    AdminInterfaceController rc = loader.getController();
                    bLogin.getScene().setRoot(root);
                }
                // ****************** kif yod5el si l client *********************
                else if (u.getRole().equals("CLIENT")) {
                    try {
                        // boolean test;
                        FXMLLoader loader = new FXMLLoader(getClass()
                                .getResource("/UserSettings.fxml"));
                        Parent root = loader.load();
                        UserSettingsController ctrl = loader.getController();
                        bLogin.getScene().setRoot(root);
                    } catch (IOException ex) {
                        Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // *******************kif yod5el si l formateur ***************
                else if (u.getRole().compareTo("FORMATEUR") == 0) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass()
                                .getResource("/UserSettings.fxml"));
                        Parent root = loader.load();
                        UserSettingsController ctrl = loader.getController();
                        bLogin.getScene().setRoot(root);
//                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                        alert.setTitle("Bienvenue dans votre Interface");
//                        alert.setHeaderText("Vous etes le bienvenue");
//                        alert.setContentText("Vous étes" + u.getUsername() + " " + u.getMdp());
//                        alert.showAndWait();
                    } catch (IOException ex) {
                        Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
//                else
//                JOptionPane.showMessageDialog(null,"Nom d'utilisateur ou mot de passe invalide ");
                System.out.println(logged);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    public void ForgotPwd(ActionEvent event) {
        //toooooooo doooooooooo
    }

    @FXML
    public void GoToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/RegisterUser.fxml"));
        Parent root = loader.load();
        RegisterUserController rc = loader.getController();
        hRegister.getScene().setRoot(root);
    }
    @FXML
    private void Exit(ActionEvent event){
        System.exit(0);
    }
}
