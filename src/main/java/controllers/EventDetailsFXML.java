package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.Evenement;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EventDetailsFXML implements Initializable {
    @FXML
    private Button backButton;
    @FXML
    private Button participer;
    @FXML
    private CalendarController calendarController;
    @FXML
    private ImageView Image;

    @FXML
    private Label nom;

    @FXML
    private Label desc;

    @FXML
    private Label date;

    @FXML
    private Label heure;

    @FXML
    private Label prix;

    @FXML
    private Label nbrp;

    @FXML
    private WebView mapView;
    private WebEngine webEngine;
    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }


    @FXML

    public void setEventDetails(Evenement event) {
        if (event != null) {
            // Set the image only if it is not null
            if (event.getImage_event() != null && !event.getImage_event().isEmpty()) {
                Image.setImage(new javafx.scene.image.Image(event.getImage_event()));
            }

            nom.setText(event.getNom_event());
            desc.setText(event.getDescription());
            date.setText(event.getDate_event().toString());
            heure.setText(event.getHeure_deb().toString());
            prix.setText(String.valueOf(event.getPrix()));
            nbrp.setText(String.valueOf(event.getNbrP()));
        } else {
            System.out.println("event mafamech");
        }
        if (event.getLieu() != null && !event.getLieu().isEmpty()) {
            // Use OpenStreetMap to generate a map URL
            String mapUrl = "https://www.openstreetmap.org/?mlat=" + event.getLieu() +
                    "&mlon=" + event.getLieu() + "#map=14/" + event.getLieu();
            setMapUrl(mapUrl);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = mapView.getEngine();
    }
    // New method to set the map URL
    private String getOpenStreetMapURL(String location) {
        return "https://www.openstreetmap.org/search?query=" + location.replace(" ", "%20");
    }

    public void setMapUrl(String mapUrl) {
        webEngine.load(mapUrl);
    }


    public void goToCalendar(javafx.event.ActionEvent actionEvent) {
        try {
            // Close the current stage
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            // Get the reference to the original CalendarController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendierFXML.fxml"));
            Parent root = loader.load();
            CalendarController calendarController = loader.getController();

            // Additional actions or data passing can be done here

            // Replace the content of the current stage
            currentStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void participerevent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ParticiperFXML.fxml"));
            Parent root = loader.load();

            // Additional setup if needed

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Participer Event");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



