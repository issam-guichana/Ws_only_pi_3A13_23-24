package controllers;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Message;
import services.Servicemessage;

public class AjoutermsgformateurFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableColumn<String, String> cnmsg;


    @FXML
    private TableColumn<Message, String> emet;

    @FXML
    private TableView<String> listmsg;
    //@FXML
    // private ListView<String> listmsg;

    @FXML
    private ComboBox<String> selectroom;

    //  @FXML
    //private ListView<String> listpart;

    @FXML
    private ListView<?> nom_room;

    @FXML
    private TextField sendmsg;

    @FXML
    private TableColumn<String, String> cpartic;
    @FXML
    private TableView<String> listpart;
    private HashMap<String, Integer> roomFormationMap = new HashMap<>();

    @FXML
    public void sendmsg(javafx.event.ActionEvent actionEvent) {
        String selectedRoom = "";
        try {
            // Get the selected room from the ComboBox
            //selectedRoom = roomid.getValue();
            selectedRoom = selectroom.getValue();
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

                if (!sendmsg.getText().trim().isEmpty()){
                int roomId = resultSet.getInt("id_room");

                // Create a message object with the message text and the id of the selected room
                Message p = new Message(sendmsg.getText(), roomId);
                System.out.println(sendmsg.getText());
                System.out.println(roomId);

                // Insert the message into the database
                Servicemessage sp = new Servicemessage();
                sp.InsertOne(p);}
                else  {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message vide ");
                    alert.setContentText("Vous ne pouvez pas envoyer un message vide ");
                    alert.show();
                }

            } else {

            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQL or Number Format exception
            // You can show an alert or perform other error handling here
        }

    }



    @FXML
    void initialize() {
        assert listmsg != null : "fx:id=\"listmsg\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert listpart != null : "fx:id=\"listpart\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert nom_room != null : "fx:id=\"nom_room\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert sendmsg != null : "fx:id=\"sendmsg\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        HashMap<String, Integer> roomFormationMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `nom_room`, `id_room` FROM `room`");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String nomRoom = resultSet.getString("nom_room");
                int idRoom = resultSet.getInt("id_room");
                nomformationList.add(nomRoom);
                roomFormationMap.put(nomRoom, idRoom); // Store the room ID along with the room name
            }

            selectroom.setItems(nomformationList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
            // You might want to show an error message to the user here
            return;
        }

        selectroom.setOnAction(event -> {
            String selectedNomFormation = selectroom.getSelectionModel().getSelectedItem();
            System.out.println(selectedNomFormation);
            if (selectedNomFormation != null) {
                try (Connection connection3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
                    // Query to retrieve the formation_id based on the selected nom_room
                    PreparedStatement preparedStatement3 = connection3.prepareStatement("SELECT formation_id FROM room WHERE nom_room = ?");
                    preparedStatement3.setString(1, selectedNomFormation);
                    ResultSet resultSet3 = preparedStatement3.executeQuery();
                    if (resultSet3.next()) {
                        int formationId = resultSet3.getInt("formation_id");
                        System.out.println("Formation id found for selected room: " + formationId);
                        try (Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
                            PreparedStatement preparedStatement2 = connection2.prepareStatement("SELECT u.username FROM user u JOIN formation f ON u.id_user = f.user_id WHERE f.id_form = ?");
                            preparedStatement2.setInt(1, formationId);
                            ResultSet resultSet2 = preparedStatement2.executeQuery();
                            ObservableList<String> Listparti = FXCollections.observableArrayList();
                            while (resultSet2.next()) {
                                Listparti.add(resultSet2.getString("username"));
                            }
                            System.out.println(Listparti);
                            cpartic.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
                            listpart.setItems(Listparti);

                            // Query to retrieve the id_room based on the selected nom_room
                            if (roomFormationMap.containsKey(selectedNomFormation)) {
                                int roomId = roomFormationMap.get(selectedNomFormation);
                                System.out.println(roomId);
                                try (PreparedStatement preparedStatement4 = connection3.prepareStatement("SELECT `contenu`, `sender_msg` FROM `message` WHERE room_id=? and status='Active'")) {
                                    preparedStatement4.setInt(1, roomId);
                                    ResultSet resultSet4 = preparedStatement4.executeQuery();
                                    //ObservableList<Message> messageList = FXCollections.observableArrayList();
                                    //while (resultSet4.next()) {
                                      //  String cnmsgValue = resultSet4.getString("contenu");
                                        //String emetValue = resultSet4.getString("sender_msg");
                                       // messageList.add(new Message(cnmsgValue, emetValue));
                                        //System.out.println(messageList);
                                    //}

                                   // ObservableList<Message> contenuList = FXCollections.observableArrayList();
                                    //while (resultSet4.next()) {
                                       // String cnmsgValue = resultSet4.getString("contenu");
                                       // String emetteurValue = resultSet4.getString("sender_msg");
                                        //contenuList.add(new Message(cnmsgValue, emetteurValue)); // Assuming Message constructor takes contenu and sender as parameters
                                   // }

// Set the cell value factories to extract values from the Message object
                                    //cnmsg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContenu()));
                                    //emet.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSender()));
                                    ObservableList<String> contenuList = FXCollections.observableArrayList();
                                    while (resultSet4.next()) {
                                        String cnmsgValue = resultSet4.getString("contenu");
                                        String emetteurValue = resultSet4.getString("sender_msg");
                                        contenuList.add(cnmsgValue);
                                    }
                                    // Set the cell value factories to extract values from the Message object
                                    // cnmsg.setCellValueFactory(new PropertyValueFactory<>("contenu"));
                                    //emet.setCellValueFactory(new PropertyValueFactory<>("emet"));

                                    // Set the items in the TableView
                                    cnmsg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
                                    //System.out.println(contenuList);
                                    listmsg.setItems(contenuList);
                                   // listmsg.setItems(messageList);
                                }
                            } else {
                                System.out.println("No message found for selected room: " + selectedNomFormation);
                                // Handle the situation where the room ID is not found
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle SQL exception
                            // You might want to show an error message to the user here
                        }
                    } else {
                        System.out.println("No formation found for selected room: " + selectedNomFormation);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle SQL exception
                    // You might want to show an error message to the user here
                }
            }
        });
    }



}