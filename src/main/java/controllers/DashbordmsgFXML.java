package controllers;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import services.Servicemessage;
import services.Serviceroom;

public class DashbordmsgFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableColumn<ObservableList<String>, String> date_msg;

    @FXML
    private TableColumn<ObservableList<String>, String>  heure;

    @FXML
    private TableColumn<ObservableList<String>, String> cnmsg;

    @FXML
    private TableColumn<ObservableList<String>, String> emet;

    @FXML
    private TableColumn<ObservableList<String>, String> nmroom;

    @FXML
    private TableView<ObservableList<String>> tabm;
    @FXML
    private TableColumn<ObservableList<String>, String> idmsg;

    @FXML
    void initialize() {
        assert cnmsg != null : "fx:id=\"cnmsg\" was not injected: check your FXML file 'DashbordmsgFXML.fxml'.";
        assert emet != null : "fx:id=\"emet\" was not injected: check your FXML file 'DashbordmsgFXML.fxml'.";
        assert nmroom != null : "fx:id=\"nmroom\" was not injected: check your FXML file 'DashbordmsgFXML.fxml'.";

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
                    String idmsgString = row.get(0);
                    System.out.println(idmsgString);
                    int idmsg = Integer.parseInt(idmsgString);
                    // Action to perform when the "Supprimer" button is clicked
                    Servicemessage sq = new Servicemessage();
                    try {
//int idRoom = Integer.parseInt(rowData.get(0));
                        sq.DeleteOne(idmsg);
                        viewdashbordmsg();

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

    public void viewdashbordmsg() {
        Serviceroom sm = new Serviceroom();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "");
            Statement statement = connection.createStatement();

            // Retrieve data from the 'user' table
            ResultSet resultSet = statement
                    .executeQuery("select DISTINCT m.id_msg,m.contenu,r.nom_room,u.username,m.heure_msg,m.date_msg From message m Join room r ON m.room_id=r.id_room and m.status='Active' Join user_formation f ON f.room_id=r.id_room join user u ON f.user_id=u.id_user;");
            ObservableList<ObservableList<String>> oblist = FXCollections.observableArrayList();

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("id_msg"));
                row.add(resultSet.getString("contenu"));
                row.add(resultSet.getString("nom_room"));
                row.add(resultSet.getString("username"));
              //  row.add(String.valueOf(resultSet.getTime("heure_msg")));
                Time time = resultSet.getTime("heure_msg");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String formattedTime = timeFormat.format(time);
                row.add(formattedTime);

                row.add(resultSet.getString("date_msg"));
               // row.add(resultSet.getString("date_msg"));

                oblist.add(row);
            }

            tabm.setItems(oblist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
// Bind columns to their respective data properties
       // idmsg.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        cnmsg.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));

        nmroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        emet.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));

      date_msg.setCellValueFactory(param->new SimpleStringProperty(param.getValue().get(4)));
        heure.setCellValueFactory(param->new SimpleStringProperty(param.getValue().get(5)));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         viewdashbordmsg();
        TableColumn<ObservableList<String>, Void> deleteColumn = setupDeleteButtonColumn();
        tabm.getColumns().add(deleteColumn);

    }

}
