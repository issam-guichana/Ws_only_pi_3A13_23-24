package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import models.User;
import services.UserService;
import utils.DBconnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
//************
import org.mindrot.jbcrypt.BCrypt;
//************
import javafx.scene.image.ImageView;
import com.github.sarxos.webcam.Webcam;


public class LoginUserController implements Initializable {

    @FXML
    private ImageView imageView;
    private Webcam webcam;


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
    @FXML
    public Text passwordfalsemessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the webcam
        webcam = Webcam.getDefault();
        webcam.setViewSize(webcam.getViewSizes()[0]);

    }

    private ResourceBundle resources;
    private URL location;
    Connection cnx = DBconnection.getInstance().getCnx();

    @FXML
    public void connect(ActionEvent event) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;

//        String sql = "Select * from user where username = ? and mdp = ?";
        String sql = "Select * from user where username = ?";

        try{
            pst = cnx.prepareStatement(sql);
            pst.setString(1,tfUsername.getText());
            //pst.setString(2,tfPassword.getText());
            rs = pst.executeQuery();
            UserService us = new UserService();
            User u = us.ChercherParUsername(tfUsername.getText());

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
                alert.setHeaderText("Votre nom d'utilisateur est introuvables");
                alert.setContentText("Vérifiez vos informations ou\nMerci de remplir notre formulaire d'inscription ");
                alert.showAndWait();
                return;
            }
            if (rs.next()) {
                // controle de saisie
             if (us.ChercherParUsername(tfUsername.getText()).getStatus() == 0){
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("compte désactivé");
                 alert.setHeaderText("désolé");
                 alert.setContentText("Votre compte est désactivé, veuillez contacter l'administrateur");
                 alert.showAndWait();
                 Reset();
                 return;
             }
             //Cryptage
             String hashedPassword = rs.getString("mdp");
//****************systeme de suspension (suspendre fi wa9t mou3ayan) tsir wa9telli l mot de passe 8alet ************************
             //  if (!tfPassword.getText().equals(u.getMdp())) {
             if (!BCrypt.checkpw(tfPassword.getText(), hashedPassword)) {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Compte introuvable");
                 alert.setHeaderText("Mot de passe incorrect");
                 alert.setContentText("Mot de passe incorrect \n nombre des essais : " + falsepassword);
                 alert.showAndWait();
                 falsepassword--;
                 if (falsepassword == 0) {
                     //take photo
                     webcam.open();
                     capturePhoto();
                     closeWebcam();

                     bLogin.setDisable(true);
                     java.util.Timer chrono = new java.util.Timer();
                     chrono.schedule(new TimerTask() {
                         int time = 60;
                         @Override
                         public void run() {
                             passwordfalsemessage.setText("Compte Verrouillé  \n Réessayez dans " + time + "s");
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
                 return;
             }
//*****************************************************************************************************
                logged = rs.getInt("id_user");
                String role = rs.getString("role");

                //********************* kif yod5el si l'admin ***********
                            //tfUsername.getText().equals("issam") && tfPassword.getText().equals("azerty")
                 if (u.getRole().equals("ADMIN")) {
                    FXMLLoader loader = new FXMLLoader(getClass()
                            .getResource("/AdminMainInterface.fxml"));
                    Parent root = loader.load();
                     AdminMainInterfaceController rc = loader.getController();
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
                    } catch (IOException ex) {
                        Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println(logged);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }


    @FXML
    public void ForgotPwd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/ForgotPassword.fxml"));
        Parent root = loader.load();
        ForgotPasswordController rc = loader.getController();
        hForgotPwd.getScene().setRoot(root);
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
    private void Reset(){
        tfUsername.setText(null);
        tfPassword.setText(null);
    }

    //******************** Take photo when False password **************************
    private void capturePhoto() {
        // Capture photo from the webcam
        BufferedImage bufferedImage = webcam.getImage();
        // Get Username
        String username = tfUsername.getText() ;
        // Generate a filename based on current timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String timestamp = dateFormat.format(new Date());
        String filename = "C:/Users/issam/Desktop/pi photos/"+username+"_"+timestamp+".png";
        // Save the captured image to a file
        File output = new File(filename);
        try {
            ImageIO.write(bufferedImage, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Load the captured image into a JavaFX Image object
        Image fxImage = convertToFxImage(bufferedImage);

        // Display the captured photo in the ImageView
        imageView.setImage(fxImage);
    }
    private Image convertToFxImage(BufferedImage bufferedImage) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
            return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void closeWebcam() {
        webcam.close();
    }
}
