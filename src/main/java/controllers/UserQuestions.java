package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.Quiz;

import java.io.IOException;

public class UserQuestions {
    @FXML
    private Label durationLabel;

    @FXML
    private Label noteLabel;
    @FXML
    private Button startQuiz;
    @FXML
    private Button cancel;
    @FXML
    private VBox quizContainer;

    private Quiz selectedQuiz;
    @FXML
    private VBox quizInfoContainer;
    @FXML
    private VBox quizInfoBG;

    public void initData(Quiz selectedQuiz) {
        this.selectedQuiz = selectedQuiz;

        HBox quizBox = new HBox();
        quizBox.setSpacing(10); // Vertical spacing between components
        quizBox.setAlignment(Pos.CENTER); // Align components to the center horizontally
        quizBox.setStyle("-fx-background-radius: 10px; -fx-padding: 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.2, 0, 0);"); // Set background color
        quizBox.setPrefWidth(quizInfoContainer.getWidth());
        quizBox.setPrefHeight(quizInfoContainer.getHeight());

        // Create a Rectangle with rounded corners
        Rectangle clip = new Rectangle(100, 100);
        clip.setArcWidth(20); // Adjust this value as needed for the roundness of the corners
        clip.setArcHeight(20); // Adjust this value as needed for the roundness of the corners

        // Create the ImageView and apply the Rectangle as a clip to it
        ImageView imageView = new ImageView(new Image(selectedQuiz.getImage()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setClip(clip);

        // Create a label to display the quiz name
        Label quizNameLabel = new Label(selectedQuiz.getNom_quiz());
        quizNameLabel.setStyle("-fx-text-fill: white");
        quizBox.getChildren().addAll(imageView, quizNameLabel);
        quizInfoContainer.getChildren().add(quizBox);

        // Set the name of the selected quiz
        quizNameLabel.setText(selectedQuiz.getNom_quiz());

    }
    @FXML
    void startQuiz(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RepondreQuiz.fxml"));
        Parent root = loader.load();

        // Get the controller of the RepondreQuiz interface
        RepondreQuiz controller= loader.getController();

        // Pass the selected quiz to the controller
        controller.initData(selectedQuiz);

        // Set the RepondreQuiz interface as the root of the scene
        startQuiz.getScene().setRoot(root);
    }


    @FXML
    void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/UserQuiz.fxml"));
        Parent root = loader.load();
        UserQuiz lc = loader.getController();
        cancel.getScene().setRoot(root);
    }
    @FXML
    public void initialize() {

    }
}