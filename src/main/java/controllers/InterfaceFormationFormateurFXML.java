package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.apache.commons.codec.binary.Base64;
import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class InterfaceFormationFormateurFXML {

    private static final String APPLICATION_NAME = "Formini.tn";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_847964430065-jnpg1qpn515spb2mi4odcujpfkqc5pf5.apps.googleusercontent.com.json"; // Define your credentials file path here
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_SEND);
    private static final String senderEmail = "habou.mariem@gmail.com"; // Replace with sender email address
    private static final String recipientEmail = "mariem.habouria00@gmail.com"; // Replace with recipient email address

    private Gmail service;

    @FXML
    private Label statusLabel;
    @FXML
    private Button ButtonFormations;

    public InterfaceFormationFormateurFXML() throws IOException {
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                sendEmail(selectedFile);
            } catch (IOException | MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                statusLabel.setText("Failed to send email.");
            }
        }
    }

    private void sendEmail(File attachmentFile) throws IOException, MessagingException, GeneralSecurityException {
        initializeService();
        MimeMessage mimeMessage = createEmail(recipientEmail, senderEmail, "Attachment Example", "Please find the attached PDF file.");
        MimeBodyPart attachmentPart = new MimeBodyPart();
        javax.activation.DataSource source = new FileDataSource(attachmentFile);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(attachmentFile.getName());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(attachmentPart);

        mimeMessage.setContent(multipart);

        Message message = createMessageWithEmail(mimeMessage);
        message = service.users().messages().send("me", message).execute();

        statusLabel.setText("Email sent successfully!");
    }

    private void initializeService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = InterfaceFormationFormateurFXML.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    @FXML
    void handleButtonFormations(ActionEvent event) throws IOException {
        // Load the FXML file for the formations interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceTFormateurFXML.fxml"));
        Parent formationsParent = loader.load();

        // Get the current stage and set its scene to the formations interface scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(formationsParent);
        stage.setScene(scene);
        stage.show();
    }
}
