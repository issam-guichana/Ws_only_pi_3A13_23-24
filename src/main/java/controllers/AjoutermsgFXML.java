package controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import models.Message;
import services.Servicemessage;

import javax.swing.*;
import javafx.scene.image.ImageView;

public class AjoutermsgFXML  implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ImageView chaticon;

    @FXML
    private ImageView formicon;

    @FXML
    private ImageView homeicon;

    @FXML
    private ImageView logofx;
    // Image   mylogo= new Image(getClass().getResourceAsStream("./images/logo.png"));
    @FXML
    private ListView<String> listmsg;
    @FXML
    private ComboBox<String> roomid;
    @FXML
    private Button btmsg;

    @FXML
    private ListView<String> listpart;

    @FXML
    private AnchorPane listpartc;

    @FXML
    private TextField msg;


    @FXML
    private TextField nmroom;

    @FXML
    void ajoutermsg(ActionEvent event) {

        // Initialize selectedRoom with an empty string
        String selectedRoom = "";
        try {
            // Get the selected room from the ComboBox
            selectedRoom = roomid.getValue();

            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");

            // Prepare statement to retrieve the id of the selected room
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?");
            preparedStatement.setString(1, selectedRoom);

            // Execute the query to retrieve the id of the selected room
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set is not empty
            if (resultSet.next()) {
                // Retrieve the id of the selected room
                int roomId = resultSet.getInt("id_room");

                // Create a message object with the message text and the id of the selected room
                Message p = new Message(msg.getText(), roomId);

                // Insert the message into the database
                Servicemessage sp = new Servicemessage();
                sp.InsertOne(p);
            } else {
                // Handle the case where no room is selected
                // You can show an alert or perform other error handling here
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQL or Number Format exception
            // You can show an alert or perform other error handling here
        }

    }


    //@FXML
    //void initialize() {
    //  assert btmsg != null : "fx:id=\"btmsg\" was not injected: check your FXML file 'AjoutermsgFXML.fxml'.";
    //   assert msg != null : "fx:id=\"msg\" was not injected: check your FXML file 'AjoutermsgFXML.fxml'.";
    // assert nmroom != null : "fx:id=\"nmroom\" was not injected: check your FXML file 'AjoutermsgFXML.fxml'.";
    // Image chatImage = new Image("../../images/chat-a-bulles.png");
    //  Image formImage = new Image("path/to/your/form_icon.png");
    //Image homeImage = new Image("path/to/your/home_icon.png");

    // Set images to ImageViews
    //chaticon.setImage(chatImage);
    //formicon.setImage(formImage);
    //homeicon.setImage(homeImage);
    //}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //  logofx.setImage(mylogo);
        // Image chatImage = new Image(getClass().getResourceAsStream("@../images/chat-a-bulles.png"));
        //chaticon.setImage(chatImage);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");

            // Create a statement for executing SQL queries
            Statement statement = connection.createStatement();

            // Retrieve data from the 'user' table
            ResultSet resultSet = statement.executeQuery("SELECT `username`,`role` FROM `user`");
            ObservableList<String> Listparti = FXCollections.observableArrayList();
            while (resultSet.next()) {
                // Add username and role to the list
                Listparti.add(resultSet.getString(1));
                Listparti.add(resultSet.getString(2));
            }
            // Set items in the ListView for participants
            listpart.setItems(Listparti);

            // Retrieve data from the 'formation' table
            ResultSet resultSet3 = statement.executeQuery("SELECT `nom_room` FROM `room`");
            ObservableList<String> nomroomList = FXCollections.observableArrayList();
            // Populate the ComboBox with data from the 'nom_room' column
            while (resultSet3.next()) {
                nomroomList.add(resultSet3.getString(1));
            }
            // Set items in the ComboBox for room IDs
            roomid.setItems(nomroomList);

            // Get the selected room
            String selectedRoom = roomid.getValue();

            // Prepare statement to select messages based on room_id
            // preparedStatement = connection.prepareStatement();
            //  preparedStatement.setString(1, selectedRoom);

            // Retrieve data from the 'message' table
            ResultSet resultSet2 = statement.executeQuery("SELECT `contenu` FROM `message` ;");

            ObservableList<String> Listmsg = FXCollections.observableArrayList();
            while (resultSet2.next()) {
                // Add message content to the list
                Listmsg.add(resultSet2.getString(1));
            }
            // Set items in the ListView for messages
            listmsg.setItems(Listmsg);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving data from the database", e);
        } finally {
            // Close the connection and prepared statement in the finally block to ensure they get closed even if an exception occurs
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
