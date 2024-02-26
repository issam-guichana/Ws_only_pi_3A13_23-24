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

public class DashbordadminFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableColumn<String, String> date_c_room;
    @FXML
    private TableColumn<ObservableList<String>,String> descroom;

    @FXML
    private TableColumn<ObservableList<String>,String> formateur;

    @FXML
    private TableColumn<ObservableList<String>,String> nmform;

    @FXML
    private TableColumn<ObservableList<String>,String>nmroom;
    @FXML
    private TableView<ObservableList<String>> tabr;

    @FXML
    void initialize() {
        assert descroom != null : "fx:id=\"descroom\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert formateur != null : "fx:id=\"formateur\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert nmform != null : "fx:id=\"nmform\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert nmroom != null : "fx:id=\"nmroom\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";

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
                    .executeQuery("select r.nom_room,f.nom_form,r.date_c_room,r.description,u.username from room r Join formation f ON r.formation_id=f.id_form join user u ON f.user_id=u.id_user and u.role='formateur';");
            ObservableList<ObservableList<String>> oblist = FXCollections.observableArrayList();

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("nom_room"));
                row.add(resultSet.getString("nom_form"));
                row.add(resultSet.getString("username"));
                row.add(resultSet.getString("description"));
                //row.add(resultSet.getString("date_c_room"));
                oblist.add(row);
            }

            tabr.setItems(oblist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
// Bind columns to their respective data properties
        nmroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        nmform.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        formateur.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        descroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
    }
}
