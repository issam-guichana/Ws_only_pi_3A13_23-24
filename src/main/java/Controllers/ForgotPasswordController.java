package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import utils.DBconnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgotPasswordController {
    @FXML
    public TextField tfEmail;
    @FXML
    public Button bSenMail;
    @FXML
    public Hyperlink hBackToLogin;

    Connection cnx = DBconnection.getInstance().getCnx();
    @FXML
    public void SendMail(ActionEvent event) {
        sendPassword();
        BackToLogin(new ActionEvent());
    }
    //*************************** Mailing ***********************************
    void sendPassword(){
        System.out.println("cxcccccccccccccccccc");
        String query2="select * from user where email=? ";
        String email1="empty";
        try {
            PreparedStatement smt = cnx.prepareStatement(query2);
            smt.setString(1, tfEmail.getText());
            ResultSet rs= smt.executeQuery();
            if(rs.next()){
                email1=rs.getString("email");
                System.out.println(email1);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        sendMail(email1);
    }
    public void sendMail(String recepient){
        System.out.println("Preparing to send email");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        String myAccountEmail = "issamguichana1@gmail.com";
        String passwordd = "lzdd bvcq vcev sizn";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail,passwordd);
            }
        });
        Message message =preparedMessage(session,myAccountEmail,recepient);
        try{
            Transport.send(message);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Formini.tn :: Boite Mail");
            alert.setHeaderText(null);
            alert.setContentText("consulter votre boite mail !!");
            alert.showAndWait();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private Message preparedMessage(Session session, String myAccountEmail, String recepient){
        String query2="select * from user where email=?";
        String userEmail="null" ;
        String pass="empty";
        try {
            PreparedStatement smt = cnx.prepareStatement(query2);
            smt.setString(1, tfEmail.getText());
            ResultSet rs= smt.executeQuery();
            System.out.println(rs);
            if(rs.next()){
                pass=rs.getString("mdp");
                userEmail=rs.getString("email");                }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.print("c est en cours");
        String text="Votre mot de pass est :"+pass+"";
        String object ="Recupération de mot de passe";
        Message message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(object);
            String htmlcode ="<h1> "+text+" </h1> <h2> <b> </b2> </h2> ";
            message.setContent(htmlcode, "text/html");
            System.out.println("Message envoyeer");

            System.out.println(pass);

            return message;

        }catch(MessagingException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @FXML
    public void BackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            hBackToLogin.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
