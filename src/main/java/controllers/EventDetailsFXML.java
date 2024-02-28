package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Evenement;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EventDetailsFXML implements Initializable  {
    @FXML
    private Button backButton;
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
    private Stage currentStage;

    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
    @FXML
    private void goBackToCalendar() {
        System.out.println("goBackToCalendar method called."); // Add this line for debugging

        // Close the current stage
        if (currentStage != null) {
            currentStage.close();
        }

        // Show the main calendar stage
        if (calendarController != null) {
            calendarController.showCalendar();
        }
    }
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

