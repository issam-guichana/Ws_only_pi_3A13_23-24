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
import models.User;
import services.UserService;
import utils.DBconnection;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminUpdateFormateurController implements Initializable {
    @FXML
    public TextField tfUsername;
    @FXML
    public TextField tfEmail;
    @FXML
    public TextField tfAge;
    @FXML
    public PasswordField tfNewPassword;
    @FXML
    public PasswordField tfCfPassword;
    @FXML
    public JFXButton bGoToAddFormateur;
    @FXML
    public JFXButton bGoToUpdateFormateur;
    @FXML
    public Button bLogout;
    @FXML
    public Button bBack;
    @FXML
    public ComboBox<String> cbFormateur;

    PreparedStatement pst = null;
    ResultSet rs = null;
    ObservableList <String> nomFormateur = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection cnx = DBconnection.getInstance().getCnx();
        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username FROM user WHERE role = 'FORMATEUR' ");
            while (resultSet.next()) {
                nomFormateur.add(resultSet.getString(1));
            }
            cbFormateur.setItems(nomFormateur);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //////////////////////////////ne9sa 5edma update
    @FXML
    public void Update(ActionEvent event) {
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
                alert.setContentText("Voulez-vous vraiment modifier les données de cette formateur?");
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
    @FXML
    public void Reset(ActionEvent event) {
        tfUsername.setText("");
        tfEmail.setText("");
        tfAge.setText("");
        tfNewPassword.setText("");
        tfCfPassword.setText("");
    }
    @FXML
    public void GoToAddFormateur(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminAddFormateur.fxml"));
        Parent root = loader.load();
        AdminAddFormateurController lc = loader.getController();
        bGoToAddFormateur.getScene().setRoot(root);
    }
    @FXML
    public void GoToUpdateFormateur(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminUpdateFormateur.fxml"));
        Parent root = loader.load();
        AdminUpdateFormateurController lc = loader.getController();
        bGoToUpdateFormateur.getScene().setRoot(root);
    }
    @FXML
    public void BackToAdminI(ActionEvent event) throws IOException{
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


}
