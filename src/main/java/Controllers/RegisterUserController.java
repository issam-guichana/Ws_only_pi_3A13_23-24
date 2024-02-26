package Controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
        //controle de saisie
        // Check if any required field is empty
        if (tfUsername.getText().isEmpty() || tfEmail.getText().isEmpty() || tfPassword.getText().isEmpty() || tfAge.getText().isEmpty()) {
            showAlert("Error", "Veuillez remplir tout les champs.");
            return;
        }
        if (tfUsername.getLength()<6){
            showAlert("Error", "Votre Nom d'utilisateur doit contenir au moins 6 caractères");
            return;
        }
        if (!isValidEmail(tfEmail.getText())) {
            showAlert("Error", "Entrer un Adress Email Valide\n Exemple : foulen@esprit.tn");
            return;
        }
        if (tfPassword.getLength()<6){
            showAlert("Error", "Votre mot de passe doit contenir au moins 6 caractères");
            return;
        }
//        else if (!tfPassword.getText().equals(Cpassword)) {
//            showAlert("Error", "les 2 password son't faux");
//            return;
//        }

        // Validate phone number
//        int phone ;
//        try {
//            phone = Integer.parseInt(phoneText);
//        } catch (NumberFormatException e) {
//            showAlert("Error", "Entrer un Numéro Télephone Valide");
//            return;
//        }
        // Validate email format

        try {
            User p = new User(tfUsername.getText(), tfEmail.getText(),tfPassword.getText(), Integer.parseInt(tfAge.getText()),cbRole.getText());
            UserService sp = new UserService();
            sp.insertOne(p);

            //information alert "added"
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bienvenue");
            alert.setContentText("Bienvenue Dans Formini.tn");
            alert.showAndWait();

            System.out.println("CLIENT ADDED ");
            Reset(new ActionEvent());
            BackToLogin(new ActionEvent());
        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

//    public void initialize() {
//        initializeComboBoxContent();
//    }

//    private void redirectToLogin() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login_User.fxml"));
//        Parent loginSuccessRoot = loader.load();
//        Scene scene = id_Password.getScene(); // Get the scene from any node in the current scene
//        scene.setRoot(loginSuccessRoot);
//    }

//    private void initializeComboBoxContent() {
//        // Initialize  choices
//        id_Role.getItems().addAll("Member", "Artist"); // Example Role choices
//        id_Gender.getItems().addAll("Homme", "Femme", "Autre"); // Example gender choices
//    }