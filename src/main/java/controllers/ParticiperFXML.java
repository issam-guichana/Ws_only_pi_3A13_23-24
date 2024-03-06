package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.SmsSender;

import java.io.IOException;
import java.util.Random;

public class ParticiperFXML {
    @FXML
    private CalendarController calendarController;
    private String generatedVerificationCode;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button confirmPaymentButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        // You can perform any initialization here
    }

    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }

    @FXML
    private void handleConfirmButton(ActionEvent event) {
        // Get the entered phone number
        String phoneNumber = phoneNumberField.getText();

        // Validate the phone number
        if (!isValidPhoneNumber(phoneNumber)) {
            // Show an alert for an invalid phone number
            showAlert("Invalid Phone Number", "Please enter a valid 8-digit phone number.");
            return;
        }

        // Generate a verification code (you can use a more secure method)
        generatedVerificationCode = generateVerificationCode();

        // Send the verification code via SMS
        boolean isSmsSent = sendVerificationCodeViaSMS(phoneNumber, generatedVerificationCode);

        if (isSmsSent) {
            // Show an alert with instructions
            showAlert("Verification Code Sent", "A verification code has been sent to your phone number.");

            // Load the Verification Code scene
            loadVerificationCodeScene();

            // Close the current stage (old scene)
            Stage stage = (Stage) confirmPaymentButton.getScene().getWindow();
            stage.close();
        } else {
            // Show an alert if there was an issue sending the SMS
            showAlert("SMS Error", "Failed to send the verification code via SMS. Please try again.");
        }
    }

    public String getGeneratedVerificationCode() {
        return generatedVerificationCode;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // You can add more validation logic based on your requirements
        return phoneNumber.matches("\\d{8}"); // Assuming a basic 8-digit phone number validation
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String generateVerificationCode() {
        // Generate a random 4-digit code
        int code = new Random().nextInt(9000) + 1000;
        return String.valueOf(code);
    }

    private boolean sendVerificationCodeViaSMS(String phoneNumber, String verificationCode) {
        // Use the SmsSender class to send the verification code via SMS
        return SmsSender.sendSms(phoneNumber, "Your verification code is: " + verificationCode);
    }
    @FXML
    private void loadVerificationCodeScene() {
        try {
            // Get the current stage
            Stage currentStage = (Stage) confirmPaymentButton.getScene().getWindow();

            // Load the VerificationcodeFXML.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VerificationcodeFXML.fxml"));
            Parent root = loader.load();

            // Create a new stage for the verification code scene
            Stage verificationCodeStage = new Stage();
            verificationCodeStage.setScene(new Scene(root));

            // Get the reference to the VerificationcodeFXML controller
            VerificationcodeFXML verificationCodeController = loader.getController();

            // Set the generatedVerificationCode in the VerificationCodeFXML controller
            verificationCodeController.setGeneratedVerificationCode(getGeneratedVerificationCode());

            // Set the ParticiperFXML controller reference
            verificationCodeController.setParticiperController(this);

            // Close the current stage (old scene)
            currentStage.close();

            // Show the new stage (verification code scene)
            verificationCodeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToCalendar(javafx.event.ActionEvent actionEvent) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();

            // Load the CalendarFXML.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendierFXML.fxml"));
            Parent root = loader.load();

            // Create a new stage for the calendar scene
            Stage calendarStage = new Stage();
            calendarStage.setScene(new Scene(root));

            // Get the reference to the CalendarController
            CalendarController calendarController = loader.getController();


            // Close the current stage (old scene)
            currentStage.close();

            // Show the new stage (calendar scene)
            calendarStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


