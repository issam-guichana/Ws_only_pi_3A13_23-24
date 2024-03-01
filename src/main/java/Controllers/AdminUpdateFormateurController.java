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
import java.util.List;
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
    public ComboBox<String> cbFormateur;
    @FXML
    public Button btn_Logout;
    @FXML
    public Button btn_Update_Formateur;
    @FXML
    public Button btn_Add_Formateur;
    @FXML
    public Button btn_Afficher_users;

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

    @FXML
    public void Update(ActionEvent event) {
        //controle de saisie
        // Check if any required field is empty
        if (tfUsername.getText().isEmpty() || tfEmail.getText().isEmpty() ||  tfAge.getText().isEmpty() || isDuplicate(tfUsername.getText())) {
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
        String sql = "Select * from user where username = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, cbFormateur.getValue());
            rs = pst.executeQuery();
            String forModf = cbFormateur.getValue();


            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous vraiment modifier les données de cette formateur?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    int id =userService.ChercherParUsername(forModf).getId_user();
                    String role =userService.ChercherParUsername(forModf).getRole();
                    String gender=userService.ChercherParUsername(forModf).getGender();
                    String image =userService.ChercherParUsername(forModf).getImage();
                    int status =userService.ChercherParUsername(forModf).getStatus();
                    String username = tfUsername.getText();
                    String email = tfEmail.getText();
                    int age = Integer.parseInt(tfAge.getText());
                    String pwd = tfNewPassword.getText();
                    String Cpwd = tfCfPassword.getText();

                    System.out.println("l id ta3 el user hedha "+forModf);
                    User userModif = new User(id,username,email,pwd,age,role,gender,image,status);
                    System.out.println(userModif);

                    userService.updateOne(userModif);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succés");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText(" Vos données a été modifiées avec succés.");
                    successAlert.showAndWait();

                    Reset(new ActionEvent());
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
    private boolean isDuplicate(String nom) {
        try {
            UserService userService = new UserService();
            List<User> existingUSER = userService.selectAll() ;// Assuming this method exists to get all badges

            for (User user : existingUSER) {
                if (user.getUsername().equalsIgnoreCase(nom)) {
                    // Display a warning dialog
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Nom d'utilisateur déjà existant");
                    alert.setContentText("Le nom utilisateur existe déjà. Veuillez entrer un nom utilisateur différent.");
                    alert.showAndWait();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
        return false;
    }
    @FXML
    public void Reset(ActionEvent event) {
        tfUsername.setText("");
        tfEmail.setText("");
        tfAge.setText("");
        tfNewPassword.setText("");
        tfCfPassword.setText("");
        cbFormateur.setValue("");
    }
    @FXML
    public void Add_Formateur(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminAddFormateur.fxml"));
        Parent root = loader.load();
        AdminAddFormateurController lc = loader.getController();
        btn_Add_Formateur.getScene().setRoot(root);
    }

    @FXML
    public void Update_Formateur(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminUpdateFormateur.fxml"));
        Parent root = loader.load();
        AdminUpdateFormateurController lc = loader.getController();
        btn_Update_Formateur.getScene().setRoot(root);
    }
    @FXML
    public void Display_Users(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminInterface.fxml"));
        Parent root = loader.load();
        AdminInterfaceController lc = loader.getController();
        btn_Afficher_users.getScene().setRoot(root);
    }

    @FXML
    public void Lougout(ActionEvent event) {
        try {
            //taawed thezzek lel inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            btn_Logout.getScene().setRoot(root);

        } catch (IOException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
