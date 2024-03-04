package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Question;
import models.Quiz;
import services.ServiceQuestion;
import javafx.scene.image.ImageView;
import services.ServiceQuiz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class AjouterQuestion {
    @FXML
    private TableView<Question> questTab;
    @FXML
    private TableColumn<?, ?> colQuest;

    @FXML
    private Button btnadd;

    @FXML
    private TextField choix1;

    @FXML
    private TextField choix2;

    @FXML
    private TextField choix3;

    @FXML
    private TextField choix4;

    @FXML
    private Text ftext1;

    @FXML
    private Text idQuizName;

    @FXML
    private Text ftext12;

    @FXML
    private Text ftext13;

    @FXML
    private Text ftext14;

    @FXML
    private Text ftext141;

    @FXML
    private Text ftext1411;

    @FXML
    private ImageView idimage;
    @FXML
    private ChoiceBox<String> idbr;

    @FXML
    private TextArea idenonce;
    private Quiz quiz;
    @FXML
    private Button btnQuiz;
    @FXML
    private Button goBackBtn;

    @FXML
    void ajouterQuestion(ActionEvent event) {
        btnadd.setOnAction(e -> {
            try {
                // Check if the number of questions exceeds the limit
                if (questTab.getItems().size() >= 10) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Vous avez dépassé le nombre maximum de questions (10).");
                    alert.showAndWait();
                    return; // Exit the method if the limit is reached
                }

                String enonce = idenonce.getText();
                String c1 = choix1.getText();
                String c2 = choix2.getText();
                String c3 = choix3.getText();
                String c4 = choix4.getText();
                String br = idbr.getValue();
                Question quest = new Question(enonce, br, c1, c2, c3, c4, this.quiz);
                ServiceQuestion sq = new ServiceQuestion();
                sq.insertOne(quest);
                displayAllQuestionsInTableView();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
                alert.show();
            }
        });

    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        // Update the label to display the quiz name
        idQuizName.setText("Quizzzz: " + quiz.getNom_quiz());
        Image image = new Image("file:" + quiz.getImage());
        idimage.setImage(image);
        displayAllQuestionsInTableView();
    }

    public void displayAllQuestionsInTableView(){
        colQuest.setCellValueFactory(new PropertyValueFactory<>("question"));


        try {
            ServiceQuestion serviceQuestion = new ServiceQuestion();
            List<Question> questions = serviceQuestion.selectQuestionByQuiz(quiz);
            ObservableList<Question> ql = FXCollections.observableArrayList(questions);
            questTab.setItems(ql);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des Questions : " + e.getMessage());
        }
    }
    private void setupModifierButtonColumn() {
        TableColumn<Question, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(col -> new TableCell<Question, Void>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(event -> {
                    Question question = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Modifier" button is clicked
                    System.out.println("Modify quiz: " + question.getQuestion());
                    displayModifyDialog(question); // Call method to display dialog
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });
        questTab.getColumns().add(modifierColumn);
    }

    private void displayModifyDialog(Question question) {
        // Create the dialog components
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier la question");
        dialog.setHeaderText("Modifier la question");

        // Set the button types
        ButtonType modifyButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        // Create the input fields
        TextField questionField = new TextField(question.getQuestion());
        questionField.setPromptText("Question...");
        TextField opt1Field = new TextField(question.getOption1());
        opt1Field.setPromptText("Option 1...");
        TextField opt2Field = new TextField(question.getOption2());
        opt2Field.setPromptText("Option 2...");
        TextField opt3Field = new TextField(question.getOption3());
        opt3Field.setPromptText("Option 3...");
        TextField opt4Field = new TextField(question.getOption4());
        opt4Field.setPromptText("Option 4...");

        // Create the choice box
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("opt1", "opt2", "opt3", "opt4");
        choiceBox.setValue(question.getReponse());

        // Enable/disable the modify button depending on whether all fields are filled
        Node modifyButton = dialog.getDialogPane().lookupButton(modifyButtonType);
        modifyButton.setDisable(true);

        questionField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() ||
                    opt1Field.getText().trim().isEmpty() ||
                    opt2Field.getText().trim().isEmpty() ||
                    opt3Field.getText().trim().isEmpty() ||
                    opt4Field.getText().trim().isEmpty());
        });
        opt1Field.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() ||
                    questionField.getText().trim().isEmpty() ||
                    opt2Field.getText().trim().isEmpty() ||
                    opt3Field.getText().trim().isEmpty() ||
                    opt4Field.getText().trim().isEmpty());
        });
        opt2Field.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() ||
                    questionField.getText().trim().isEmpty() ||
                    opt1Field.getText().trim().isEmpty() ||
                    opt3Field.getText().trim().isEmpty() ||
                    opt4Field.getText().trim().isEmpty());
        });
        opt3Field.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() ||
                    questionField.getText().trim().isEmpty() ||
                    opt1Field.getText().trim().isEmpty() ||
                    opt2Field.getText().trim().isEmpty() ||
                    opt4Field.getText().trim().isEmpty());
        });
        opt4Field.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButton.setDisable(newValue.trim().isEmpty() ||
                    questionField.getText().trim().isEmpty() ||
                    opt1Field.getText().trim().isEmpty() ||
                    opt2Field.getText().trim().isEmpty() ||
                    opt3Field.getText().trim().isEmpty());
        });

        // Set content of dialog pane
        dialog.getDialogPane().setContent(new VBox(8, questionField, opt1Field, opt2Field, opt3Field, opt4Field, choiceBox));

        // Request focus on the question field by default
        Platform.runLater(questionField::requestFocus);

        // Convert the result to a string when the modify button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                // Update the question object with the new values
                question.setId_quest(question.getId_quest());
                question.setQuestion(questionField.getText());
                question.setOption1(opt1Field.getText());
                question.setOption2(opt2Field.getText());
                question.setOption3(opt3Field.getText());
                question.setOption4(opt4Field.getText());
                question.setReponse(choiceBox.getValue());
                question.setQuiz(quiz);

                return "OK";
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(result -> {
            if (result.equals("OK")) {
                // Call your service method to update the question in the database
                try {
                    ServiceQuestion sq = new ServiceQuestion();
                    sq.updateOne(question);
                    // Refresh the table view to reflect the changes
                    displayAllQuestionsInTableView();
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle exception
                }
            }
        });
    }

    private void setupSupprimerButtonColumn() {
        TableColumn<Question, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<Question, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Question question = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Supprimer" button is clicked
                    ServiceQuestion sq=new ServiceQuestion();
                    System.out.println("Delete quiz: " + question.getQuestion());
                    try {
                        sq.deleteOne(question);
                        displayAllQuestionsInTableView();
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
        questTab.getColumns().add(supprimerColumn);
    }
    private void setupButtonColumns() {
        setupModifierButtonColumn();
        setupSupprimerButtonColumn();
    }
    @FXML
    void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AjouterQuiz.fxml"));
        Parent root = loader.load();
        AjouterQuiz lc = loader.getController();
        goBackBtn.getScene().setRoot(root);
    }
    @FXML
    void accederQuiz(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AjouterQuiz.fxml"));
        Parent root = loader.load();
        AjouterQuiz lc = loader.getController();
        btnQuiz.getScene().setRoot(root);
    }

    @FXML
    public void initialize() {
        setupButtonColumns();
        btnadd.disableProperty().bind(
                idenonce.textProperty().isEmpty()
                        .or(choix1.textProperty().isEmpty())
                        .or(choix2.textProperty().isEmpty())
                        .or(choix3.textProperty().isEmpty())
                        .or(choix4.textProperty().isEmpty())
                        .or(idbr.valueProperty().isNull())
        );
        ObservableList<String> items = FXCollections.observableArrayList("choix1", "choix2", "choix3", "choix4");
        idbr.setItems(items);

    }

}

