package controllers;

import controllers.PdfCertificat;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Certificat;
import services.ServiceCertificat;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class AfficahgeCertificatFXML {

    @FXML
    private VBox idBadgeContainer;

    private ServiceCertificat certificatService = new ServiceCertificat();
    private List<Certificat> allCertificats;

    private int pageSize = 10; // Number of certificates per page

    private PdfCertificat pdfCertificat = new PdfCertificat(); // Création d'une instance

    public void displayCertificatsForPage(int page) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allCertificats.size());

        idBadgeContainer.getChildren().clear(); // Clear previous certificate items

        GridPane certificatsGrid = new GridPane();
        certificatsGrid.setHgap(20); // Horizontal gap between columns
        certificatsGrid.setVgap(20); // Vertical gap between rows

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            Certificat certificat = allCertificats.get(i);

            // Create a VBox to hold the components of each certificate
            VBox certificatBox = new VBox();
            certificatBox.setSpacing(20); // Vertical spacing between components
            certificatBox.setAlignment(Pos.CENTER); // Align components to the center horizontally
            certificatBox.setStyle("-fx-background-color: #E68C3AFF;-fx-background-radius: 10px; -fx-padding: 20px;"); // Set background color
            certificatBox.setEffect(new DropShadow());

            // Create a label to display the certificate name
            Label certificatNameLabel = new Label(certificat.getNomCertif());
            certificatNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

            // Create a button to save the certificate
            Button saveButton = new Button("Save");
            saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
            // Add event handler to the button to handle saving the certificate
            int finalI = i; // Pour l'utilisation dans la lambda
            saveButton.setOnAction(event -> {
                // Appeler la méthode generateCertificate de PdfCertificat avec l'ID du certificat
                pdfCertificat.generateCertificate(certificat.getNomCertif(), new File("C:/Users/rymra/Downloads/Certificat.png"));
            });

            // Add nodes to the VBox in the desired order
            certificatBox.getChildren().addAll(certificatNameLabel, saveButton);

            // Add the VBox to the grid at the specified column and row
            certificatsGrid.add(certificatBox, col, row);

            // Increment column index and reset to 0 if it reaches 4
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        // Add the grid to the idCertifContainer
        idBadgeContainer.getChildren().add(certificatsGrid);

        // Log message for debugging
        System.out.println("Displayed certificates: " + startIndex + " to " + (endIndex - 1));
    }

    // Method to handle saving the certificate
    private void saveCertificat(int certificatId) {
        // Implement your logic to save the certificate with the given ID
        System.out.println("Saving certificate with ID: " + certificatId);
    }

    // This method can be called to initialize the certificates and display the first page
    public void initialize() {
        try {
            allCertificats = certificatService.selectAll();
            displayCertificatsForPage(1); // Display the first page of certificates initially
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}
