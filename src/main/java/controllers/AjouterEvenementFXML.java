package controllers;

import controllers.UserParticipantController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Evenement;
import services.ServiceEvenement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AjouterEvenementFXML {
    @FXML
    private Button tfListP;

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
    private TableColumn<Evenement, String> colimg;
    @FXML
    private TableColumn<Evenement, Integer> colNbrP;

    @FXML
    private TableColumn<Evenement, Integer> colPrix;

    @FXML
    private TableColumn<Evenement, Date> coldate;

    @FXML
    private TableColumn<Evenement, Date> colheure;
    @FXML
    private TextField tfimage;
    @FXML
    private TableView<Evenement> tbEvents;

    @FXML
    private Button btn_importer;
    @FXML
    private ImageView imageevenement;
    private String xamppFolderPath = "C:/xampp/htdocs/img/";

    @FXML
    void ajouterEvenemnt(ActionEvent event) {
        try {
            // Validate input fields
            if (tfNom1.getText().isEmpty() || tfDescr1.getText().isEmpty() || tfDate_event.getValue() == null
                    || tfheure_event.getValue() == null || tfprix.getText().isEmpty() || tfNbrP1.getText().isEmpty()
                    || tfimage.getText().isEmpty()) {
                // Display an error message if any of the fields are empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.show();
                return; // Exit the method if validation fails
            }


            LocalDate localDate = tfDate_event.getValue();
            LocalTime localTime = tfheure_event.getValue();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            // Additional validation for numeric fields
            if (!isNumeric(tfprix.getText()) || !isNumeric(tfNbrP1.getText())) {
                // Display an error message if price or quantity are not numeric
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Les champs Prix et Quantité doivent être des valeurs numériques.");
                alert.show();
                return; // Exit the method if validation fails
            }

            // Get the image URL from the tfimage TextField
            String imageUrl = tfimage.getText();

            Evenement p = new Evenement(tfNom1.getText(), tfDescr1.getText(), sqlDate, localTime,
                    Integer.parseInt(tfprix.getText()), Integer.parseInt(tfNbrP1.getText()), imageUrl);

            ServiceEvenement sp = new ServiceEvenement(tbEvents);

            if (sp.isEventNameUnique(p)) {
                // Insert the event if the name is unique
                sp.insertOne(p);

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setContentText("L'événement a été ajouté avec succès.");
                successAlert.show();

                // Additional log for debugging
                System.out.println("Event added successfully");
            } else {
                // Display an error message if the event name is not unique
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Le nom de l'événement doit être unique.");
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        }
    }



    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    @FXML
    void clearFiled(ActionEvent event) {
        tfNom1.setText(null);
        tfDate_event.setValue(null);
        tfheure_event.getValueFactory().setValue(null);
        tfprix.setText(null);
        tfDescr1.setText(null);
        tfNbrP1.setText(null);
        tfimage.setText(null);
        imageevenement.setImage(null);
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

            // Call the refresh function to update the table after deletion
            refresh(null);

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
            String image_event = (tfimage.getText() != null) ? tfimage.getText() : "";
            // Conversion de LocalDate en java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            // Conversion de LocalTime en java.sql.Time
            int prix = Integer.parseInt((tfprix.getText() != null) ? tfprix.getText() : "0");
            int nbrP = Integer.parseInt((tfNbrP1.getText() != null) ? tfNbrP1.getText() : "0");

            // Create Evenement object
            Evenement newEvent = new Evenement(selectedEvent.getId_event(), nom_event, description, sqlDate, timeText, prix, nbrP, image_event);
            // Set the image_event property
            newEvent.setImage_event(image_event);

            // Update in the database
            ServiceEvenement se = new ServiceEvenement();
            se.updateOne(newEvent);

            // Update the corresponding object in the ObservableList
            int index = tbEvents.getItems().indexOf(selectedEvent);
            tbEvents.getItems().set(index, newEvent);

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

@FXML
    void afficherevent() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_event"));
        ColNom.setCellValueFactory(new PropertyValueFactory<>("nom_event"));
        colDescr.setCellValueFactory(new PropertyValueFactory<>("description"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date_event"));
        colheure.setCellValueFactory(new PropertyValueFactory<>("heure_deb"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colNbrP.setCellValueFactory(new PropertyValueFactory<>("nbrP"));
        colimg.setCellValueFactory(new PropertyValueFactory<>("image_event"));
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
        assert tfupdate != null : "fx:id=\"tfupdate\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfdelete != null : "fx:id=\"tfdelete\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert tfafficher != null : "fx:id=\"tfafficher\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert btn_importer != null : "fx:id=\"btn_importer\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert imageevenement != null : "fx:id=\"imageevenement\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        //tableau

        assert ColNom != null : "fx:id=\"ColNom\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colDescr != null : "fx:id=\"colDescr\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert coldate != null : "fx:id=\"coldate\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colheure != null : "fx:id=\" colheure\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colId != null : "fx:id=\"colId\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colPrix != null : "fx:id=\"colLieu\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colNbrP != null : "fx:id=\"colNbrP\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
        assert colimg != null : "fx:id=\"Colimg\" was not injected: check your FXML file 'AjouterEvenementFXML.fxml'.";
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
        ColNom.setCellValueFactory(new PropertyValueFactory<>("nom_event"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date_event"));

        tbEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setFieldsFromSelectedEvent(newSelection);
            }
        });

        afficherevent();

    }

    @FXML
    void accederListeParticipants(ActionEvent event) {
        try {
            Evenement selectedEvent = tbEvents.getSelectionModel().getSelectedItem();

            if (selectedEvent == null) {
                // If no event is selected, show the list of all participants
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GererParticipantsFXML.fxml"));
                Parent root = loader.load();

                UserParticipantController controller = loader.getController();
                controller.setEventId(0); // Pass a special value to indicate all participants

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des participants");
                stage.show();
            } else {
                // If an event is selected, show participants for that event
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GererParticipantsFXML.fxml"));
                Parent root = loader.load();

                UserParticipantController controller = loader.getController();
                controller.setEventId(selectedEvent.getId_event());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des participants");
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception
        }
    }
    @FXML
    void importerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick a banner file !");
        Stage stage = new Stage();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Path source = file.toPath();

            // Save the full URL of the image
            String imageUrl = xamppFolderPath + file.getName();

            try {
                Files.copy(source, Paths.get(imageUrl), StandardCopyOption.REPLACE_EXISTING);

                // Load the image
                Image image = new Image("file:" + imageUrl);

                // Set the image to the ImageView
                imageevenement.setImage(image);

                // Set the image URL to the text field
                tfimage.setText(imageUrl);

            } catch (IOException ex) {
                System.out.println("Could not get the image");
                ex.printStackTrace();
            }
        } else {
            System.out.println("No file selected");
        }
    }
    private void setFieldsFromSelectedEvent(Evenement selectedEvent) {
        // Set the text of your TextFields based on the selected event
        tfNom1.setText(selectedEvent.getNom_event());
        tfDescr1.setText(selectedEvent.getDescription());
        tfprix.setText(String.valueOf(selectedEvent.getPrix()));
        tfDate_event.setValue(selectedEvent.getDate_event());
        LocalTime selectedTime = selectedEvent.getHeure_deb();
        tfheure_event.getValueFactory().setValue(selectedTime);
        tfNbrP1.setText(String.valueOf(selectedEvent.getNbrP()));
        tfimage.setText(selectedEvent.getImage_event());

        // Set the image to the ImageView
        String imageUrl = selectedEvent.getImage_event();
        System.out.println("Image URL: " + imageUrl); // Print URL for debugging

        try {
            // Load the image
            Image image = new Image("file:" + imageUrl);
            System.out.println("Image Loaded Successfully"); // Debugging statement

            // Set the image to the ImageView
            imageevenement.setImage(image);

        } catch (Exception ex) {
            System.out.println("Could not load the image");
            ex.printStackTrace();
        }
    }

}