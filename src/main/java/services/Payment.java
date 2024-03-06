package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class Payment {
    public void processPayment(long price) {
        try {
            // Set your secret key here
            Stripe.apiKey = "sk_test_51Oqx0WE8iyMaB3ndGAVXUlntCtfeo5Pqd5efNY8yAq1XIdHx6yoRy7bqIcSSAzAyncrIHys3T59RbANu0yAlcbnU00olKYtOyN";

            // Create a PaymentIntent with other payment details
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(price) // Amount in cents (e.g., $10.00)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // If the payment was successful, display a success message
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
        } catch (StripeException e) {
            // If there was an error processing the payment, display the error message
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }
}