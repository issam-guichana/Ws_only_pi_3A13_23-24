package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Quiz;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Captcha implements Initializable {
    @FXML
    private Label captchaLabel;

    @FXML
    private TextField captchaCode;

    @FXML
    private Label msg;

    @FXML
    private Button refreshCaptcha;

    private String captcha;
    private Quiz selectedQuiz;

    @FXML
    void onRefreshCaptcha(ActionEvent event) {
        captcha = generateCaptcha();
        captchaLabel.setText(captcha);
        captchaCode.setText("");
    }

    @FXML
    void onSubmit(ActionEvent event) throws IOException {
        if (captchaCode.getText().equalsIgnoreCase(captcha)) {
            msg.setText("You have entered the correct code!");
            msg.setTextFill(Color.GREEN);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserQuestions.fxml"));
            Parent root = loader.load();
            UserQuestions userQuestionsController = loader.getController();
            // Pass the selected quiz to the UserQuestions controller
            userQuestionsController.initData(selectedQuiz);
            // Set the scene to the UserQuestions controller
            captchaCode.getScene().setRoot(root);

        } else {
            msg.setText("You have entered the wrong code");
            msg.setTextFill(Color.RED);
            captcha = generateCaptcha();
            captchaLabel.setText(captcha);
            captchaCode.setText("");
        }
    }

    private String generateCaptcha() {
        char[] data = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's','t', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        int max = data.length - 1;
        int min = 0;
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int rand = (int) Math.floor(Math.random() * (max - min + 1) + min);
            captcha.append(data[rand]);
        }

        System.out.println(captcha.toString());
        return captcha.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/img/tourner-a-droite.png")).toExternalForm());
        refreshCaptcha.setGraphic(imageView);

        captcha = generateCaptcha();
        captchaLabel.setText(captcha);
    }

    public void initData(Quiz selectedQuiz) {
        this.selectedQuiz=selectedQuiz;
    }
}