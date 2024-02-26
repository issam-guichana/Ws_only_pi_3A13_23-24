package controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Room;
import services.Serviceroom;

public class AjouterroomFXML {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> formation;

    @FXML
    private TextField nomroom;




    @FXML
    void ajouterroom(ActionEvent event) throws SQLException {

        try {


            try
                    (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
                String selectedNomForm = formation.getValue();

                // Prepare a statement to retrieve the id_form corresponding to the selected nom_form
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_form` FROM `formation` WHERE `nom_form`=?");
                preparedStatement.setString(1, selectedNomForm);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if a result is found
                if (resultSet.next()) {
                    int idForm = resultSet.getInt("id_form");

                    // Check if nomroom is not null
                    if (nomroom.getText() != null && !nomroom.getText().isEmpty()) {
                        // Create the Room object with the retrieved id_form
                        Room r = new Room(nomroom.getText(),idForm);

                        // Call the service to insert the room
                        Serviceroom sp = new Serviceroom();
                        sp.InsertOne(r);
                    } else {
                        System.out.println("Error: nom_room cannot be null or empty.");
                    }
                } else {
                    // Handle case where no result is found for the selected nom_form
                    System.out.println("No id_form found for the selected nom_form: " + selectedNomForm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {

        }
    }

    //   void initialize () {


//
    //          assert formation != null : "fx:id=\"formation\" was not injected: check your FXML file 'AjouterroomFXML.fxml'.";
    //        assert nomroom != null : "fx:id=\"nomroom\" was not injected: check your FXML file 'AjouterroomFXML.fxml'.";

    //   }


    public void initialize() {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
            Statement statement = connection.createStatement();
            // Retrieve data from the 'formation' table
            ResultSet resultSet = statement.executeQuery("SELECT `nom_form` FROM `formation`");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();
            // Populate the ComboBox with data from the 'nom_formation' column
            while (resultSet.next()) {
                nomformationList.add(resultSet.getString(1));
            }
            formation.setItems(nomformationList);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing AjouterroomFXML", e);
        }
    }
}




