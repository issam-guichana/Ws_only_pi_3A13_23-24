package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.Quiz;
import services.ServiceQuiz;

import java.sql.SQLException;
import java.util.List;

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
    private int pageSize = 10;
    private List<Quiz> allQuizzes;
    private int totalPages;



    public void loadQuizzes() {
        try {
            ServiceQuiz sq = new ServiceQuiz();
            allQuizzes = sq.selectAll();

            totalPages = (int) Math.ceil((double) allQuizzes.size() / pageSize);

            // Assuming you have a container to hold the quizzes, like a VBox
            VBox quizzesContainer = new VBox();

            // Loop through the list of quizzes
            for (Quiz quiz : allQuizzes) {
                // Create an HBox to hold the information of each quiz
                HBox quizBox = new HBox();
                quizBox.setSpacing(10); // Adjust spacing as needed

                // Create an ImageView for the quiz image
                ImageView imageView = new ImageView(new Image(quiz.getImage()));
                imageView.setFitWidth(100); // Adjust width as needed
                imageView.setFitHeight(100); // Adjust height as needed

                // Create a button to access the quiz
                Button button = new Button("Access Quiz");
                // Add event handler to the button to handle accessing the quiz

                // Add the image view and button to the HBox
                quizBox.getChildren().addAll(imageView, button);

                // Add the HBox to the container
                quizzesContainer.getChildren().add(quizBox);
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
            quizBox.setStyle("-fx-background-color: #E68C3AFF;-fx-background-radius: 10px; -fx-padding: 10px;"); // Set background color
            quizBox.setEffect(new DropShadow());

            ImageView imageView = new ImageView(new Image(quiz.getImage()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            // Create a label to display the quiz name
            Label quizNameLabel = new Label(quiz.getNom_quiz());
            quizNameLabel.setStyle("-fx-text-fill: white");

            Button button = new Button();
            Text buttonText = new Text("Access Quiz");
            buttonText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            buttonText.setFill(Color.WHITE);
            buttonText.setUnderline(true);
            button.setGraphic(buttonText);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(38,38,38,0.64);-fx-border-radius: 10px;-fx-background-radius: 10px");
            // Add event handler to the button to handle accessing the quiz

            // Add nodes to the VBox in the desired order
            quizBox.getChildren().addAll(imageView, quizNameLabel, button);

            // Add the VBox to the grid at the specified column and row
            quizzesGrid.add(quizBox, col, row);

            // Increment column index and reset to 0 if it reaches 5
            col++;
            if (col == 5) {
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
    public void initialize() {
        loadQuizzes();
        displayQuizzesForPage(currentPage);
        updatePageLabel();
        // Disable previous button if currentPage is 1
        previousButton.setDisable(currentPage == 1);
        // Disable next button if currentPage is equal to totalPages
        nextButton.setDisable(currentPage == totalPages);
    }
}
