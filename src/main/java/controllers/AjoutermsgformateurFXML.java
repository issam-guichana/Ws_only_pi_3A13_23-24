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

public class AjoutermsgformateurFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableColumn<String, String> cnmsg;


    @FXML
    private TableColumn<String, String> emet;

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
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `nom_room`, `id_room` FROM `room`");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();
            HashMap<String, Integer> roomFormationMap = new HashMap<>();
            while (resultSet.next()) {
                String nomRoom = resultSet.getString("nom_room");
                int idRoom = resultSet.getInt("id_room");
                nomformationList.add(nomRoom);
                roomFormationMap.put(nomRoom, idRoom);
            }
            selectroom.setItems(nomformationList);
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
            // You might want to show an error message to the user here
            return;
        }

        selectroom.setOnAction(event -> {
            String selectedNomFormation = selectroom.getSelectionModel().getSelectedItem();
            System.out.println(selectedNomFormation);
            if (selectedNomFormation != null) {
                try (Connection connection3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
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

                            try (Connection connection4 = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")) {
                                PreparedStatement preparedStatement4 = connection4.prepareStatement("SELECT `contenu` FROM `message` where room_id=?");
                                int roomId = roomFormationMap.get(selectedNomFormation);
                                preparedStatement4.setInt(1, roomId);
                                ResultSet resultSet4 = preparedStatement4.executeQuery();
                                ObservableList<String> Listmsg = FXCollections.observableArrayList();
                                while (resultSet4.next()) {
                                    Listmsg.add(resultSet4.getString("contenu"));
                                }
                                listmsg.setItems(Listmsg);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Handle SQL exception
                                // You might want to show an error message to the user here
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
