package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.Question;
import models.Quiz;
import services.ServiceQuestion;
import services.ServiceQuiz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserQuiz {

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSettings1;

    @FXML
    private TextField finput11;

    @FXML
    private Text ftext11;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    @FXML
    private HBox quizContainer;

    @FXML
    private Label pageLabel;

    private int currentPage = 1;
    private int pageSize = 12;
    private List<Quiz> allQuizzes;
    private int totalPages;
    @FXML
    private Button btnQuiz;
    private int score;
    public void setScore(int score){
        this.score=score;
        System.out.println(score);
    }

    public void loadQuizzes() {
        try {
            ServiceQuiz sq = new ServiceQuiz();
            allQuizzes = sq.selectAll();

            // Filter quizzes to only those with 10 questions
            allQuizzes = allQuizzes.stream()
                    .filter(quiz -> {
                        try {
                            List<Question> questions = new ServiceQuestion().selectQuestionByQuiz(quiz);
                            return questions.size() == 10;
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            totalPages = (int) Math.ceil((double) allQuizzes.size() / pageSize);

            // Assuming you have a container to hold the quizzes, like a VBox
            VBox quizzesContainer = new VBox();

            // Loop through the filtered list of quizzes
            for (Quiz quiz : allQuizzes) {
                // Create UI components to display the quiz
                // Add these components to the quizzesContainer
            }

            // Add the container to the main view (wherever you want to display the quizzes)
            quizContainer.getChildren().add(quizzesContainer);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }
    private void displayQuizzesForPage(int page) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allQuizzes.size());

        quizContainer.getChildren().clear(); // Clear previous quiz items

        GridPane quizzesGrid = new GridPane();
        quizzesGrid.setHgap(10); // Horizontal gap between columns
        quizzesGrid.setVgap(10); // Vertical gap between rows

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            Quiz quiz = allQuizzes.get(i);

            // Create a VBox to hold the components of each quiz
            VBox quizBox = new VBox();
            quizBox.setSpacing(10); // Vertical spacing between components
            quizBox.setAlignment(Pos.CENTER); // Align components to the center horizontally
            quizBox.setStyle("-fx-background-color: #E68C3AFF;-fx-background-radius: 10px; -fx-padding: 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.2, 0, 0);"); // Set background color

            // Create a Rectangle with rounded corners
            Rectangle clip = new Rectangle(100, 100);
            clip.setArcWidth(20); // Adjust this value as needed for the roundness of the corners
            clip.setArcHeight(20); // Adjust this value as needed for the roundness of the corners

            // Create the ImageView and apply the Rectangle as a clip to it
            ImageView imageView = new ImageView(new Image(quiz.getImage()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setClip(clip);

            // Create a label to display the quiz name
            Label quizNameLabel = new Label(quiz.getNom_quiz());
            quizNameLabel.setStyle("-fx-text-fill: white");

            Button button = new Button();
            Text buttonText = new Text("Acceder au Quiz");
            buttonText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            buttonText.setFill(Color.WHITE);
            buttonText.setUnderline(true);
            button.setGraphic(buttonText);
            button.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;-fx-background-radius: 10px");
            button.setCursor(Cursor.HAND);
            final int finalCol = col;
            final int finalRow = row;

// Add event handler to the "Access Quiz" button
            button.setOnAction(event -> {
                // Get the index of the selected quiz
                int index = (finalRow * 6) + finalCol;
                // Get the selected quiz
                Quiz selectedQuiz = allQuizzes.get(index);
                try {
                    // Load the UserQuestions controller
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserQuestions.fxml"));
                    Parent root = loader.load();
                    UserQuestions userQuestionsController = loader.getController();
                    // Pass the selected quiz to the UserQuestions controller
                    userQuestionsController.initData(selectedQuiz);
                    // Set the scene to the UserQuestions controller
                    button.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Add nodes to the VBox in the desired order
            quizBox.getChildren().addAll(imageView, quizNameLabel, button);

            // Add the VBox to the grid at the specified column and row
            quizzesGrid.add(quizBox, col, row);

            // Increment column index and reset to 0 if it reaches 5
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }

        // Add the grid to the quizContainer
        quizContainer.getChildren().add(quizzesGrid);

    }

    // Handle pagination actions
    @FXML
    private void goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayQuizzesForPage(currentPage);
        }
    }

    @FXML
    private void goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            displayQuizzesForPage(currentPage);
        }
    }
    // Method to update the label text showing the current page
    private void updatePageLabel() {
        pageLabel.setText("Page " + currentPage);
    }

    // Method to handle when the previous button is clicked
    @FXML
    private void handlePreviousButtonClick() {
        if (currentPage > 1) {
            currentPage--;
            updatePageLabel();
            displayQuizzesForPage(currentPage); // Update the displayed quizzes
            // Disable previous button if currentPage is 1
            previousButton.setDisable(currentPage == 1);
            // Enable next button since we can go to previous page
            nextButton.setDisable(false);
        }
    }

    // Method to handle when the next button is clicked
    @FXML
    private void handleNextButtonClick() {
        if (currentPage < totalPages) {
            currentPage++;
            updatePageLabel();
            displayQuizzesForPage(currentPage); // Update the displayed quizzes
            // Disable next button if currentPage is equal to totalPages
            nextButton.setDisable(currentPage == totalPages);
            // Enable previous button since we can go to next page
            previousButton.setDisable(false);
        }
    }

    // Add getters and setters for totalPages and currentPage
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    @FXML
    void rechercherQuiz(ActionEvent event) {
        finput11.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.trim();

            try {
                ServiceQuiz serviceQuiz = new ServiceQuiz();
                if (searchTerm.isEmpty()) {
                    // If the search bar is empty, display all quizzes
                    allQuizzes = serviceQuiz.selectAll();
                } else {
                    // Otherwise, search for quizzes based on the search term
                    allQuizzes = serviceQuiz.ChercherParQuizName(searchTerm);
                }

                totalPages = (int) Math.ceil((double) allQuizzes.size() / pageSize);
                currentPage = 1; // Reset to first page when searching

                displayQuizzesForPage(currentPage);
                updatePageLabel();
                // Disable previous button if currentPage is 1
                previousButton.setDisable(currentPage == 1);
                // Disable next button if currentPage is equal to totalPages
                nextButton.setDisable(currentPage == totalPages);
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la récupération des Quiz : " + ex.getMessage());
            }
        });
    }
    @FXML
    void accederQuiz(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/UserQuiz.fxml"));
        Parent root = loader.load();
        UserQuiz lc = loader.getController();

        btnQuiz.getScene().setRoot(root);
    }

    @FXML
    public void initialize() {
        loadQuizzes();
        displayQuizzesForPage(currentPage);
        updatePageLabel();
        // Disable previous button if currentPage is 1
        previousButton.setDisable(currentPage == 1);
        // Disable next button if currentPage is equal to totalPages
        nextButton.setDisable(currentPage == totalPages);
        finput11.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherQuiz(new ActionEvent()); // Call the method to perform the search
        });
    }
}
