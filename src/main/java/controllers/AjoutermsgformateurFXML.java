package controllers;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AjoutermsgformateurFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<?> listmsg;

    @FXML
    private ListView<String> listpart;

    @FXML
    private ListView<?> nom_room;

    @FXML
    private TextField sendmsg;


    @FXML
    void sendmsg(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert listmsg != null : "fx:id=\"listmsg\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert listpart != null : "fx:id=\"listpart\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert nom_room != null : "fx:id=\"nom_room\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert sendmsg != null : "fx:id=\"sendmsg\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");

            // Create a statement for executing SQL queries
            Statement statement = connection.createStatement();

            // Retrieve data from the 'user' table
            ResultSet resultSet = statement.executeQuery("select u.username  from user u JOIN formation f  ON u.id_user = f.user_id  JOIN room r ON f.id_form= r.formation_id");
            ObservableList<String> Listparti = FXCollections.observableArrayList();
            while (resultSet.next()) {
                // Add username and role to the list
                Listparti.add(resultSet.getString(1));

            }
            // Set items in the ListView for participants
            listpart.setItems(Listparti);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving data from the database", e);

        }
    }
}