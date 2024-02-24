package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import models.Question;
import models.Quiz;
import services.ServiceQuestion;
import services.ServiceQuiz;

import java.sql.SQLException;

public class AjouterQuestion {

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
    private ChoiceBox<?> idbr;

    @FXML
    private TextArea idenonce;
    private Quiz quiz;

    @FXML
    void ajouterQuestion(ActionEvent event) {
        btnadd.setOnAction(e->{
            String enonce=idenonce.getText();
            String c1=choix1.getText();
            String c2=choix2.getText();
            String c3=choix3.getText();
            String c4=choix4.getText();
            String br=idbr.get;
            Question quest=new Question(enonce,br,c1,c2,c3,c4,this.quiz);
            ServiceQuestion sq =new ServiceQuestion();
            try {
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
        idQuizName.setText("Quiz: " + quiz.getNom_quiz());
    }
    @FXML
    public void initialize() {

    }

}

