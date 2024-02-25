package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InterfaceMainClientFXML implements Initializable {

    @FXML
    private ImageView Menu;

    @FXML
    private ImageView MenuBack;

    @FXML
    private AnchorPane slider;
    @FXML
    private JFXButton formationbutton;

    @FXML
    private JFXButton pourvousb;

    @FXML
    private JFXButton lesplusvisitesb;

    @FXML
    private JFXButton eventsbutton;
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        slider.setTranslateX(-176);

        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-176);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                MenuBack.setVisible(false);
                Menu.setVisible(true);
            });
        });
    }
    @FXML
    private void handleFormationButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceMainClient1FXML.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePourVousButton(ActionEvent event) {
        // Code pour ouvrir la nouvelle interface ici
    }

    @FXML
    private void handleLesPlusVisitesButton(ActionEvent event) {
        // Code pour ouvrir la nouvelle interface ici
    }

    @FXML
    private void handleEventsButton(ActionEvent event) {
        // Code pour ouvrir la nouvelle interface ici
    }
    }

