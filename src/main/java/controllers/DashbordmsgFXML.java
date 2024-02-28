package controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import services.Serviceroom;

public class DashbordmsgFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<ObservableList<String>,String> cnmsg;

    @FXML
    private TableColumn<ObservableList<String>,String> emet;

    @FXML
    private TableColumn<ObservableList<String>,String> nmroom;

    @FXML
    private TableView<ObservableList<String>> tabm;


    @FXML
    void initialize() {
        assert cnmsg != null : "fx:id=\"cnmsg\" was not injected: check your FXML file 'DashbordmsgFXML.fxml'.";
        assert emet != null : "fx:id=\"emet\" was not injected: check your FXML file 'DashbordmsgFXML.fxml'.";
        assert nmroom != null : "fx:id=\"nmroom\" was not injected: check your FXML file 'DashbordmsgFXML.fxml'.";

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Serviceroom sm = new Serviceroom();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");
            Statement statement = connection.createStatement();

            // Retrieve data from the 'user' table
            ResultSet resultSet = statement
                    .executeQuery("select m.contenu,r.nom_room,u.username From message m Join room r ON m.room_id=r.id_room and m.status='Active' Join formation f ON f.id_form=r.formation_id join user u on f.user_id=u.id_user;");
            ObservableList<ObservableList<String>> oblist = FXCollections.observableArrayList();

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("contenu"));
                row.add(resultSet.getString("nom_room"));
                row.add(resultSet.getString("username"));

                oblist.add(row);
            }

            tabm.setItems(oblist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
// Bind columns to their respective data properties
        cnmsg.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));

        nmroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        emet.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));


    }
}
