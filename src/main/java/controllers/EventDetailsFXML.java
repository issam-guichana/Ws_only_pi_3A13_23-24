package controllers;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.Evenement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import services.Payment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EventDetailsFXML implements Initializable {
    @FXML
    private Button backButton;
    @FXML
    private Button participer;
    @FXML
    private Label countdownLabel;
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
    private Evenement currentEvent;
    @FXML
    private WebView mapView;
    private WebEngine webEngine;
    private boolean stylesApplied = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = mapView.getEngine();
        setupCountdownTimer();
    }
    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
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
        if (event.getLieu() != null && !event.getLieu().isEmpty()) {
            // Use OpenStreetMap to generate a map URL
            String mapUrl = "https://www.openstreetmap.org/?mlat=" + event.getLieu() +
                    "&mlon=" + event.getLieu() + "#map=14/" + event.getLieu();
            webEngine.load(mapUrl);
        }
        currentEvent = event;
    }

    public void goToCalendar(ActionEvent actionEvent) {
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

    private void setupCountdownTimer() {
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.seconds(0),
                        actionEvent -> {
                            if (currentEvent != null) {
                                LocalDateTime startDateTime = LocalDateTime.of(currentEvent.getDate_event(), currentEvent.getHeure_deb());
                                java.time.Duration duration = java.time.Duration.between(LocalDateTime.now(), startDateTime);

                                if (!duration.isNegative()) {
                                    long seconds = duration.getSeconds();
                                    String time = String.format("%dd %dh %dm %ds left",
                                            seconds / 86400,
                                            (seconds % 86400) / 3600,
                                            (seconds % 3600) / 60,
                                            (seconds % 60));

                                    // Apply styles for the countdown label
                                    countdownLabel.getStylesheets().add(getClass().getResource("/Styles.css").toExternalForm());
                                    countdownLabel.getStyleClass().clear(); // Clear existing styles
                                    countdownLabel.getStyleClass().addAll("countdown-label", "clock-digits");
                                    countdownLabel.setText(time);
                                } else {
                                    // Apply styles for the countdown label when the event is happening
                                    countdownLabel.setText("Event is happening now!");
                                    countdownLabel.getStylesheets().add(getClass().getResource("/Styles.css").toExternalForm());
                                    countdownLabel.getStyleClass().clear(); // Clear existing styles
                                    countdownLabel.getStyleClass().addAll("countdown-label", "clock-digits", "event-now");

                                    // Apply a pulsing effect
                                    FadeTransition fadeTransition = new FadeTransition(javafx.util.Duration.seconds(1), countdownLabel);
                                    fadeTransition.setFromValue(1.0);
                                    fadeTransition.setToValue(0.3);
                                    fadeTransition.setCycleCount(Timeline.INDEFINITE);
                                    fadeTransition.setAutoReverse(true);
                                    fadeTransition.play();
                                }
                            }
                        }
                ),
                new KeyFrame(javafx.util.Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}


