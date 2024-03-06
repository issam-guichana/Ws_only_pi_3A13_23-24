package controllers;

import models.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentFXML implements Initializable {

    @FXML
    private Label total;
    @FXML
    private Button pay_btn;
    @FXML
    private TextField email;
    @FXML
    private TextField num_card;
    @FXML
    private Spinner<Integer> MM;
    @FXML
    private Spinner<Integer> YY;
    @FXML
    private Spinner<Integer> cvc;
    @FXML
    private TextField client_name;
    @FXML
    private Button back_btn;

    private float total_pay;
    private TextField spinnerTextField;

    private boolean isEmpty(TextField field) {
        return field.getText().trim().isEmpty();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String STRIPE_SECRET_KEY = "sk_test_51Oqy3NJsoP2nhhVcJxoSx2GVpetMR0MqADUsz272WU3w4upLiIk8njJDDev2u64nGgdanH3wCQvh2RCBG16vvGMI00XiieGZIj";

        try {
            // Create a PaymentIntent with other payment details
            Stripe.apiKey = STRIPE_SECRET_KEY;
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L) // Amount in cents (e.g., $10.00)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // If the payment was successful, display a success message
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
        } catch (StripeException e) {
            // If there was an error processing the payment, display the error message
            System.out.println("Payment failed. Error: " + e.getMessage());
        }

        // Set default values for MM (month) and YY (year) spinners
        int mm = LocalDate.now().getMonthValue();
        int yy = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> valueFactory_month = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, mm, 1);
        SpinnerValueFactory<Integer> valueFactory_year = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999999, yy, 1);
        SpinnerValueFactory<Integer> valueFactory_cvc = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1, 1);
        MM.setValueFactory(valueFactory_month);
        YY.setValueFactory(valueFactory_year);
        cvc.setValueFactory(valueFactory_cvc);

        // Listener to sync the spinner value with the text field
        spinnerTextField = cvc.getEditor();
        spinnerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                cvc.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                // Handle invalid input (e.g., display an error message)
            }
        });
    }

    public void setData(float total_price) {
        this.total_pay = total_price;
        String total_txt = "Total : " + String.valueOf(total_pay) + " Dt.";
        total.setText(total_txt);
    }

    private boolean isValidEmail(String email) {
        // Implement your logic to validate email format using a regular expression
        String regex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean check_cvc(int cvc) {
        // Check if the CVC code is a 3- or 4-digit number
        if (String.valueOf(cvc).length() != 3 && String.valueOf(cvc).length() != 4) {
            return false;
        }
        // You can add further validation based on specific card types
        // (e.g., some card types have specific ranges for their CVC codes)
        // Assuming the CVC code is valid after the length check
        return true;
    }

    private boolean check_expDate(int year, int month) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Check if the entered year is in the future
        if (year < currentDate.getYear()) {
            return false;
        }
        // Check if the entered month is in the future (if the year is the same)
        if (year == currentDate.getYear() && month < currentDate.getMonthValue()) {
            return false;
        }
        // Assuming the expiration date is valid after these checks
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void payment(ActionEvent event) throws StripeException {
        // Input validation
        long priceLong = (long) (total_pay * 0.32) * 100;
        Payment.processPayment(priceLong);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(" paiement ");
        alert.setHeaderText(null);
        alert.setContentText("paiement avec succès!");
        alert.showAndWait();
    }
}
