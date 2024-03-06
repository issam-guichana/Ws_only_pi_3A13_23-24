package controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Message;
import services.Servicemessage;

public class ListmsgFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableView<Message> msg;

    @FXML
    private TableColumn<?, ?> contenu;

    @FXML
    private TableColumn<?, ?> msg_id;

    @FXML
    private ComboBox<String> room_id;

    @FXML
    void initialize() {
        assert contenu != null : "fx:id=\"contenu\" was not injected: check your FXML file 'ListmsgFXML.fxml'.";
        assert msg_id != null : "fx:id=\"msg_id\" was not injected: check your FXML file 'ListmsgFXML.fxml'.";
        assert room_id != null : "fx:id=\"room_id\" was not injected: check your FXML file 'ListmsgFXML.fxml'.";

    }

    public void affichermsg(ActionEvent actionEvent) throws SQLException {
        Refresh(new ActionEvent() );

    }

    public void Refresh(ActionEvent actionEvent) throws SQLException {
        Servicemessage sm = new Servicemessage();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");

            // Prepare statement to select id_room based on nom_room
            String selectedroom = room_id.getValue();
            preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` where `nom_room`=?");
            preparedStatement.setString(1, selectedroom);

            // Execute the query to retrieve id_room
            resultSet = preparedStatement.executeQuery();

            // Check if there is a result
            if (resultSet.next()) {
                String roomId = resultSet.getString("id_room");

                // Prepare statement to select messages based on id_room
                preparedStatement = connection.prepareStatement("SELECT `id_msg`, `contenu` FROM `message` where `room_id`=?");
                preparedStatement.setString(1, roomId);

                // Execute the query to retrieve messages
                resultSet = preparedStatement.executeQuery();

                // Create an ObservableList to hold messages
                ObservableList<Message> msglist = FXCollections.observableArrayList();

                // Map the columns to the Message object
                msg_id.setCellValueFactory(new PropertyValueFactory<>("id_msg"));
                contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));

                // Iterate through the result set and add messages to msglist
                while (resultSet.next()) {
                    Message message = new Message(resultSet.getInt("id_msg"), resultSet.getString("contenu"));
                    msglist.add(message);
                }

                // Set the items in the TableView
                msg.setItems(msglist);
            } else {
                // Handle the case where no id_room was found
                System.out.println("No room found for the selected room name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading messages", e);
        } finally {
            // Close ResultSet, PreparedStatement, and Connection in reverse order
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
            Statement statement = connection.createStatement();
            // Retrieve data from the 'formation' table
            ResultSet resultSet = statement.executeQuery("SELECT `nom_room` FROM `room`");
            ObservableList<String> roomList = FXCollections.observableArrayList();
            // Populate the ComboBox with data from the 'nom_formation' column
            while (resultSet.next()) {
                roomList.add(resultSet.getString(1));
            }
            room_id.setItems(roomList);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing AjouterroomFXML", e);
        }
    }
}


