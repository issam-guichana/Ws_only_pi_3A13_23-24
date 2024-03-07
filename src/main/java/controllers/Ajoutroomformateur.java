package controllers;

import java.net.PasswordAuthentication;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Room;
import models.user_formation;
import services.Serviceroom;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Ajoutroomformateur implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField desc;

    @FXML
    private TextField namesapce;

    @FXML
    private ComboBox<String> nmform;


    @FXML
    void ajoutroom(ActionEvent event) throws SQLException {
        try {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")) {
                String selectedNomForm = nmform.getValue();

                // Prepare a statement to retrieve the id_form corresponding to the selected nom_form
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_form` FROM `formation` WHERE `nom_form`=?");
                preparedStatement.setString(1, selectedNomForm);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if a result is found
                if (resultSet.next()) {
                    int idForm = resultSet.getInt("id_form");

                    // Check if nomroom is not null
                    if (namesapce.getText() != null && !namesapce.getText().isEmpty() && desc.getText() != null) {
                        // Check if the room name already exists for the selected formation_id
                        PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM room WHERE nom_room=? and status='Active' ");
                        checkStatement.setString(1, namesapce.getText());
                      //  checkStatement.setInt(2, idForm);
                        ResultSet checkResult = checkStatement.executeQuery();

                        if (checkResult.next()) {
                            int count = checkResult.getInt("count");
                            if (count == 0) {
                                // Create the Room object with the retrieved id_form
                                Room r = new Room(namesapce.getText(), desc.getText());
                                user_formation uf =new user_formation(idForm,r.getId_room());
                                // Call the service to insert the room
                                Serviceroom sp = new Serviceroom();
                                sp.InsertOne(r,uf);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                                alert.setTitle("Espace crée !");
                                alert.setContentText("Vous pouvez maintenant communiquer avec vos étudiants");
                                alert.show();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur de controle de saisie");
                                alert.setContentText("Le nom de la salle existe déjà pour cette formation.");
                                alert.show();
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de saisie");
                        alert.setContentText("Le nom de la salle ne peut pas être vide.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de saisie");
                    alert.setContentText("Veuillez choisir une formation appartenant à un esace");
                    alert.show();
                    // Handle case where no result is found for the selected nom_form
                    System.out.println("No id_form found for the selected nom_form: " + selectedNomForm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            // Add any cleanup code if needed
        }
    }


   // public void sendmailroom() {
     //   String recipientEmail = GlobalVariables.userEmail;

        // Check if recipientEmail is not null before using it
       // if (recipientEmail != null) {
       
            //Properties props = new Properties();
            //props.put("mail.smtp.host", "smtp.office365.com");
            //props.put("mail.smtp.port", "587");
            //props.put("mail.smtp.auth", "true");
           // props.put("mail.smtp.starttls.enable", "true");

            //try {
              //  final String finalPassword = password;
                //Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                  //  protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    //    return new PasswordAuthentication(senderEmail, finalPassword.toCharArray());
                    //}
                //});
                //javax.mail.Message message = new MimeMessage(session);
                //message.setFrom(new InternetAddress(senderEmail));

                // Create InternetAddress object only if recipientEmail is not null
                //InternetAddress recipientAddress = new InternetAddress(recipientEmail);
                //message.setRecipient(javax.mail.Message.RecipientType.TO, recipientAddress);

                //message.setSubject("Course purchase success!");
                //message.setText("Dear User,\n\nThis is to inform you that your course purchase is successful.\n\nBest regards,\nEduHub Connect");

                //Transport.send(message);

                //System.out.println("Course purchase email sent successfully to " + recipientEmail);
            //} catch (MessagingException e) {
              //  System.err.println("Failed to send email: " + e.getMessage());
                //e.printStackTrace();
            //}
        //} else {
          //  System.out.println("Recipient email is null.");
        //}
    //}




    @FXML
    void initialize() {
        assert desc != null : "fx:id=\"desc\" was not injected: check your FXML file 'Ajoutroomformateur.fxml'.";
        assert namesapce != null : "fx:id=\"namesapce\" was not injected: check your FXML file 'Ajoutroomformateur.fxml'.";
        assert nmform != null : "fx:id=\"nmform\" was not injected: check your FXML file 'Ajoutroomformateur.fxml'.";

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
                (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")){
            Statement statement = connection.createStatement();
            // Retrieve data from the 'formation' table
            ResultSet resultSet = statement.executeQuery("SELECT `nom_form` FROM `formation`");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();
            // Populate the ComboBox with data from the 'nom_formation' column
            while (resultSet.next()) {
                nomformationList.add(resultSet.getString(1));
            }
            nmform.setItems(nomformationList); // Set items to ComboBox

        } catch (SQLException e) {
            throw new RuntimeException("Error initializing AjouterroomFXML", e);
        }




    }
}
