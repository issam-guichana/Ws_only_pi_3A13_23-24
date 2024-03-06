package controllers;
import models.Question;
import models.Quiz;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailUtil {

    // Method to send email with PDF attachment
    public static void send(String toEmail , byte[] attachmentData) {
        String from = "sadok.mestiri@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sadok.mestiri@gmail.com", "oqyk kemu jrgs dxmt");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(from, "Formini.tn", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Quiz à faire");

            // Create Multipart
            Multipart multipart = new MimeMultipart();

            // Add Text Part
            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Cher(e) Monsieur/Madame," + "\n\n" +
                    "Vous avez un quiz à faire attribué par votre formateur." + "\n\n" +
                    "Ci-dessous, vous trouverez les détails de votre quiz :\n\n");
            multipart.addBodyPart(textBodyPart);

            // Add PDF Part
            BodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachmentData, "application/pdf")));
            pdfBodyPart.setFileName("Quiz_a_faire.pdf");
            multipart.addBodyPart(pdfBodyPart);

            // Set Content
            message.setContent(multipart);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

