package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Quiz;
import services.ServiceQuiz;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterQuiz {



    @FXML
    private Button btnadd;

    @FXML
    private Button btnrefresh;

    @FXML
    private TextField finput1;

    @FXML
    private TextField finput11;

    @FXML
    private Text ftext1;

    @FXML
    private Text ftext11;

    @FXML
    private TableView<Quiz> quizlist;

    @FXML
    private TableColumn<?, ?> quizname;




    @FXML
    void ajouterQuiz(ActionEvent event) {
        btnadd.setOnAction(e->{
            String qn=finput1.getText();
            Quiz q=new Quiz(qn);
            ServiceQuiz sq =new ServiceQuiz();
            try {
                sq.insertOne(q);
                displayAllQuizInTableView();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
                alert.show();
            }
        });
    }
    public void displayAllQuizInTableView() {
        quizname.setCellValueFactory(new PropertyValueFactory<>("nom_quiz"));


        try {
            ServiceQuiz serviceQuiz = new ServiceQuiz();
            List<Quiz> quizzes = serviceQuiz.selectAll();
            ObservableList<Quiz> ql = FXCollections.observableArrayList(quizzes);
            quizlist.setItems(ql);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes : " + e.getMessage());
        }
    }
    @FXML
    void refresh(ActionEvent event) {
        btnrefresh.setOnAction(e->{
            displayAllQuizInTableView();
        });
    }

   private void setupButtonColumns() {
        setupAccederButtonColumn();
        setupModifierButtonColumn();
        setupSupprimerButtonColumn();
    }

    private void setupAccederButtonColumn() {
        TableColumn<Quiz, Void> accederColumn = new TableColumn<>("Accéder");
        accederColumn.setCellFactory(col -> new TableCell<Quiz, Void>() {
            private final Button accessButton = new Button("Accéder");

            {
                accessButton.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Accéder" button is clicked
                    System.out.println("Accéder to quiz: " + quiz.getNom_quiz());
                    openQuestionCRUDPage(quiz); // Call method to navigate to question CRUD page
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : accessButton);
            }
        });
        quizlist.getColumns().add(accederColumn);
    }

    private void openQuestionCRUDPage(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterQuestion.fxml"));
            Parent root = loader.load();

            // Pass the quiz to the controller of the question CRUD page
            AjouterQuestion controller = loader.getController();
            controller.setQuiz(quiz);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Question CRUD Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception
        }
    }


    private void setupModifierButtonColumn() {
        TableColumn<Quiz, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(col -> new TableCell<Quiz, Void>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Modifier" button is clicked
                    System.out.println("Modify quiz: " + quiz.getNom_quiz());
                    displayModifyDialog(quiz); // Call method to display dialog
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });
        quizlist.getColumns().add(modifierColumn);
    }

    private void displayModifyDialog(Quiz quiz) {
        // Create the dialog components
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier le nom du quiz");
        dialog.setHeaderText("Entrez le nouveau nom pour le quiz");

        // Set the button types
        ButtonType modifyButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        // Create the input field
        TextField newNameField = new TextField();
        newNameField.setPromptText("Nouveau nom");

        // Enable/disable the modify button depending on whether a name is entered
        Node modifyButton = dialog.getDialogPane().lookupButton(modifyButtonType);
        modifyButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        newNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(newNameField);

        // Request focus on the newNameField by default
        Platform.runLater(newNameField::requestFocus);

        // Convert the result to a string when the modify button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return newNameField.getText();
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(newName -> {
            // Update the quiz name if the user clicked "Modifier"
            if (newName != null && !newName.isEmpty()) {
                quiz.setNom_quiz(newName);
                // Call your service method to update the quiz in the database
                try {
                    ServiceQuiz sq = new ServiceQuiz();
                    sq.updateOne(quiz);
                    // Refresh the table view to reflect the changes
                    displayAllQuizInTableView();
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle exception
                }
            }
        });
    }


    private void setupSupprimerButtonColumn() {
        TableColumn<Quiz, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<Quiz, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Supprimer" button is clicked
                    ServiceQuiz sq=new ServiceQuiz();
                    System.out.println("Delete quiz: " + quiz.getNom_quiz());
                    try {
                        sq.deleteOne(quiz);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        quizlist.getColumns().add(supprimerColumn);
    }

    @FXML
    public void initialize() {
        displayAllQuizInTableView();
        setupButtonColumns();
    }

}
