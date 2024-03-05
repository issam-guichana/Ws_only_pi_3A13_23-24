package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            navigateToPaymentPage();
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

    private void navigateToPaymentPage() {
        try {
            // Get the current stage
            Stage currentStage = (Stage) confirmPaymentButton.getScene().getWindow();

            // Load the PaimentEventFXML.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paimenteventFXML.fxml"));
            Parent root = loader.load();

            // Create a new stage for the payment scene
            Stage paymentStage = new Stage();
            paymentStage.setScene(new Scene(root));

            // Get the reference to the PaimentEventFXML controller
            PaimenteventFXML paimentEventController = loader.getController();

            // Additional actions or data passing can be done here

            // Set the ParticiperFXML controller reference
            paimentEventController.setParticiperController(participerController);

            // Close the current stage (old scene)
            currentStage.close();

            // Show the new stage (payment scene)
            paymentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
