package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.Question;
import models.Quiz;
import services.ServiceQuestion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepondreQuiz {
    @FXML
    private ProgressBar countdownBar;
    private List<String> userChoices = new ArrayList<>(); // List to store user's choices
    private Timeline timer; // Timer for the quiz

    @FXML
    private Button choix1;

    @FXML
    private Button choix2;

    @FXML
    private Button choix3;

    @FXML
    private Button choix4;

    @FXML
    private Label question;
    private Quiz selectedQuiz;
    private List<Question> questions;
    private int currentQuestionIndex;
    private Question currentQuestion; // Declaration of currentQuestion

    public void initData(Quiz selectedQuiz) {
        this.selectedQuiz = selectedQuiz;
        try {
            // Get the questions for the selected quiz
            ServiceQuestion serviceQuestion = new ServiceQuestion();
            questions = serviceQuestion.selectQuestionByQuiz(selectedQuiz);
            currentQuestionIndex = 0;

            // Display the first question
            displayQuestion();

            // Start the timer
            startTimer();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            System.out.println(currentQuestionIndex);
            currentQuestion = questions.get(currentQuestionIndex); // Assigning currentQuestion

            // Display the question
            question.setText(currentQuestion.getQuestion());

            // Display the choices
            // Display the choices as buttons
            choix1.setText(currentQuestion.getOption1());


            choix2.setText(currentQuestion.getOption2());


            choix3.setText(currentQuestion.getOption3());


            choix4.setText(currentQuestion.getOption4());

            choix1.setOnAction(this::handleChoiceSelection);
            choix2.setOnAction(this::handleChoiceSelection);
            choix3.setOnAction(this::handleChoiceSelection);
            choix4.setOnAction(this::handleChoiceSelection);
        } else {
            // If all questions have been displayed, show a completion message
            question.setText("Quiz completed!");
            choix1.setText("");
            choix2.setText("");
            choix3.setText("");
            choix4.setText("");
            stopTimer(); // Stop the timer
            displayScore(); // Display the user's score when quiz is completed
        }
    }

    @FXML
    public void handleChoiceSelection(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        String selectedChoice = clickedButton.getText();
        userChoices.add(selectedChoice); // Store the user's choice

        // Move to the next question
        currentQuestionIndex++;
        displayQuestion();
    }

    // Method to calculate the user's score at the end of the quiz
    private int calculateScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userChoice = userChoices.get(i);
            String correctChoice = ""; // Initialize correctChoice

            // Convert the choice number to the corresponding option string
            switch (question.getReponse()) {
                case "choix1":
                    correctChoice = question.getOption1();
                    break;
                case "choix2":
                    correctChoice = question.getOption2();
                    break;
                case "choix3":
                    correctChoice = question.getOption3();
                    break;
                case "choix4":
                    correctChoice = question.getOption4();
                    break;
            }

            // Check if the user's choice matches the correct choice
            if (userChoice.equals(correctChoice)) {
                score++; // Increment score if the user's choice is correct
            }
        }
        return score;
    }

    // Method to display the user's score
    private void displayScore() {
        int score = calculateScore();
        // Display the user's score, e.g., in a label or alert dialog
        // For example:
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Completed");
        alert.setHeaderText(null);
        alert.setContentText("Your score: " + score + " out of 10");
        alert.showAndWait();
    }

    // Method to start the timer
    private void startTimer() {
        countdownBar.setProgress(1.0); // Reset progress to full at the start

        timer = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(countdownBar.progressProperty(), 1.0)),
                new KeyFrame(Duration.seconds(10)/*Duration.minutes(30)*/, event -> {
                    // Close the quiz and return to Quiz Selection
                    try {
                        backToQuizSelection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, new KeyValue(countdownBar.progressProperty(), 0.0)) // Update progress to 0.0 after 30 seconds
        );

        timer.setOnFinished(event -> {
            // Show an alert indicating that the quiz time has expired
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Quiz Time Expired");
                alert.setHeaderText(null);
                alert.setContentText("You have exceeded the time limit for the quiz. Returning to Quiz Selection.");
                alert.showAndWait();
            });
        });

        timer.setCycleCount(1); // Run the timer once
        timer.play();
    }

    // Method to stop the timer
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
    private int calculateScoreWithTimeLimit() {
        int score = 0;
        int answeredQuestionsCount = Math.min(currentQuestionIndex, questions.size()); // Count of questions user managed to answer

        for (int i = 0; i < answeredQuestionsCount; i++) {
            Question question = questions.get(i);
            String userChoice = userChoices.get(i);
            String correctChoice = ""; // Initialize correctChoice

            // Convert the choice number to the corresponding option string
            switch (question.getReponse()) {
                case "choix1":
                    correctChoice = question.getOption1();
                    break;
                case "choix2":
                    correctChoice = question.getOption2();
                    break;
                case "choix3":
                    correctChoice = question.getOption3();
                    break;
                case "choix4":
                    correctChoice = question.getOption4();
                    break;
            }

            // Check if the user's choice matches the correct choice
            if (userChoice.equals(correctChoice)) {
                score++; // Increment score if the user's choice is correct
            }
        }
        return score;
    }
    // Method to navigate back to Quiz Selection
    private void backToQuizSelection() throws IOException {
        int score = calculateScoreWithTimeLimit();

        // Load the UserQuiz interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserQuiz.fxml"));
        Parent root = loader.load();

        // Access UserQuiz controller and set the score
        UserQuiz userQuizController = loader.getController();
        userQuizController.setScore(score);

        // Change the scene to the UserQuiz interface
        countdownBar.getScene().setRoot(root);
    }

}
