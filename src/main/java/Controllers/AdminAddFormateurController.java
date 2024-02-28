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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.User;
import services.UserService;

import java.io.*;
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
    @FXML
    public JFXButton bGoToAddFormateur;
    @FXML
    public JFXButton bGoToUpdateFormateur;
    @FXML
    public ComboBox<String> cbGender;
    @FXML
    public Button UploadImg;
    @FXML
    public ImageView UserImg;
    @FXML
    public Label ImageName;
    private String Imguser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbRole.setText("FORMATEUR");
        ObservableList<String> gender = FXCollections.observableArrayList();
        cbGender.getItems().addAll("Homme","Femme");
    }

    @FXML
    public void AddFormateur(ActionEvent event) {
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
        try {
            User p = new User();
            p.setUsername(tfUsername.getText());
            p.setEmail(tfEmail.getText());
            p.setMdp(tfPassword.getText());
            p.setAge(Integer.parseInt(tfAge.getText()));
            p.setRole(cbRole.getText());
            p.setGender(cbGender.getValue());
            p.setImage(Imguser);
            p.setStatus(Integer.parseInt("1"));
            UserService sp = new UserService();
            sp.insertOne(p);

            //information alert "added"
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("succès");
            alert.setContentText("formateur ajouté avec succès");
            alert.showAndWait();

            System.out.println("FORMATEUR ADDED");
            GoToAddFormateur(new ActionEvent());
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
    @FXML
    public void UploadImage(ActionEvent event) throws FileNotFoundException, IOException {
        FileChooser fc = new FileChooser();
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", listFichier));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            //Commentaire.setText("Image selectionnée" + f.getAbsolutePath());
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(new File(f.getAbsolutePath()));
                os = new FileOutputStream(new File("D:/progrms/xamp/htdocs/PIDEV IMG/profil/" + f.getName()));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                is.close();
                os.close();
            }
            File file = new File("D:/progrms/xamp/htdocs/PIDEV IMG/profil/" + f.getName());
            System.out.println(file.toURI());
            UserImg.setImage(new Image(file.toURI().toString()));
            Imguser = f.getName();
            System.out.println(Imguser);
            ImageName.setText(Imguser);
        } else if (f == null) {
            //Commentaire.setText("Erreur chargement de l'image");
            //Allert
            System.out.println("Erreur !");
        }
    }

    @FXML
    public void Reset(ActionEvent event) {
        tfUsername.setText("");
        tfEmail.setText("");
        tfPassword.setText("");
        tfAge.setText("");
        cbGender.setValue("");
        UserImg.setImage(null);
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
    public void GoToUpdateFormateur(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminUpdateFormateur.fxml"));
        Parent root = loader.load();
        AdminUpdateFormateurController lc = loader.getController();
        bGoToUpdateFormateur.getScene().setRoot(root);
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
