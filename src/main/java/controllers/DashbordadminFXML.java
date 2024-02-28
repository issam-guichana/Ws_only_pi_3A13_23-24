package controllers;
import java.awt.*;
import java.awt.Label;
import java.awt.TextField;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import models.Room;
import services.Serviceroom;

public class DashbordadminFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableColumn<ObservableList<String>, Void> button_room;
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
    private TextField descriptionroom;

    @FXML
    private TextField nomroom;

    @FXML
    private ComboBox<String> nomform;
    @FXML
    private Pane modifyRoomPane;
    private Label nomRoomField;
    private Label descriptionField;

    private  TableColumn<ObservableList<String> , Void>setupModifierButtonColumn() {
        // Create the button column
        TableColumn<ObservableList<String>, Void> modifierColumn = new TableColumn<>("Modifier");

        // Set the cell factory to create buttons for each cell in the column
        modifierColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ObservableList<String>, Void> call(TableColumn<ObservableList<String>, Void> param) {
                return new TableCell<>() {
                    private final Button modifyButton = new Button("Modifier");


                    {
                        modifyButton.setOnAction(event -> {
                            // Get the row index
                            int index = getIndex();

                            // Handle the button click event
                            System.out.println("Modifier clicked for row: " + index);

                            // Assuming you have the necessary data to populate the modification form
                            String nomRoom = tabr.getItems().get(index).get(0); // Get the room name
                            String description = tabr.getItems().get(index).get(3); // Get the room description

                            // Show the modification form
                            //  showModifyRoomForm(nomRoom, description);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                        }
                    }
                };
            }
        });

          return modifierColumn;
    }
    @FXML
    private void showModifyRoomForm(String nomRoom, String description) {
        // Populate the modification form fields
        // Assuming you have TextField fields named "nomRoomField" and "descriptionField"


        nomRoomField.setText(nomRoom);
        descriptionField.setText(description);

        // Show the modification pane
        modifyRoomPane.setVisible(true);
    }

    private TableColumn<ObservableList<String>, Void> setupDeleteButtonColumn() {
        TableColumn<ObservableList<String>, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    int index = getIndex();
                    // Get the inner ObservableList<String> corresponding to the selected row
                    ObservableList<String> row = getTableView().getItems().get(index);
                    // Retrieve the id_room from the inner list
                    String idRoomString = row.get(0);
                    System.out.println(idRoomString);
                    int idRoom = Integer.parseInt(idRoomString);
                    // Action to perform when the "Supprimer" button is clicked
                    Serviceroom sq = new Serviceroom();
                    try{
//int idRoom = Integer.parseInt(rowData.get(0));
                        sq.DeleteOne(idRoom);
                        // Refresh the table view after deletion if needed
                        //displayAllRoomsInTableView(); // Define this method to update the table view
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        return supprimerColumn;
    }

    @FXML
    void initialize() {
        assert descroom != null : "fx:id=\"descroom\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert formateur != null : "fx:id=\"formateur\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert nmform != null : "fx:id=\"nmform\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert nmroom != null : "fx:id=\"nmroom\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";

    }
    @FXML
    void ajoutroom(ActionEvent event) throws SQLException {


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try
        (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "")){
            Statement statement = connection.createStatement();
            // Retrieve data from the 'formation' table
            ResultSet resultSet = statement.executeQuery("SELECT `nom_form` FROM `formation`");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();
            // Populate the ComboBox with data from the 'nom_formation' column
            while (resultSet.next()) {
                nomformationList.add(resultSet.getString(1));
            }
            nomform.setItems(nomformationList); // Set items to ComboBox

        } catch (SQLException e) {
            throw new RuntimeException("Error initializing AjouterroomFXML", e);
        }


        Serviceroom sm = new Serviceroom();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");
            Statement statement = connection.createStatement();

            // Retrieve data from the 'user' table
            ResultSet resultSet = statement
                    .executeQuery("select r.id_room,r.nom_room,f.nom_form,r.date_c_room,r.description,u.username from room r Join formation f ON r.formation_id=f.id_form join user u ON f.user_id=u.id_user and u.role='formateur';");
            ObservableList<ObservableList<String>> oblist = FXCollections.observableArrayList();

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("id_room"));
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

        TableColumn<ObservableList<String>, Void> modifierColumn = setupModifierButtonColumn();
        tabr.getColumns().add(modifierColumn);
        TableColumn<ObservableList<String>, Void> deleteColumn = setupDeleteButtonColumn();
        tabr.getColumns().add(deleteColumn);

    }


}
