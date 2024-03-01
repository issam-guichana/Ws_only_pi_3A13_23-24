package controllers;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Message;
import services.Servicemessage;

import javafx.scene.image.ImageView;

public class AjoutermsgFXML  implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;




    // Image   mylogo= new Image(getClass().getResourceAsStream("./images/logo.png"));


    @FXML
    private Button btmsg;
    @FXML
    private TableColumn<String, String> cnmsg;
    @FXML
    private TableColumn<String, String> cpartic;

    @FXML
    private TableColumn<Message, String> emet;

    @FXML
    private TableView<String> listmsg;

    @FXML
    private TableView<String> listpart;

    @FXML
    private TextField msg;


    @FXML
    private ComboBox<String> selectroom;
    @FXML
    private TextField nmroom;


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

  public void initial() {

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

initial();
    }

    @FXML
    void sendmsg(ActionEvent event) {
        String selectedRoom = selectroom.getValue(); // Get the selected room
        if (selectedRoom == null) {
            // Handle case where no room is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Espace non sélectionné");
            alert.setContentText("Vous devez choisir un espace d'abord !");
            alert.show();
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?");
            preparedStatement.setString(1, selectedRoom);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int roomId = resultSet.getInt("id_room");
                Message p = new Message(msg.getText(), roomId);
                Servicemessage sp = new Servicemessage();
                sp.InsertOne(p);
                initial();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Message envoyé");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Espace non existant");
                alert.setContentText("Vous devez choisir un espace valide !");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setContentText("Erreur lors de l'envoi du message !");
            alert.show();
        }
    }
}

