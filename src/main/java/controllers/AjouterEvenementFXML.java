package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Evenement;
import services.ServiceEvenement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AjouterEvenementFXML {

    @FXML
    private DatePicker tfDate_event;

    @FXML
    private TextArea tfDescr1;

    @FXML
    private TextField tfNbrP1;

    @FXML
    private TextField tfNom1;

    @FXML
    private Button tfSave;

    @FXML
    private Button tfclear;

    @FXML
    private Button tfdelete;

    @FXML
    private Spinner<LocalTime> tfheure_event;
    int currentvalue;

    @FXML
    private TextField tfprix;

    @FXML
    private Button tfupdate;

    @FXML
    private Button tfafficher;

    @FXML
    private TableColumn<Evenement, String> ColNom;

    @FXML
    private TableColumn<Evenement, String> colDescr;

    @FXML
    private TableColumn<Evenement, Integer> colId;

    @FXML
    private TableColumn<Evenement, Integer> colNbrP;

    @FXML
    private TableColumn<Evenement, Integer> colPrix;

    @FXML
    private TableColumn<Evenement, Date> coldate;

    @FXML
    private TableColumn<Evenement, Date> colheure;

    @FXML
    private TableView<Evenement> tbEvents;

    @FXML
    void ajouterEvenemnt(ActionEvent event) {
        try {
            LocalDate localDate = tfDate_event.getValue();
            LocalTime localTime = tfheure_event.getValue();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            Evenement p = new Evenement(tfNom1.getText(), tfDescr1.getText(), sqlDate, localTime, Integer.parseInt(tfprix.getText()), Integer.parseInt(tfNbrP1.getText()));

            ServiceEvenement sp = new ServiceEvenement(tbEvents);
            sp.insertOne(p);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        }
    }

    @FXML
    void clearFiled(ActionEvent event) {
        tfNom1.setText(null);
        tfDate_event.setValue(null);
        tfheure_event.getValueFactory().setValue(null);
        tfprix.setText(null);
        tfDescr1.setText(null);
        tfNbrP1.setText(null);
    }

    private LocalTime defaultValue() {
        return LocalTime.now().truncatedTo(ChronoUnit.HOURS);
    }

    @FXML
    void deleteEvenemnt(ActionEvent event) {
        Evenement selectedEvent = tbEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            // Handle the case where no event is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Event Selected");
            alert.setContentText("Please select an event to delete.");
            alert.show();
            return;
        }

        // Delete selected Event from the database
        try {
            ServiceEvenement se = new ServiceEvenement();  // Creating an instance
            se.deleteOne(selectedEvent);
            tbEvents.refresh(); // Refresh TableView after deletion
            System.out.println("Event deleted successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateEvenemnt(ActionEvent event) {
        // Check if there is a selected event
        Evenement selectedEvent = tbEvents.getSelectionModel().getSelectedItem();

        try {
            String nom_event = (tfNom1.getText() != null) ? tfNom1.getText() : "";
            String description = (tfDescr1.getText() != null) ? tfDescr1.getText() : "";
            LocalDate localDate = tfDate_event.getValue();
            LocalTime timeText = tfheure_event.getValue();

// Conversion de LocalDate en java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

// Conversion de LocalTime en java.sql.Time

            int prix = Integer.parseInt((tfprix.getText() != null) ? tfprix.getText() : "0");
            int nbrP = Integer.parseInt((tfNbrP1.getText() != null) ? tfNbrP1.getText() : "0");

// Create Evenement object

            Evenement newEvent = new Evenement(selectedEvent.getId_event(), nom_event, description, sqlDate, timeText, prix, nbrP);
            // Update in the database
            ServiceEvenement se = new ServiceEvenement();
            se.updateOne(newEvent);

            // Refresh TableView after update
            tbEvents.refresh();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setContentText("Event updated successfully.");
            alert.show();

        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setContentText("Failed to update event: " + e.getMessage());
            alert.show();
        }
    }



    void afficherevent() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_event"));
        ColNom.setCellValueFactory(new PropertyValueFactory<>("nom_event"));
        colDescr.setCellValueFactory(new PropertyValueFactory<>("description"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date_event"));
        colheure.setCellValueFactory(new PropertyValueFactory<>("heure_deb"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colNbrP.setCellValueFactory(new PropertyValueFactory<>("nbrP"));

        try {
            ServiceEvenement se = new ServiceEvenement();
            List<Evenement> events = se.selectAll();
            ObservableList<Evenement> el = FXCollections.observableArrayList(events);
            tbEvents.setItems(el);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes : " + e.getMessage());
        }
    }

    @FXML
    void refresh(ActionEvent event) {
        afficherevent();
    }

    @FXML
    void initialize() {
        assert tfDate_event != null : "fx:id=\"tfDate_event\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfDescr1 != null : "fx:id=\"tfDescr\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfheure_event != null : "fx:id=\"tfheure_deb\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfNbrP1 != null : "fx:id=\"tfNbrP\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfNom1 != null : "fx:id=\"tfNom\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfclear != null : "fx:id=\"tfReset\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfSave != null : "fx:id=\"tfSave\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfupdate != null : "fx:id=\"tfSave\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfdelete != null : "fx:id=\"tfSave\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfafficher != null : "fx:id=\"tfafficher\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";


        //tableau
        assert ColNom != null : "fx:id=\"ColNom\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colDescr != null : "fx:id=\"colDescr\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert coldate != null : "fx:id=\"coldate\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colheure != null : "fx:id=\" colheure\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colId != null : "fx:id=\"colId\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colPrix != null : "fx:id=\"colLieu\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colNbrP != null : "fx:id=\"colNbrP\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tbEvents != null : "fx:id=\"tbEvents\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<LocalTime>() {
            {
                setValue(defaultValue());
            }

            private LocalTime defaultValue() {
                return null;
            }

            @Override
            public void decrement(int steps) {
                LocalTime value = getValue();
                setValue(value == null ? null : value.minusMinutes(30 * steps));
            }

            @Override
            public void increment(int steps) {
                LocalTime value = getValue();
                setValue(value == null ? LocalTime.now().truncatedTo(ChronoUnit.HOURS) : value.plusMinutes(30 * steps));
            }
        };

        tfheure_event.setValueFactory(factory);

        afficherevent();
    }
}

