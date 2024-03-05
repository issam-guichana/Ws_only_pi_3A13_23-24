package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.Evenement;
import services.Payment;

import java.io.IOException;

public class VerificationcodeFXML {

    @FXML
    private CalendarController calendarController;

    @FXML
    private TextField verificationCodeField;

    @FXML
    private Button confirmPaymentButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label messageLabel;

    private ParticiperFXML participerController;
    private String generatedVerificationCode;

    public void setParticiperController(ParticiperFXML participerController) {
        this.participerController = participerController;
    }

    public void setGeneratedVerificationCode(String code) {
        this.generatedVerificationCode = code;
    }

    @FXML
    private void initialize() {
        // You can perform any initialization here
    }
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    Float prix = 150F;
    @FXML
    private void handleConfirmPaymentButton(ActionEvent event) {
        // Get the entered verification code
        String enteredCode = verificationCodeField.getText();
        String verificationCode = participerController.getGeneratedVerificationCode();

        // Assuming you have a method to verify the entered code
        if (isValidVerificationCode(enteredCode, verificationCode)) {
            // Code is valid, proceed to the payment page
            messageLabel.setText("Verification successful. Redirecting to the payment page...");
            // Implement code to navigate to the payment page
           // navigateToPaymentPage();
            Evenement evenement =new Evenement();
            webEngine.load("https://dashboard.stripe.com/test/payments");
            StackPane root = new StackPane();
            root.getChildren().addAll(webView);
            Payment p = new Payment();
            long priceLong = (long) (prix*0.32) *100;
            p.processPayment(priceLong);
            // Create a Scene and add the StackPane to it
            Scene scene = new Scene(root, 800, 600);
            Stage primaryStage = new Stage();
            // Set the Scene to the Stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Load Web Page on Button Click");
            primaryStage.show();
        } else {
            // Code is not valid, show an error message
            messageLabel.setText("Invalid verification code. Please try again.");
        }
    }

    @FXML
    public void goToCalendar(javafx.event.ActionEvent actionEvent) {
        try {
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
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

    private boolean isValidVerificationCode(String enteredCode, String expectedCode) {
        // Validate that enteredCode is not null or empty
        if (enteredCode == null || enteredCode.isEmpty()) {
            return false;
        }

        // Implement a constant time comparison to mitigate timing attacks
        boolean isValid = true;
        for (int i = 0; i < enteredCode.length(); i++) {
            // Use XOR for constant time comparison
            isValid &= enteredCode.charAt(i) == expectedCode.charAt(i);
        }

        return isValid;
    }


}
